package cn.colins.rocketmqstarter.consumer.config;

import cn.colins.rocketmqstarter.producer.config.DefaultMQProducerConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @Description
 * @Author czl
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2023/3/28
 */
@ConfigurationProperties(prefix = "rocket-mq.consumer")
public class RocketMqConsumerConfig {
    private boolean enabled=false;
    private String namesrvAddr;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getNamesrvAddr() {
        return namesrvAddr;
    }

    public void setNamesrvAddr(String namesrvAddr) {
        this.namesrvAddr = namesrvAddr;
    }
}
