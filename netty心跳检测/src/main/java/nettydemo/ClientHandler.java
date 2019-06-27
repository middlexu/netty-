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
    private static final String SUCCESS = "OK";  // 返回这个表明验证成功

    private String clientIP;
    private int clientPort;
    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> scheduledFuture;


    // 通道建立初始化时  发送信息 准备握手验证
    // Client和Server建立通道初始化的时候，Client会向服务器发送信息用于认证。
    // 在实际开发中，Client在发送心跳前，需要和Server端进行握手验证，会涉及到加解密，
    // 这里为了简单起见，省去了这些过程。
    // 从上面的代码也可以看到，如果服务端认证成功，那么Client会开始启动定时线程去执行任务，那么接下来，我们看看这个心跳任务。
    @Override
    public void channelActive(ChannelHandlerContext ctx){
        // 获取本地ip和port
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().localAddress();
        this.clientIP = insocket.getAddress().getHostAddress();
        this.clientPort = insocket.getPort();
        System.out.println(this.clientIP + ":" + this.clientPort);
        String authInfo = this.clientIP;// + ":" + this.clientPort;
        ctx.writeAndFlush(authInfo);

    }

    // 从channel读数据
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if(msg instanceof String){

            //认证成功
            if(SUCCESS.equals((String)msg)){

                // 2秒之后启动，每隔3秒重新运行
                this.scheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(
                    new HeartTask(ctx,this.clientIP,this.clientPort),3,5, TimeUnit.SECONDS);

            }else{

                System.out.println("服务器发来消息：" + msg);
            }

        }

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
