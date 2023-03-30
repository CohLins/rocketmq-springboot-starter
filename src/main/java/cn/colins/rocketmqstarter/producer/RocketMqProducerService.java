package cn.colins.rocketmqstarter.producer;

import cn.colins.rocketmqstarter.producer.config.DefaultMQProducerConfig;
import cn.colins.rocketmqstarter.producer.config.RocketMqProducerConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;


@Slf4j
public class RocketMqProducerService extends AbstractRocketMqProducer {

    public RocketMqProducerService(DefaultMQProducerConfig rocketMqProducerConfig) {
        Assert.notNull(rocketMqProducerConfig.getProducerGroup(), "ProducerGroup setting is not null");
        Assert.notNull(rocketMqProducerConfig.getNamesrvAddr(), "NamesrvAddr setting is not null");
        producerGroup = rocketMqProducerConfig.getProducerGroup();
        producer = new DefaultMQProducer(rocketMqProducerConfig.getProducerGroup());
        producer.setNamesrvAddr(rocketMqProducerConfig.getNamesrvAddr());
        producer.setRetryTimesWhenSendFailed(rocketMqProducerConfig.getRetryTimesWhenSendFailed());
        producer.setSendMsgTimeout(rocketMqProducerConfig.getSendMsgTimeout());
    }

    @Override
    public void startProducer(){
        try {
            producer.start();
            log.info("RocketMqProducerGroup:{} start success",producerGroup);
        } catch (Exception e) {
            log.info("RocketMqProducerGroup:{} start error:{}",producerGroup,e.getMessage());
        }
    }
    @Override
    public void shutDownProducer(){
        if (producer != null) {
            producer.shutdown();
        }
    }
}
