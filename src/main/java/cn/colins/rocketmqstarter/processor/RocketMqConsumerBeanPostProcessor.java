package cn.colins.rocketmqstarter.processor;

import java.util.*;

import cn.colins.rocketmqstarter.consumer.config.RocketMqConsumerConfig;
import cn.colins.rocketmqstarter.consumer.config.RocketMqConsumerSubscribe;

import cn.colins.rocketmqstarter.annotation.RocketMqConsumerHandler;
import cn.colins.rocketmqstarter.annotation.RocketResource;
import cn.colins.rocketmqstarter.consumer.RocketMqMsgHandler;
import cn.colins.rocketmqstarter.consumer.config.RocketMqConsumerBaseConfig;
import cn.colins.rocketmqstarter.consumer.factory.RocketMqConsumerFactory;
import cn.colins.rocketmqstarter.producer.factory.RocketMqProducerFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;

@Slf4j
public class RocketMqConsumerBeanPostProcessor implements InstantiationAwareBeanPostProcessor {

    private RocketMqConsumerFactory rocketMqConsumerFactory;

    private final RocketMqConsumerConfig consumerConfig;

    public RocketMqConsumerBeanPostProcessor(RocketMqConsumerFactory rocketMqConsumerFactory) {
        this.rocketMqConsumerFactory = rocketMqConsumerFactory;
        this.consumerConfig = rocketMqConsumerFactory.getCommonConsumerConfig();
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        if (!RocketMqMsgHandler.class.isAssignableFrom(beanClass)) {
            return null;
        }
        RocketMqConsumerHandler annotation = beanClass.getAnnotation(RocketMqConsumerHandler.class);
        if (annotation == null) {
            return null;
        }
        RocketMqConsumerBaseConfig consumerConfig = getConsumerConfig(annotation);
        rocketMqConsumerFactory.setConsumerConfig(consumerConfig);
        RocketMqMsgHandler rocketMqMsgHandler = null;
        try {
            rocketMqMsgHandler = (RocketMqMsgHandler) beanClass.newInstance();
        } catch (Exception e) {
            log.error("ConsumerGroup: {} dataHandler instantiation fail",consumerConfig.getConsumerGroup());
        }
        rocketMqConsumerFactory.setConsumer(consumerConfig,rocketMqMsgHandler);
        return rocketMqMsgHandler;
    }

    private RocketMqConsumerBaseConfig getConsumerConfig(RocketMqConsumerHandler annotation) {
        RocketMqConsumerBaseConfig rocketMqConsumerBaseConfig = new RocketMqConsumerBaseConfig();
        rocketMqConsumerBaseConfig.setNamesrvAddr(StringUtils.isEmpty(annotation.namesrvAddr()) ? consumerConfig.getNamesrvAddr() : annotation.namesrvAddr());
        rocketMqConsumerBaseConfig.setConsumerGroup(annotation.consumerGroup());
        rocketMqConsumerBaseConfig.setConsumeThreadMin(annotation.consumeThreadMin());
        rocketMqConsumerBaseConfig.setConsumeThreadMax(annotation.consumeThreadMax());
        rocketMqConsumerBaseConfig.setConsumeMessageBatchMaxSize(annotation.consumeMessageBatchMaxSize());
        rocketMqConsumerBaseConfig.setPullBatchSize(annotation.pullBatchSize());
        rocketMqConsumerBaseConfig.setConsumeTimeout(annotation.consumeTimeout());
        rocketMqConsumerBaseConfig.setMaxReconsumeTimes(annotation.maxReconsumeTimes());
        rocketMqConsumerBaseConfig.setOrderConsumer(annotation.isOrderConsumer());
        Set<RocketMqConsumerSubscribe> rocketMqConsumerSubscribes=new HashSet<>(4);
        for(int i=0;i<annotation.subscribes().length;i++){
            Assert.isTrue(rocketMqConsumerSubscribes.add(new RocketMqConsumerSubscribe(annotation.subscribes()[i].topic(),annotation.subscribes()[i].tag())),
                    "ConsumerGroup: " + rocketMqConsumerBaseConfig.getConsumerGroup() + " can't subscribe to two of the same topics");
        }
        rocketMqConsumerBaseConfig.setSubscribes(rocketMqConsumerSubscribes);
        return rocketMqConsumerBaseConfig;
    }


}
