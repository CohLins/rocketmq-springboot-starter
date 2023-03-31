package cn.colins.rocketmqstarter.processor;

import cn.colins.rocketmqstarter.annotation.RocketResource;
import cn.colins.rocketmqstarter.annotation.RocketTransactionHandler;
import cn.colins.rocketmqstarter.consumer.RocketMqMsgHandler;
import cn.colins.rocketmqstarter.consumer.config.RocketMqConsumerBaseConfig;
import cn.colins.rocketmqstarter.producer.RocketMqProducer;
import cn.colins.rocketmqstarter.producer.RocketMqTransactionHandler;
import cn.colins.rocketmqstarter.producer.RocketMqTransactionProducer;
import cn.colins.rocketmqstarter.producer.factory.RocketMqProducerFactory;
import cn.colins.rocketmqstarter.producer.service.RocketMqProduceTransactionService;
import cn.colins.rocketmqstarter.producer.service.RocketMqProducerDefaultService;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.LinkedHashSet;
import java.util.Set;


public class RocketResourceAnnotationBeanPostProcessor implements InstantiationAwareBeanPostProcessor {
    private static final Set<Class<? extends Annotation>> resourceAnnotationTypes = new LinkedHashSet(4);

    static {
        resourceAnnotationTypes.add(RocketResource.class);
    }

    private RocketMqProducerFactory rocketMqProducerFactory;

    public RocketResourceAnnotationBeanPostProcessor(RocketMqProducerFactory rocketMqProducerFactory) {
        this.rocketMqProducerFactory = rocketMqProducerFactory;
    }


    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        rocketResourceAnnotationHandler(bean.getClass(), bean);
        return pvs;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (!RocketMqTransactionHandler.class.isAssignableFrom(bean.getClass())) {
            return bean;
        }
        RocketTransactionHandler annotation = bean.getClass().getAnnotation(RocketTransactionHandler.class);
        if (annotation == null) {
            return bean;
        }
        RocketMqProducerFactory.ROCKET_TRANSACTION_HANDLER.put(annotation.producerGroup(), (RocketMqTransactionHandler) bean);
        return bean;
    }

    private void rocketResourceAnnotationHandler(Class<?> aClass, Object bean) {
        ReflectionUtils.doWithLocalFields(aClass, (field) -> {
            if (field.isAnnotationPresent(RocketResource.class)) {
                if (Modifier.isStatic(field.getModifiers())) {
                    throw new IllegalStateException("@RocketResource annotation is not supported on static fields");
                }
                RocketResource rocketResource = AnnotationUtils.getAnnotation(field, RocketResource.class);
                String producerGroup = rocketResource.producerGroup();
                try {
                    if(field.getType().equals(RocketMqProducer.class)){
                        field.setAccessible(true);
                        RocketMqProducer rocketMqProducer = new RocketMqProducer((RocketMqProducerDefaultService) rocketMqProducerFactory.getRocketMqProducerService(producerGroup));
                        field.set(bean, rocketMqProducer);
                    }
                    if(field.getType().equals(RocketMqTransactionProducer.class)){
                        field.setAccessible(true);
                        RocketMqTransactionProducer rocketMqProducer = new RocketMqTransactionProducer((RocketMqProduceTransactionService) rocketMqProducerFactory.getRocketMqProducerService(producerGroup));
                        field.set(bean, rocketMqProducer);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
