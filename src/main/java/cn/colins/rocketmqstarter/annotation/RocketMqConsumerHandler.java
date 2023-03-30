package cn.colins.rocketmqstarter.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RocketMqConsumerHandler {
    /**
     * namesrvAddr 地址 不填则以配置文件为准
     **/
    String namesrvAddr() default "";
    /**
     * 消费组名称
     **/
    String consumerGroup();

    /**
     * 订阅关系 topic 、tag
     **/
    RocketMqSubscribes[] subscribes();

    /**
     * 消费开启最小线程数  默认20
     **/
    int consumeThreadMin() default 20;

    /**
     * 消费开启最大线程数  默认20
     **/
    int consumeThreadMax() default 20;
    /**
     * 批量消费最大条数 默认1条
     **/
    int consumeMessageBatchMaxSize() default 1;
    /**
     * 一次从broker拉取消息条数 默认32条
     **/
    int pullBatchSize() default 32;
    /**
     * 消费超时时间 单位：分钟 默认15分钟
     **/
    int consumeTimeout() default 15;
    /**
     * 消费失败最大重试次数 默认16次
     **/
    int maxReconsumeTimes() default 16;
}
