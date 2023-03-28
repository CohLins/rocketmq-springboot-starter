package cn.colins.rocketmqstarter.config;

import cn.colins.rocketmqstarter.processor.RocketMqAnnotationBeanPostProcessor;
import cn.colins.rocketmqstarter.producer.RocketMqFactory;
import cn.colins.rocketmqstarter.producer.config.RocketMqProducerConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description
 * @Author czl
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2023/3/28
 */
@Configuration
@EnableConfigurationProperties(RocketMqProducerConfig.class)
@ConditionalOnProperty(value = {"rocket-mq.producer.enabled"}, havingValue = "true")
public class RocketMqAutoConfiguration {

    @Bean
    public RocketMqFactory rocketMqFactory(RocketMqProducerConfig rocketMqProducerConfig){
        return new RocketMqFactory(rocketMqProducerConfig);
    }

    @Bean
    @ConditionalOnBean(RocketMqFactory.class)
    public RocketMqAnnotationBeanPostProcessor rocketMqAnnotationBeanPostProcessor(RocketMqFactory rocketMqFactory){
        return new RocketMqAnnotationBeanPostProcessor(rocketMqFactory);
    }
}
