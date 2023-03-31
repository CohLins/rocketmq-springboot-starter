package cn.colins.rocketmqstarter.producer.listener;

import cn.colins.rocketmqstarter.producer.RocketMqTransactionHandler;
import cn.colins.rocketmqstarter.producer.factory.RocketMqProducerFactory;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.util.Assert;


public class RocketMqTransactionListener implements TransactionListener {

    private String producerGroup;

    public RocketMqTransactionListener(String producerGroup) {
        this.producerGroup = producerGroup;
    }

    @Override
    public LocalTransactionState executeLocalTransaction(Message message, Object o) {
        return checkTransactionHandler().executeLocalTransaction(message,o);
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
        return checkTransactionHandler().checkLocalTransaction(messageExt);
    }

    public RocketMqTransactionHandler checkTransactionHandler() {
        RocketMqTransactionHandler rocketMqTransactionHandler = RocketMqProducerFactory.ROCKET_TRANSACTION_HANDLER.get(producerGroup);
        Assert.notNull(rocketMqTransactionHandler, "RocketMq Producer: " + producerGroup + " not find transaction handler");
        return rocketMqTransactionHandler;
    }
}
