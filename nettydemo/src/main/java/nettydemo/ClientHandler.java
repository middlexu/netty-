package nettydemo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelHandler.Sharable;


/**
 * @author 15510
 * @create 2019-06-27 10:06
 */
@Sharable
public class ClientHandler extends ChannelInboundHandlerAdapter {
    private byte[] firstMessage;

    /**
     * Creates a client-side handler.
     */
    public ClientHandler() {
        firstMessage = ("hello server! i am client" + System.getProperty("line.separator")).getBytes();
    }

    // 连接建立，马上就要做的事
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ByteBuf message = null;
        for (int i = 0; i < 10; i++) {
            message = Unpooled.buffer(firstMessage.length);
            message.writeBytes(firstMessage);
            ctx.writeAndFlush(message);
        }
    }

    // 从channel读数据
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        //ctx.write(msg);  //加没加这句话没影响
        //System.out.println(msg.getClass());//msg是String类型,因为clienthandler前面加了StringDecoder
        System.out.println(msg);

        // 博客（netty5）里说：
        //Client/Server端都存在缓冲区，所以我们需要注意，缓冲区的消息释放和刷新。
        // 如果读，那么需要release，如果写，只需要flush(flush的时候已经做了release)进行发送到对方。

        // 但是我试了，不加好像也没啥关系.可能跟版本有关系
        //ReferenceCountUtil.release(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close(); // 这个会关闭客户端
    }
}
