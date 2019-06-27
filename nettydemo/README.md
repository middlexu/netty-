全是套路，代码很简单。
可能需要改的是p.addLast(各种handler)

想象成管道。两端可以加各种handler(handler是选择执行)

中间传输数据底层还是bytebuf

ctx.writeAndFlush(obj)
1. 经过handler/encoder要能转成bytebuf才能发送出去
2. 接收方要有对应的handler/decoder转成相应的obj
3. 因为服务器要和客户端互发消息，所有两个都要有encoder和decoder。发送消息的时候，一个xxcoder不走；接收消息的时候，另一个xxcoder不走


主要参考https://blog.51cto.com/zhangfengzhe/1890017
博客的是netty5，现在已经弃用不更新。主流是netty4。本例是netty4.1