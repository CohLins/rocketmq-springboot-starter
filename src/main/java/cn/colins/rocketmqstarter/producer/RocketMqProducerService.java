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
        super.producerGroup = rocketMqProducerConfig.getProducerGroup();
        super.producer = new DefaultMQProducer(rocketMqProducerConfig.getProducerGroup());
        super.producer.setNamesrvAddr(rocketMqProducerConfig.getNamesrvAddr());
        super.producer.setRetryTimesWhenSendFailed(rocketMqProducerConfig.getRetryTimesWhenSendFailed());
        super.producer.setSendMsgTimeout(rocketMqProducerConfig.getSendMsgTimeout());
    }

    @Override
    public void startProducer() {
        try {
            super.producer.start();
            log.info("RocketMqProducerGroup:{} start success", super.producerGroup);
        } catch (Exception e) {
            log.info("RocketMqProducerGroup:{} start error:{}", super.producerGroup, e.getMessage());
        }
    }

    @Override
    public void shutDownProducer() {
        if (super.producer != null) {
            super.producer.shutdown();
        }
    }
}
