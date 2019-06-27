package nettydemo.bean;

import lombok.Data;

/**
 * @author 15510
 * @create 2019-06-27 16:16
 */
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