# 自定义协议


对上一个版本做的改进。
1. 上一个版本只能推送 `MyMessage` 
2. 其实本质是将推送的内容给到 `bytebuf` ，这样我们就能想到序列化。将对象变成字节。毕竟网络传输也是字节。采用了Marshalling序列化框架
3. 序列化的话，那么我们就可能实现多种对象的序列化和反序列化。可以推送`MyMessage2` `MyMessage3`这样的对象。**也就是说可以crt.writeAndFlush任何对象**
4. 这个例子还是只有`MyMessage` 。要实现3的话，写一个been类`MyMessage2`，
ctx.writeAndFlush(new MyMessage2(new MyHead(content.getBytes("UTF-8").length, 1), content));


---
做的改动如下


`pom.xml`
```xml
<dependency>
    <groupId>org.jboss.marshalling</groupId>
    <artifactId>jboss-marshalling-serial</artifactId>
    <version>2.0.0.Beta2</version>
</dependency>
```


bean类全部要implements Serializable
```java
public class MyHead implements Serializable {...}
public class MyMessage implements Serializable {...}
```


新建MarshallingCodeCFactory.java
```java
public final class MarshallingCodeCFactory {

    /**
     * 创建Jboss Marshalling×××MarshallingDecoder
     * @return MarshallingDecoder
     */
    public static MarshallingDecoder buildMarshallingDecoder() {
        //首先通过Marshalling工具类的精通方法获取Marshalling实例对象 参数serial标识创建的是java序列化工厂对象。
        final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
        //创建了MarshallingConfiguration对象，配置了版本号为5
        final MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        //根据marshallerFactory和configuration创建provider
        UnmarshallerProvider provider = new DefaultUnmarshallerProvider(marshallerFactory, configuration);
        //构建Netty的MarshallingDecoder对象，俩个参数分别为provider和单个消息序列化后的最大长度
        MarshallingDecoder decoder = new MarshallingDecoder(provider, 1024);
        return decoder;
    }

    /**
     * 创建Jboss Marshalling编码器MarshallingEncoder
     * @return MarshallingEncoder
     */
    public static MarshallingEncoder buildMarshallingEncoder() {
        final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
        final MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        MarshallerProvider provider = new DefaultMarshallerProvider(marshallerFactory, configuration);
        //构建Netty的MarshallingEncoder对象，MarshallingEncoder用于实现序列化接口的POJO对象序列化为二进制数组
        MarshallingEncoder encoder = new MarshallingEncoder(provider);
        return encoder;
    }
}
```



client.java
```java
p.addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
p.addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
p.addLast(new ClientHandler());
```


server.java
```java
p.addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
p.addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
p.addLast(new ServerHandler());
```

