# 自定义协议


服务器、客户端 都是推送MyMessage类消息给对方

ctx.writeAndFlush(new MyMessage(new MyHead(content.getBytes("UTF-8").length, 1), content));



`MyMessage` 由 `head` 和 `content` 组成
```java
@Data
public class MyMessage {
    //消息head
    private MyHead head;
    //消息body
    private String content;

    public MyMessage(MyHead head, String content) {
        this.head = head;
        this.content = content;
    }
}
```


`MyHead` 由 `length`消息长度 和 `version`版本号构成。消息长度指的是`MyMessage`中的`content`的长度
```java
@Data
public class MyHead {

    //数据长度
    private int length;

    //数据版本
    private int version;


    public MyHead(int length, int version) {
        this.length = length;
        this.version = version;
    }
}
```


`MyDecoder` 将 `bytebuf` 转换为 `MyMessage`。接收消息的时候会执行这个

先读两个int，求出消息体的长度length。再读length个byte。最后组合成一个message对象

这样解决了拆包、粘包问题

```java
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
```




`MyEncoder` 将 `MyMessage` 转换为 `bytebuf`。发送消息的时候会执行这个
```java
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
```



client.java中添加MyDecoder，MyEncoder，ClientHandler
```java
p.addLast(new MyDecoder());
p.addLast(new MyEncoder());
p.addLast(new ClientHandler());
```

server.java中添加MyDecoder，MyEncoder，ClientHandler
```java
p.addLast(new MyDecoder());
p.addLast(new MyEncoder());
p.addLast(new ServerHandler());
```


`ClientHandler` 和 `ServerHandler` 逻辑请具体看代码