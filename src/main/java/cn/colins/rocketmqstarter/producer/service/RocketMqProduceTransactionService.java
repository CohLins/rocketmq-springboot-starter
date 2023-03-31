package cn.colins.rocketmqstarter.producer.service;

import cn.colins.rocketmqstarter.producer.RocketMqProducerService;
import cn.colins.rocketmqstarter.producer.RocketMqTransactionHandler;
import cn.colins.rocketmqstarter.producer.config.DefaultMQProducerConfig;
import cn.colins.rocketmqstarter.producer.factory.RocketMqProducerFactory;
import cn.colins.rocketmqstarter.producer.listener.RocketMqTransactionListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.util.Assert;


@Slf4j
public class RocketMqProduceTransactionService implements RocketMqProducerService {

    private String producerGroup;

    private TransactionMQProducer producer;


    public RocketMqProduceTransactionService(DefaultMQProducerConfig rocketMqProducerConfig) {
        Assert.notNull(rocketMqProducerConfig.getProducerGroup(), "ProducerGroup setting is not null");
        Assert.notNull(rocketMqProducerConfig.getNamesrvAddr(), "NamesrvAddr setting is not null");
        this.producerGroup = rocketMqProducerConfig.getProducerGroup();
        this.producer = new TransactionMQProducer(rocketMqProducerConfig.getProducerGroup());
        this.producer.setNamesrvAddr(rocketMqProducerConfig.getNamesrvAddr());
        this.producer.setRetryTimesWhenSendFailed(rocketMqProducerConfig.getRetryTimesWhenSendFailed());
        this.producer.setSendMsgTimeout(rocketMqProducerConfig.getSendMsgTimeout());
        this.producer.setTransactionListener(new RocketMqTransactionListener(producerGroup));
    }

    public String getProducerGroup(){
        return this.producerGroup;
    }

    public TransactionMQProducer getMqProducer(){
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
