package cn.colins.rocketmqstarter.producer;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

public interface RocketMqTransactionHandler {

    LocalTransactionState executeLocalTransaction(Message msg, Object arg);

    LocalTransactionState checkLocalTransaction(MessageExt msg);

}
