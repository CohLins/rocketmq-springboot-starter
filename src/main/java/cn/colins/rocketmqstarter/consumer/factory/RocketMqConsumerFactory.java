package cn.colins.rocketmqstarter.consumer.factory;

import cn.colins.rocketmqstarter.consumer.service.RocketMqConsumerDefaultService;
import cn.colins.rocketmqstarter.consumer.service.RocketMqConsumerOrderlyService;
import cn.colins.rocketmqstarter.consumer.RocketMqConsumerService;
import cn.colins.rocketmqstarter.consumer.RocketMqMsgHandler;
import cn.colins.rocketmqstarter.consumer.config.RocketMqConsumerBaseConfig;
import cn.colins.rocketmqstarter.consumer.config.RocketMqConsumerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

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
@Slf4j
public class RocketMqConsumerFactory {

    public static ConcurrentHashMap<String, RocketMqConsumerService> CONSUMER_MAP = new ConcurrentHashMap(4);

    private static Set<RocketMqConsumerBaseConfig> CONSUMER_CONFIG = new HashSet<>(4);

    private final RocketMqConsumerConfig commonConsumerConfig;



    public RocketMqConsumerFactory(RocketMqConsumerConfig mqConsumerConfig) {
        this.commonConsumerConfig=mqConsumerConfig;
    }


    public void setConsumerConfig(RocketMqConsumerBaseConfig consumerBaseConfig){
        consumerBaseConfig.setNamesrvAddr(consumerBaseConfig.getNamesrvAddr() == null ? commonConsumerConfig.getNamesrvAddr() : consumerBaseConfig.getNamesrvAddr());
        Assert.notNull(consumerBaseConfig.getNamesrvAddr(),consumerBaseConfig.getConsumerGroup() + " : namesrvAddr is not null ");
        Assert.isTrue(CONSUMER_CONFIG.add(consumerBaseConfig), "There are two identical consumer groups : "+ consumerBaseConfig.getConsumerGroup());
    }

    public void setConsumer(RocketMqConsumerBaseConfig consumerBaseConfig, RocketMqMsgHandler mqMsgHandler){
        CONSUMER_MAP.put(consumerBaseConfig.getConsumerGroup(),consumerBaseConfig.getOrderConsumer() ?
                new RocketMqConsumerOrderlyService(consumerBaseConfig,mqMsgHandler):new RocketMqConsumerDefaultService(consumerBaseConfig,mqMsgHandler));
    }

    public RocketMqConsumerConfig getCommonConsumerConfig(){
        return commonConsumerConfig;
    }



    @PreDestroy
    public void shutDown(){
        Iterator<RocketMqConsumerService> iterator = CONSUMER_MAP.values().iterator();
        while (iterator.hasNext()){
            iterator.next().shutDownConsumer();
        }
    }
}
