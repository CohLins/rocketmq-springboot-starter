package cn.colins.rocketmqstarter.config;

import cn.colins.rocketmqstarter.consumer.config.RocketMqConsumerConfig;
import cn.colins.rocketmqstarter.processor.RocketMqAnnotationBeanPostProcessor;
import cn.colins.rocketmqstarter.producer.factory.RocketMqProducerFactory;
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
@EnableConfigurationProperties({RocketMqProducerConfig.class, RocketMqConsumerConfig.class})
public class RocketMqAutoConfiguration {

    @Bean
    @ConditionalOnProperty(value = {"rocket-mq.producer.enabled"}, havingValue = "true")
    public RocketMqProducerFactory rocketMqFactory(RocketMqProducerConfig rocketMqProducerConfig){
        return new RocketMqProducerFactory(rocketMqProducerConfig);
    }

    @Bean
    @ConditionalOnBean(RocketMqProducerFactory.class)
    public RocketMqAnnotationBeanPostProcessor rocketMqAnnotationBeanPostProcessor(RocketMqProducerFactory rocketMqProducerFactory){
        return new RocketMqAnnotationBeanPostProcessor(rocketMqProducerFactory);
    }

}