package cn.colins.rocketmqstarter.producer;

import cn.colins.rocketmqstarter.producer.service.RocketMqProduceTransactionService;
import cn.colins.rocketmqstarter.producer.service.RocketMqProducerDefaultService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.List;


@Slf4j
public class RocketMqProducer {

    private String producerGroup;

    private DefaultMQProducer producer;

    public RocketMqProducer(RocketMqProduceTransactionService defaultService){
        this.producer = defaultService.getMqProducer();
        this.producerGroup = defaultService.getProducerGroup();
    }

    public RocketMqProducer(RocketMqProducerDefaultService defaultService) {
        this.producer = defaultService.getMqProducer();
        this.producerGroup = defaultService.getProducerGroup();
    }

    /**
     * @return boolean 成功 true  失败 false
     * 场景：可靠的同步传输应用于广泛的场景，如重要通知消息、短信通知、短信营销系统等
     * @Author czl
     * @Description 简单同步消息发送
     * @Date 2021/11/3 15:05
     * @Param [topic, msg] 主题、消息
     **/
    public boolean syncProducerSend(String topic, String msg) {
        return syncProducerSend(topic, topic, msg);
    }

    /**
     * @return boolean 成功 true  失败 false
     * 场景：可靠的同步传输应用于广泛的场景，如重要通知消息、短信通知、短信营销系统等
     * @Author czl
     * @Description 带标签的同步消息发送
     * @Date 2021/11/3 15:05
     * @Param [topic, tag, msg] 主题、标签、消息
     **/
    public boolean syncProducerSend(String topic, String tag, String msg) {
        return syncProducerSend(topic, tag, msg, 0);
    }

    public boolean syncProducerOrderSend(String topic, String tags, String msg, String orderKey) {
        try {
            Message message = new Message(topic, tags, msg.getBytes(RemotingHelper.DEFAULT_CHARSET));
            SendResult result = producer.send(message, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> messageQueues, Message message, Object o) {
                    int index = Math.abs(orderKey.hashCode()) % messageQueues.size();
                    return messageQueues.get(index);
                }
            }, orderKey);
            log.info("[ProducerGroup:{}] TOPIC: {}--> msgID({}) :RESULT({})", producerGroup, topic, result.getMsgId(), result.getSendStatus());
            return result.getSendStatus() == SendStatus.SEND_OK;
        } catch (Exception e) {
            log.info("[ProducerGroup:{}] TOPIC: {}-->SYNC_SEND_ERROR({})", producerGroup, topic, e.getMessage());
        }
        return false;
    }


    /**
     * @return boolean 成功 true  失败 false
     * 场景：可靠的同步传输应用于广泛的场景，如重要通知消息、短信通知、短信营销系统等
     * @Author czl
     * @Description 同步消息生产者
     * @Date 2021/11/3 14:22
     * @Param [topic, tags, msg, DelayTimeLevel] 主题，标签，消息，延迟等级（参考以下1-18级对应时间）
     * @DelayTimeLevel : "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h"
     **/

    public boolean syncProducerSend(String topic, String tags, String msg, int delayTimeLevel) {
        try {
            Message message = new Message(topic, tags, msg.getBytes(RemotingHelper.DEFAULT_CHARSET));
            if (delayTimeLevel != 0) {
                message.setDelayTimeLevel(delayTimeLevel);
            }
            SendResult result = producer.send(message);
            log.info("[ProducerGroup:{}] TOPIC: {}--> msgID({}) :RESULT({})", producerGroup, topic, result.getMsgId(), result.getSendStatus());
            return result.getSendStatus() == SendStatus.SEND_OK;
        } catch (Exception e) {
            log.info("[ProducerGroup:{}] TOPIC: {}-->SYNC_SEND_ERROR({})", producerGroup, topic, e.getMessage());
        }
        return false;
    }


    /**
     * @return void 场景：异步传输一般用于响应时间敏感的业务场景
     * @Author czl
     * @Description 简单异步消息发送
     * @Date 2021/11/3 15:25
     * @Param [topic, msg] 主题、信息
     **/
    public void asyncProducerSend(String topic, String msg) {
        asyncProducerSend(topic, topic, msg);
    }

    /**
     * @return void 场景：异步传输一般用于响应时间敏感的业务场景
     * @Author czl
     * @Description 异步消息生产者
     * @Date 2021/11/3 15:23
     * @Param [topic, tag, msg] 主题、标签、信息
     **/
    public void asyncProducerSend(String topic, String tag, String msg) {
        try {
            Message message = new Message(topic, tag, msg.getBytes(RemotingHelper.DEFAULT_CHARSET));
            producer.send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    log.info("[ProducerGroup:{}] TOPIC: {}--> msgID({}) :RESULT({})", producerGroup, topic, sendResult.getMsgId(), sendResult.getSendStatus());
                }

                @Override
                public void onException(Throwable throwable) {
                    log.info("[ProducerGroup:{}] TOPIC: {}-->ASYNC_BACK_ERROR({})", producerGroup, topic, throwable.getMessage());
                }
            });
        } catch (Exception e) {
            log.error("[ProducerGroup:" + producerGroup + "] TOPIC:" + topic + "--> ASYNC_SEND_ERROR: " + e.getMessage());
        }
    }

    /**
     * @Description 异步发送顺序消息
     * @Param [topic, tag, msg]
     * @return void
     **/
    public void asyncProducerOrderSend(String topic, String tag, String msg ,String orderKey) {
        try {
            Message message = new Message(topic, tag, msg.getBytes(RemotingHelper.DEFAULT_CHARSET));
            producer.send(message, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> messageQueues, Message message, Object o) {
                    int index = Math.abs(orderKey.hashCode()) % messageQueues.size();
                    return messageQueues.get(index);
                }
            }, orderKey,new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    log.info("[ProducerGroup:{}] TOPIC: {}--> msgID({}) :RESULT({})", producerGroup, topic, sendResult.getMsgId(), sendResult.getSendStatus());
                }

                @Override
                public void onException(Throwable throwable) {
                    log.error("[ProducerGroup:{}] TOPIC: {}-->ASYNC_BACK_ERROR({})", producerGroup, topic, throwable.getMessage());
                }
            });
        } catch (Exception e) {
            log.error("[ProducerGroup:" + producerGroup + "] TOPIC:" + topic + "--> ASYNC_SEND_ERROR: " + e.getMessage());
        }
    }

    /**
     * @return void
     * 这种方式主要用在不特别关心发送结果的场景，例如日志发送。
     * @Author czl
     * @Description 简单单向信息发送
     * @Date 2021/11/3 15:30
     * @Param [topic, msg] 主题、消息
     **/
    public void oneWayProducerSend(String topic, String msg) {
        oneWayProducerSend(topic, topic, msg);
    }

    /**
     * @return void
     * 这种方式主要用在不特别关心发送结果的场景，例如日志发送。
     * @Author czl
     * @Description 单向信息生产者
     * @Date 2021/11/3 15:30
     * @Param [topic, tag, msg] 主题、标签、消息
     **/
    public void oneWayProducerSend(String topic, String tag, String msg) {
        try {
            Message message = new Message(topic, tag, msg.getBytes(RemotingHelper.DEFAULT_CHARSET));
            // 发送单向消息，没有任何返回结果
            producer.sendOneway(message);
            log.info("[ProducerGroup:{}] TOPIC: {}--> ONEWAY_MSG:{}", producerGroup, topic, msg);
        } catch (Exception e) {
            log.info("[ProducerGroup:{}] TOPIC: {}--> ONEWAY_SEND_ERROR({})", producerGroup, topic, e.getMessage());
        }
    }

}
