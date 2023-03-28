package cn.colins.rocketmqstarter.producer;

import cn.colins.rocketmqstarter.producer.config.RocketMqProducerConfig;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description
 * @Author czl
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2023/3/28
 */
public class RocketMqFactory {

    private static ConcurrentHashMap<String,RocketMqProducerService> ROCKET_PRODUCER=new ConcurrentHashMap(8);


    public RocketMqFactory(RocketMqProducerConfig rocketMqProducerConfig) {
        Assert.notNull(rocketMqProducerConfig.getDefaultMQProducerConfigs(),"rocketMqProducerConfig is not null");
        rocketMqProducerConfig.getDefaultMQProducerConfigs().forEach(item->{
            ROCKET_PRODUCER.put(item.getProducerGroup(),new RocketMqProducerService(item));
        });
    }

    public RocketMqProducerService getRocketMqProducerService(String producerGroup){
        RocketMqProducerService rocketMqProducerService = ROCKET_PRODUCER.get(producerGroup);
        Assert.notNull(rocketMqProducerService,producerGroup + "is not exist,setting attribute fail");
        return rocketMqProducerService;
    }

    public RocketMqProducerService getRocketMqProducerService(){
        Assert.notNull(ROCKET_PRODUCER, " RocketMqProducerService is not find,setting attribute fail");
        return ROCKET_PRODUCER.values().iterator().next();
    }

    @PostConstruct
    public void start(){
        Iterator<RocketMqProducerService> iterator = ROCKET_PRODUCER.values().iterator();
        while (iterator.hasNext()){
            iterator.next().startProducer();
        }
    }

    @PreDestroy
    public void shutDown(){
        Iterator<RocketMqProducerService> iterator = ROCKET_PRODUCER.values().iterator();
        while (iterator.hasNext()){
            iterator.next().shutDownProducer();
        }
    }
}
