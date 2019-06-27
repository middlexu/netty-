package nettydemo;


import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import nettydemo.bean.MyHead;
import nettydemo.bean.MyMessage;

import java.io.UnsupportedEncodingException;


/**
 * @author 15510
 * @create 2019-06-27 9:59
 */
//标识同一个ChannelHandler的实例可以被多次添加到多个ChannelPipelines中，而且不会出现竞争条件。
//如果一个ChannelHandler没有标志@Sharable，在添加到到一个pipeline中时，你需要每次都创建一个新的handler实例，因为它的成员变量是不可分享的。
@Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter {
    private String content = "hello, 客户端";

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {
        System.out.println(msg.getClass());//nettydemo.bean.MyMessage
        System.out.println(msg);
        ctx.writeAndFlush(new MyMessage(new MyHead(content.getBytes("UTF-8").length, 1), content));

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
