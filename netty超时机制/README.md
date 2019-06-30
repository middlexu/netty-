# 超时检测

`IdleStateHandler(读超时，写超时，所有超时)`

自定义的handler重写
`userEventTriggered`方法


一般来说，超时3次左右，可以ctx.close()避免资源浪费