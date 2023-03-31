package cn.colins.rocketmqstarter.config;

import cn.colins.rocketmqstarter.consumer.config.RocketMqConsumerConfig;
import cn.colins.rocketmqstarter.consumer.factory.RocketMqConsumerFactory;
import cn.colins.rocketmqstarter.processor.RocketMqConsumerBeanPostProcessor;
import cn.colins.rocketmqstarter.processor.RocketResourceAnnotationBeanPostProcessor;
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
    public RocketMqProducerFactory rocketMqProducerFactory(RocketMqProducerConfig rocketMqProducerConfig){
        return new RocketMqProducerFactory(rocketMqProducerConfig);
    }

    @Bean
    @ConditionalOnBean(RocketMqProducerFactory.class)
    public RocketResourceAnnotationBeanPostProcessor rocketResourceAnnotationBeanPostProcessor(RocketMqProducerFactory rocketMqProducerFactory){
        return new RocketResourceAnnotationBeanPostProcessor(rocketMqProducerFactory);
    }


    @Bean
    @ConditionalOnProperty(value = {"rocket-mq.consumer.enabled"}, havingValue = "true")
    public RocketMqConsumerFactory rocketMqConsumerFactory(RocketMqConsumerConfig rocketMqConsumerConfig){
        return new RocketMqConsumerFactory(rocketMqConsumerConfig);
    }

    @Bean
    @ConditionalOnBean(RocketMqConsumerFactory.class)
    public RocketMqConsumerBeanPostProcessor rocketMqConsumerBeanPostProcessor(RocketMqConsumerFactory rocketMqConsumerFactory){
        return new RocketMqConsumerBeanPostProcessor(rocketMqConsumerFactory);
    }
}
