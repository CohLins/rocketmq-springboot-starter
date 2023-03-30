package cn.colins.rocketmqstarter.consumer;

import cn.colins.rocketmqstarter.consumer.config.RocketMqConsumerBaseConfig;
import cn.colins.rocketmqstarter.consumer.config.RocketMqConsumerSubscribe;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.Iterator;
import java.util.List;


@Slf4j
public class RocketMqConsumerOrderlyService implements RocketMqConsumerService {

    private final DefaultMQPushConsumer consumer;

    private final RocketMqConsumerBaseConfig consumerBaseConfig;

    private final RocketMqMsgHandler mqMsgHandler;

    public RocketMqConsumerOrderlyService(RocketMqConsumerBaseConfig consumerConfig, RocketMqMsgHandler mqMsgHandler) {
        this.consumerBaseConfig = consumerConfig;
        this.consumer = new DefaultMQPushConsumer(consumerConfig.getConsumerGroup());
        this.consumer.setNamesrvAddr(consumerConfig.getNamesrvAddr());
        this.consumer.setConsumeThreadMax(consumerConfig.getConsumeThreadMax());
        this.consumer.setConsumeThreadMin(consumerConfig.getConsumeThreadMin());
        this.consumer.setPullBatchSize(consumerConfig.getPullBatchSize());
        this.consumer.setConsumeMessageBatchMaxSize(consumerConfig.getConsumeMessageBatchMaxSize());
        this.consumer.setConsumeTimeout(consumerConfig.getConsumeTimeout());
        this.consumer.setMaxReconsumeTimes(consumerConfig.getMaxReconsumeTimes());
        this.mqMsgHandler = mqMsgHandler;
    }

    @Override
    public void startConsumer() {
        try {
            Iterator<RocketMqConsumerSubscribe> iterator = consumerBaseConfig.getSubscribes().iterator();
            while (iterator.hasNext()) {
                RocketMqConsumerSubscribe next = iterator.next();
                this.consumer.subscribe(next.getTopic(), next.getTag());
            }

            // 注册回调实现类来处理从broker拉取回来的消息
            consumer.registerMessageListener(new MessageListenerOrderly() {
                @Override
                public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                    for (MessageExt messageExt : msgs) {
                        try {
                            if (mqMsgHandler.beforeMsgHandler(messageExt)) {
                                if (!mqMsgHandler.msgDataHandler(messageExt)) {
                                    return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                                }
                                mqMsgHandler.afterMsgHandler(messageExt);
                            }
                        } catch (Exception e) {
                            if (mqMsgHandler.exceptionMsgHandler(messageExt)) {
                                return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                            }
                        }
                    }
                    return ConsumeOrderlyStatus.SUCCESS;
                }
            });
            // 启动消费者实例
            consumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
            log.error("[CustomerGroup:{} ] --> ERROR: {}", consumerBaseConfig.getConsumerGroup(), e.getMessage());
        }
        log.info("[CustomerGroup: {} ] --> START_SUCCESS ", consumerBaseConfig.getConsumerGroup());
    }


    @Override
    public void shutDownConsumer() {
        if (consumer != null) {
            consumer.shutdown();
        }
    }
}
