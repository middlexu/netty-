package nettydemo.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 15510
 * @create 2019-06-27 22:59
 */
public class HeartInfo implements Serializable {

    private String ip;

    private int port;

    private Date lasttime;

    private Map<String , String> cpuInfo = new HashMap<String,String>();

    private Map<String , String> memInfo = new HashMap<String, String>();

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Date getLasttime() {
        return lasttime;
    }

    public void setLasttime(Date lasttime) {
        this.lasttime = lasttime;
    }

    public Map<String, String> getCpuInfo() {
        return cpuInfo;
    }

    public void setCpuInfo(Map<String, String> cpuInfo) {
        this.cpuInfo = cpuInfo;
    }

    public Map<String, String> getMemInfo() {
        return memInfo;
    }

    public void setMemInfo(Map<String, String> memInfo) {
        this.memInfo = memInfo;
    }

    @Override
    public String toString() {
        return "HeartInfo{" +
            "ip='" + ip + '\'' +
            ", port=" + port +
            ", lasttime=" + lasttime +
            ", cpuInfo=" + cpuInfo +
            ", memInfo=" + memInfo +
            '}';
    }
}
