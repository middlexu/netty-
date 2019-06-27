package nettydemo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;


/**
 * @author 15510
 * @create 2019-06-27 9:56
 */
public class Server {
    static final int PORT = Integer.parseInt(System.getProperty("port", "8888"));

    public static void main(String[] args) throws Exception {

        // Configure the server.
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .option(ChannelOption.SO_BACKLOG, 1024)//.option(...)

             //handler在初始化时就会执行，而childHandler会在客户端成功connect后才执行，这是两者的区别。
             //记住，一般情况下服务器写.childHandler,客户端写.handler
             .handler(new LoggingHandler(LogLevel.INFO))
             .childHandler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                     ChannelPipeline p = ch.pipeline();
                     //通过Netty的LineBasedFrameDecoder和StringDecoder来解决TCP粘包问题。
                     p.addLast(new LineBasedFrameDecoder(1024));//工作原理是它依次遍历ByteBuf中的可读字节，判断看是否有“\n”或者“\r\n”,如果有就以此位置为结束位置，从可读索引到结束位置区间的字节就组成了一行。它是以换行符为结束标志的解码器，支持携带结束符或者不携带结束符两种解码方式，同时支持配置单行的最大长度。如果连续读取到最大长度后仍然没有发现换行符，就会抛出异常，同时忽略掉之前读取到的异常码流。

                     // 这些handler处理不了就不会做处理
                     p.addLast(new StringDecoder());//ByteBuf to String，数据流进来时处理，变成string
                     p.addLast(new StringEncoder());//String to ByteBuf，数据流出去时处理，变成bytebuf


                     //p.addLast(new LoggingHandler(LogLevel.INFO));
                     p.addLast(new ServerHandler());
                 }
             });

            // Start the server.
            ChannelFuture f = b.bind(PORT).sync();

            // Wait until the server socket is closed.
            f.channel().closeFuture().sync();
        } finally {
            // Shut down all event loops to terminate all threads.
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
