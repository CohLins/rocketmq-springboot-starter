package cn.colins.rocketmqstarter.producer.service;

import cn.colins.rocketmqstarter.producer.RocketMqProducerService;
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
public class RocketMqProducerDefaultService implements RocketMqProducerService {

    private String producerGroup;

    private DefaultMQProducer producer;
    

    public RocketMqProducerDefaultService(DefaultMQProducerConfig rocketMqProducerConfig) {
        Assert.notNull(rocketMqProducerConfig.getProducerGroup(), "ProducerGroup setting is not null");
        Assert.notNull(rocketMqProducerConfig.getNamesrvAddr(), "NamesrvAddr setting is not null");
        this.producerGroup = rocketMqProducerConfig.getProducerGroup();
        this.producer = new DefaultMQProducer(rocketMqProducerConfig.getProducerGroup());
        this.producer.setNamesrvAddr(rocketMqProducerConfig.getNamesrvAddr());
        this.producer.setRetryTimesWhenSendFailed(rocketMqProducerConfig.getRetryTimesWhenSendFailed());
        this.producer.setSendMsgTimeout(rocketMqProducerConfig.getSendMsgTimeout());
    }

    public String getProducerGroup(){
        return this.producerGroup;
    }

    public DefaultMQProducer getMqProducer(){
        return this.producer;
    }


    @Override
    public void startProducer() {
        try {
            this.producer.start();
            log.info("RocketMqProducerGroup:{} start success", this.producerGroup);
        } catch (Exception e) {
            log.info("RocketMqProducerGroup:{} start error:{}", this.producerGroup, e.getMessage());
        }
    }

    @Override
    public void shutDownProducer() {
        if (this.producer != null) {
            this.producer.shutdown();
        }
    }
}
