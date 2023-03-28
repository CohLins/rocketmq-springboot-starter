package cn.colins.rocketmqstarter.consumer;

import org.apache.rocketmq.common.message.MessageExt;

public interface RocketMqConsumer {

    boolean msgDataHandler(MessageExt msg);

}
