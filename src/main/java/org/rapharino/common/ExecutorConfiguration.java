package org.rapharino.common;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created By Rapharino on 2017/7/27
 */
public class ExecutorConfiguration {

    // name
    private String name;
    // 最大线程数
    private int maximumPoolSize;
    // 核心线程数
    private int corePoolSize;
    // 存活时间
    private long keepAliveTime;
    // 单位
    private String unit;
    // 阻塞队列名称
    private String blockingQueue;
    // 队列容量
    private int blockingQueueCapacity;
    // 拒绝处理器
    private String handler;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public void setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public long getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public TimeUnit getTimeUnit() {
        return map.get(unit);
    }

    private static final Map<String, TimeUnit> map = new HashMap<String, TimeUnit>(8);

    static {
        map.put("DAYS", TimeUnit.DAYS);
        map.put("HOURS", TimeUnit.HOURS);
        map.put("MINUTES", TimeUnit.MINUTES);
        map.put("SECONDS", TimeUnit.SECONDS);
        map.put("MILLISECONDS", TimeUnit.MILLISECONDS);
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getBlockingQueue() {
        return blockingQueue;
    }

    public void setBlockingQueue(String blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    public int getBlockingQueueCapacity() {
        return blockingQueueCapacity;
    }

    public void setBlockingQueueCapacity(int blockingQueueCapacity) {
        this.blockingQueueCapacity = blockingQueueCapacity;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    @Override
    public String toString() {
        return "ExecutorConfiguration{" +
                "name='" + name + '\'' +
                ", maximumPoolSize=" + maximumPoolSize +
                ", corePoolSize=" + corePoolSize +
                ", keepAliveTime=" + keepAliveTime +
                ", unit='" + unit + '\'' +
                ", blockingQueue='" + blockingQueue + '\'' +
                ", blockingQueueCapacity=" + blockingQueueCapacity +
                ", handler=" + handler +
                '}';
    }
}
