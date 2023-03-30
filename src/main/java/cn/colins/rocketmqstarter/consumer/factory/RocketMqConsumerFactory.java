package cn.colins.rocketmqstarter.consumer.factory;

import cn.colins.rocketmqstarter.consumer.RocketMqConsumerDefaultService;
import cn.colins.rocketmqstarter.consumer.RocketMqConsumerOrderlyService;
import cn.colins.rocketmqstarter.consumer.RocketMqConsumerService;
import cn.colins.rocketmqstarter.consumer.RocketMqMsgHandler;
import cn.colins.rocketmqstarter.consumer.config.RocketMqConsumerBaseConfig;
import cn.colins.rocketmqstarter.consumer.config.RocketMqConsumerConfig;
import cn.colins.rocketmqstarter.producer.RocketMqProducerService;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description
 * @Author czl
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2023/3/29
 */
public class RocketMqConsumerFactory {

    private static ConcurrentHashMap<String, RocketMqConsumerService> CONSUMER_MAP = new ConcurrentHashMap(8);

    private static Set<RocketMqConsumerBaseConfig> CONSUMER_CONFIG = new HashSet<>(16);

    private final RocketMqConsumerConfig commonConsumerConfig;



    public RocketMqConsumerFactory(RocketMqConsumerConfig mqConsumerConfig) {
        this.commonConsumerConfig=mqConsumerConfig;
    }


    public void setConsumerConfig(RocketMqConsumerBaseConfig consumerBaseConfig){
        consumerBaseConfig.setNamesrvAddr(consumerBaseConfig.getNamesrvAddr() == null ? commonConsumerConfig.getNamesrvAddr() : consumerBaseConfig.getNamesrvAddr());
        Assert.isNull(consumerBaseConfig.getNamesrvAddr(),consumerBaseConfig.getConsumerGroup() + " : namesrvAddr is not null ");
        Assert.isTrue(CONSUMER_CONFIG.add(consumerBaseConfig), "There are two identical consumer groups : "+ consumerBaseConfig.getConsumerGroup());
    }

    public void setConsumer(RocketMqConsumerBaseConfig consumerBaseConfig, RocketMqMsgHandler mqMsgHandler){
        CONSUMER_MAP.put(consumerBaseConfig.getConsumerGroup(),consumerBaseConfig.getOrderConsumer() ?
                new RocketMqConsumerOrderlyService(consumerBaseConfig,mqMsgHandler):new RocketMqConsumerDefaultService(consumerBaseConfig,mqMsgHandler));
    }

    public RocketMqConsumerConfig getCommonConsumerConfig(){
        return commonConsumerConfig;
    }

    @PostConstruct
    public void start(){
        Iterator<RocketMqConsumerService> iterator = CONSUMER_MAP.values().iterator();
        while (iterator.hasNext()){
            iterator.next().startConsumer();
        }
    }

    @PreDestroy
    public void shutDown(){
        Iterator<RocketMqConsumerService> iterator = CONSUMER_MAP.values().iterator();
        while (iterator.hasNext()){
            iterator.next().shutDownConsumer();
        }
    }
}
