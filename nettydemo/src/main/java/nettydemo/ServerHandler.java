package nettydemo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


/**
 * @author 15510
 * @create 2019-06-27 9:59
 */
//标识同一个ChannelHandler的实例可以被多次添加到多个ChannelPipelines中，而且不会出现竞争条件。
//如果一个ChannelHandler没有标志@Sharable，在添加到到一个pipeline中时，你需要每次都创建一个新的handler实例，因为它的成员变量是不可分享的。
@Sharable
public class ServerHandler  extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        //ctx.write(msg);  //加没加这句话没影响
        String body = (String) msg;
        System.out.println(body);
        String req = "hello client! this message is from server" + System.getProperty("line.separator");
        ByteBuf resp = Unpooled.copiedBuffer(req.getBytes());
        ctx.writeAndFlush(resp);
        // 因为前面server配置了StringEncoder，所以，直接write字符串也是可以的
        ctx.writeAndFlush("测试这个行不行" + System.getProperty("line.separator"));

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
