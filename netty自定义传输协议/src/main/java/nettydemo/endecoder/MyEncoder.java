package nettydemo.endecoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import nettydemo.bean.MyMessage;

import java.nio.charset.Charset;

/**
 * @author 15510
 * @create 2019-06-27 16:19
 */
public class MyEncoder extends MessageToByteEncoder<MyMessage> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, MyMessage myMessage, ByteBuf byteBuf) throws Exception {

        int length = myMessage.getHead().getLength();
        int version = myMessage.getHead().getVersion();
        String content = myMessage.getContent();

        byteBuf.writeInt(length);
        byteBuf.writeInt(version);
        byteBuf.writeBytes(content.getBytes(Charset.forName("UTF-8")));

    }
}
