package cn.colins.rocketmqstarter.processor;

import java.util.HashSet;

import cn.colins.rocketmqstarter.consumer.config.RocketMqConsumerConfig;
import cn.colins.rocketmqstarter.consumer.config.RocketMqConsumerSubscribe;

import cn.colins.rocketmqstarter.annotation.RocketMqConsumerHandler;
import cn.colins.rocketmqstarter.annotation.RocketResource;
import cn.colins.rocketmqstarter.consumer.RocketMqMsgHandler;
import cn.colins.rocketmqstarter.consumer.config.RocketMqConsumerBaseConfig;
import cn.colins.rocketmqstarter.consumer.factory.RocketMqConsumerFactory;
import cn.colins.rocketmqstarter.producer.factory.RocketMqProducerFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.LinkedHashSet;
import java.util.Set;


public class RocketMqConsumerBeanPostProcessor implements InstantiationAwareBeanPostProcessor {

    private final RocketMqConsumerFactory rocketMqConsumerFactory;

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
        getConsumerConfig(annotation);
        return null;
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
        rocketMqConsumerBaseConfig.setSubscribes(new HashSet<RocketMqConsumerSubscribe>());
        rocketMqConsumerBaseConfig.setOrderConsumer(annotation.isOrderConsumer());
        return rocketMqConsumerBaseConfig;
    }


}
