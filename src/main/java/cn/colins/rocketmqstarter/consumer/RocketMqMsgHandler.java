package cn.colins.rocketmqstarter.consumer;

import org.apache.rocketmq.common.message.MessageExt;

public interface RocketMqMsgHandler {

    boolean beforeMsgHandler(MessageExt msg);

    boolean msgDataHandler(MessageExt msg);

    void afterMsgHandler(MessageExt msg);

    boolean exceptionMsgHandler(MessageExt msg, Exception e);

    void finallyMsgHandler(MessageExt msg);
}
