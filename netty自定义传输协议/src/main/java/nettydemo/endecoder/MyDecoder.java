package nettydemo.endecoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import nettydemo.bean.MyHead;
import nettydemo.bean.MyMessage;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @author 15510
 * @create 2019-06-27 16:19
 *
 * 将ByteBuf数据转化成自定义消息
 */
public class MyDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {

        int length = byteBuf.readInt();
        int version = byteBuf.readInt();

        byte[] body = new byte[length];
        byteBuf.readBytes(body);

        String content = new String(body, Charset.forName("UTF-8"));

        MyMessage myMessage = new MyMessage(new MyHead(length,version),content);

        list.add(myMessage);
    }
}
