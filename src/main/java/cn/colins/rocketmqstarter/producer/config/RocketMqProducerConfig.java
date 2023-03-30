package cn.colins.rocketmqstarter.producer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @Description
 * @Author czl
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2023/3/28
 */
@ConfigurationProperties(prefix = "rocket-mq.producer")
public class RocketMqProducerConfig {
    private boolean enabled=false;
    private String namesrvAddr;
    private List<DefaultMQProducerConfig> defaultMqProducerConfigs;

    public List<DefaultMQProducerConfig> getDefaultMqProducerConfigs() {
        return defaultMqProducerConfigs;
    }

    public void setDefaultMqProducerConfigs(List<DefaultMQProducerConfig> defaultMQProducerConfigs) {
        this.defaultMqProducerConfigs = defaultMQProducerConfigs;
    }

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
