package nettydemo.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 15510
 * @create 2019-06-27 16:17
 */
@Data
public class MyMessage implements Serializable {
    //消息head
    private MyHead head;
    //消息body
    private String content;

    public MyMessage(MyHead head, String content) {
        this.head = head;
        this.content = content;
    }
}
