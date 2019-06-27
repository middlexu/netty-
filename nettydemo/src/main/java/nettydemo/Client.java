package nettydemo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * @author 15510
 * @create 2019-06-27 10:05
 */
public class Client {

    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8888"));
    static final int SIZE = Integer.parseInt(System.getProperty("size", "256"));

    public static void main(String[] args) throws Exception {

        // Configure the client.
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
             .channel(NioSocketChannel.class)
             .option(ChannelOption.TCP_NODELAY, true)
             .handler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                     ChannelPipeline p = ch.pipeline();
                     p.addLast(new LineBasedFrameDecoder(1024)); //拆包、粘包问题.以换行符分割
//                     p.addLast(new StringDecoder()); //ByteBuf to String，数据流进来时处理，变成string
//                     p.addLast(new StringEncoder()); //String to ByteBuf，数据流出去时处理，变成bytebuf
                     //p.addLast(new LoggingHandler(LogLevel.INFO));

                     //超时handler（当服务器端与客户端在指定时间以上没有任何进行通信，则会关闭相应的通道，主要为减小服务端资源占用）
                     p.addLast(new ReadTimeoutHandler(10));
                     p.addLast(new ClientHandler());
                 }
             });

            // Start the client.
            ChannelFuture f = b.connect(HOST, PORT).sync();

            System.out.println(f.channel().isActive());//true
            System.out.println(f.channel().isOpen());//true

            // Wait until the connection is closed.阻塞的
            f.channel().closeFuture().sync();


            System.out.println(f.channel().isActive());//false
            System.out.println(f.channel().isOpen());//false
        } finally {
            // Shut down the event loop to terminate all threads.
            group.shutdownGracefully();
        }
    }

}

