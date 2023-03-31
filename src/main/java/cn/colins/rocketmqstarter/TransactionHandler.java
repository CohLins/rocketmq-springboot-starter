package cn.colins.rocketmqstarter;

import cn.colins.rocketmqstarter.annotation.RocketTransactionHandler;
import cn.colins.rocketmqstarter.producer.RocketMqTransactionHandler;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * @Description
 * @Author czl
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2023/3/31
 */
@Slf4j
@RocketTransactionHandler(producerGroup = "TEST_PRODUCER_GROUP1")
public class TransactionHandler implements RocketMqTransactionHandler {
    @Resource
    private BeanTest beanTest;

    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        log.info("Bean test : {}",beanTest.string);
        log.info("msg: {} ,arg: {} ", JSON.toJSON(new String(msg.getBody(), StandardCharsets.UTF_8)),arg);
        return LocalTransactionState.ROLLBACK_MESSAGE;
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        log.info("失败回查，然后成功");
        return LocalTransactionState.COMMIT_MESSAGE;
    }
}
