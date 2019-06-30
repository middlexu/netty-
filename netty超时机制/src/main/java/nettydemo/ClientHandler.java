package nettydemo;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelHandler.Sharable;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


/**
 * @author 15510
 * @create 2019-06-27 10:06
 */
@Sharable
public class ClientHandler extends ChannelInboundHandlerAdapter {

    private String clientIP;
    private int clientPort;
    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> scheduledFuture;


    @Override
    public void channelActive(ChannelHandlerContext ctx){
        // 获取本地ip和port
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().localAddress();
        this.clientIP = insocket.getAddress().getHostAddress();
        this.clientPort = insocket.getPort();
        System.out.println(this.clientIP + ":" + this.clientPort);
        String authInfo = this.clientIP;// + ":" + this.clientPort;
        ctx.writeAndFlush(authInfo);

        for (;;) {
            try {
                ctx.writeAndFlush("heartBeat");// + System.getProperty("line.separator"));
                TimeUnit.MILLISECONDS.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    // 从channel读数据
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        System.out.println(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();//.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();

        if(this.scheduledFuture != null){
            this.scheduledFuture.cancel(true);
            this.scheduledFuture = null;
        }

        ctx.close(); // 这个会关闭客户端
    }
}
