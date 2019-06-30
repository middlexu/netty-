package nettydemo;


import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import nettydemo.bean.HeartInfo;
import java.util.ArrayList;
import java.util.List;



/**
 * @author 15510
 * @create 2019-06-27 9:59
 */
//标识同一个ChannelHandler的实例可以被多次添加到多个ChannelPipelines中，而且不会出现竞争条件。
//如果一个ChannelHandler没有标志@Sharable，在添加到到一个pipeline中时，你需要每次都创建一个新的handler实例，因为它的成员变量是不可分享的。
@Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter {
    //KEY: ip:port VALUE: HeartInfo
//    private Map<String,HeartInfo> heartInfoMap = new HashMap<String, HeartInfo>();

    private static final List<String> authList = new ArrayList<String>();

    static {
        //从其他地方加载出来的IP列表
        authList.add("192.168.31.13");
        authList.add("127.0.0.1");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if(msg instanceof String){

            if(authList.contains(msg)){ //验证通过
                ctx.writeAndFlush("OK");
            }else{
                ctx.writeAndFlush("不在认证列表中...");
                ctx.close(); //验证没通过，把channel关闭，不要浪费服务器资源
            }

        }else if(msg instanceof HeartInfo){

            System.out.println((HeartInfo)msg);

            ctx.writeAndFlush("心跳接收成功!");

            HeartInfo heartInfo = (HeartInfo)msg;
//            heartInfoMap.put(heartInfo.getIp() + ":" + heartInfo.getPort(),heartInfo);
        }

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
