package nettydemo;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelHandler.Sharable;
import nettydemo.bean.MyHead;
import nettydemo.bean.MyMessage;

import java.io.UnsupportedEncodingException;


/**
 * @author 15510
 * @create 2019-06-27 10:06
 */
@Sharable
public class ClientHandler extends ChannelInboundHandlerAdapter {
    private String content = "hello, 服务器";
    // 连接建立，做的事
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws UnsupportedEncodingException {
        ctx.writeAndFlush(new MyMessage(new MyHead(content.getBytes("UTF-8").length,1),content));

    }

    // 从channel读数据
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        //ctx.write(msg);  //加没加这句话没影响

        System.out.println(msg);
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
