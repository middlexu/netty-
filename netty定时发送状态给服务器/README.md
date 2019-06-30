# 定时发送状态给服务器


1. 上一个版本已经可以传输任何对象了
2. 将电脑运行状态发给服务器。比如说，每隔5秒发送电脑的运行状态到服务器。我们可以将状态打包成一个对象



---
做的改动如下


`pom.xml`为了方便收集系统的内存、CPU信息，这里使用了Sigar，也是在实际中引用非常广泛的一个工具。

Sigar参考使用https://blog.csdn.net/junlong750/article/details/85991123
```xml
<dependency>
    <groupId>org.fusesource</groupId>
    <artifactId>sigar</artifactId>
    <version>1.6.4</version>
</dependency>

```

本代码参考地址：https://blog.51cto.com/zhangfengzhe/1895031

