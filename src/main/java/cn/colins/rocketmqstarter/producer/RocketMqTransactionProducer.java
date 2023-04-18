package cn.colins.rocketmqstarter.producer;

import cn.colins.rocketmqstarter.producer.service.RocketMqProduceTransactionService;
import cn.colins.rocketmqstarter.producer.service.RocketMqProducerDefaultService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.io.UnsupportedEncodingException;
import java.util.List;


@Slf4j
public class RocketMqTransactionProducer extends RocketMqProducer{

    private String producerGroup;

    private TransactionMQProducer producer;

    public RocketMqTransactionProducer(RocketMqProduceTransactionService defaultService) {
        super(defaultService);
        this.producer = defaultService.getMqProducer();
        this.producerGroup = defaultService.getProducerGroup();
    }

    public void transactionProducerSend(String topic,String tog,String msg,Object arg){
        try {
            Message message = new Message(topic, tog, msg.getBytes(RemotingHelper.DEFAULT_CHARSET));
            TransactionSendResult transactionSendResult = producer.sendMessageInTransaction(message, arg);
            log.info("[ProducerGroup:{}] TOPIC: {}--> TRANSACTION_MSG:{}", producerGroup, topic, msg);
        } catch (UnsupportedEncodingException | MQClientException e) {
            log.info("[ProducerGroup:{}] TOPIC: {}--> TRANSACTION_SEND_ERROR({})", producerGroup, topic, e.getMessage());
        }
    }
}
