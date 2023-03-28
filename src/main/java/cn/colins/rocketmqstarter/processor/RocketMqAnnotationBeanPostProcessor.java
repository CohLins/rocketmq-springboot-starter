package cn.colins.rocketmqstarter.processor;

import cn.colins.rocketmqstarter.RocketmqStarterApplication;
import cn.colins.rocketmqstarter.annotation.RocketResource;
import cn.colins.rocketmqstarter.producer.RocketMqFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashSet;
import java.util.Set;


public class RocketMqAnnotationBeanPostProcessor implements InstantiationAwareBeanPostProcessor {
    private final static String DEFAULT = "DEFAULT";
    private static final Set<Class<? extends Annotation>> resourceAnnotationTypes = new LinkedHashSet(4);

    static {
        resourceAnnotationTypes.add(RocketResource.class);
    }

    private RocketMqFactory rocketMqFactory;

    public RocketMqAnnotationBeanPostProcessor(RocketMqFactory rocketMqFactory) {
        this.rocketMqFactory = rocketMqFactory;
    }


    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        rocketResourceAnnotationHandler(bean.getClass(), bean);
        return pvs;
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
                    field.setAccessible(true);
                    field.set(bean, producerGroup.equals(DEFAULT) ?
                            rocketMqFactory.getRocketMqProducerService() : rocketMqFactory.getRocketMqProducerService(producerGroup));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
