package cn.colins.rocketmqstarter.consumer.config;

import java.util.Set;

/**
 * @Description
 * @Author czl
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2023/3/28
 */
public class RocketMqConsumerBaseConfig {
    private String namesrvAddr;
    private String consumerGroup;
    private Set<RocketMqConsumerSubscribe> subscribes;
    private Integer consumeThreadMin;
    private Integer consumeThreadMax;
    private Integer consumeMessageBatchMaxSize;
    private Integer pullBatchSize;
    private Integer consumeTimeout;
    private Integer maxReconsumeTimes;


    public String getNamesrvAddr() {
        return namesrvAddr;
    }

    public void setNamesrvAddr(String namesrvAddr) {
        this.namesrvAddr = namesrvAddr;
    }

    public String getConsumerGroup() {
        return consumerGroup;
    }

    public void setConsumerGroup(String consumerGroup) {
        this.consumerGroup = consumerGroup;
    }



    public Integer getConsumeThreadMin() {
        return consumeThreadMin;
    }

    public void setConsumeThreadMin(Integer consumeThreadMin) {
        this.consumeThreadMin = consumeThreadMin;
    }

    public Integer getConsumeThreadMax() {
        return consumeThreadMax;
    }

    public void setConsumeThreadMax(Integer consumeThreadMax) {
        this.consumeThreadMax = consumeThreadMax;
    }

    public Integer getConsumeMessageBatchMaxSize() {
        return consumeMessageBatchMaxSize;
    }

    public void setConsumeMessageBatchMaxSize(Integer consumeMessageBatchMaxSize) {
        this.consumeMessageBatchMaxSize = consumeMessageBatchMaxSize;
    }

    public Integer getPullBatchSize() {
        return pullBatchSize;
    }

    public void setPullBatchSize(Integer pullBatchSize) {
        this.pullBatchSize = pullBatchSize;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((namesrvAddr == null) ? 0 : namesrvAddr.hashCode());
        result = prime * result + ((consumerGroup == null) ? 0 : consumerGroup.hashCode());
        result = prime * result + ((subscribes == null) ? 0 : subscribes.hashCode());
        result = prime * result + ((consumeThreadMin == null) ? 0 : consumeThreadMin.hashCode());
        result = prime * result + ((consumeThreadMax == null) ? 0 : consumeThreadMax.hashCode());
        result = prime * result + ((consumeMessageBatchMaxSize == null) ? 0 : consumeMessageBatchMaxSize.hashCode());
        result = prime * result + ((pullBatchSize == null) ? 0 : pullBatchSize.hashCode());
        result = prime * result + ((consumeTimeout == null) ? 0 : consumeTimeout.hashCode());
        result = prime * result + ((maxReconsumeTimes == null) ? 0 : maxReconsumeTimes.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RocketMqConsumerBaseConfig other = (RocketMqConsumerBaseConfig) obj;
        if (consumerGroup == null) {
            if (other.consumerGroup != null)
                return false;
        } else if (!consumerGroup.equals(other.consumerGroup))
            return false;
        return true;
    }


    public Integer getConsumeTimeout() {
        return consumeTimeout;
    }

    public void setConsumeTimeout(Integer consumeTimeout) {
        this.consumeTimeout = consumeTimeout;
    }

    public Integer getMaxReconsumeTimes() {
        return maxReconsumeTimes;
    }

    public void setMaxReconsumeTimes(Integer maxReconsumeTimes) {
        this.maxReconsumeTimes = maxReconsumeTimes;
    }

    public void setSubscribes(Set<RocketMqConsumerSubscribe> subscribes) {
        this.subscribes = subscribes;
    }

    public Set<RocketMqConsumerSubscribe> getSubscribes() {
        return this.subscribes;
    }
}
