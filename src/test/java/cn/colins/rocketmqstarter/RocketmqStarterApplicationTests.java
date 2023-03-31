package cn.colins.rocketmqstarter;

import cn.colins.rocketmqstarter.annotation.RocketResource;
import cn.colins.rocketmqstarter.producer.RocketMqProducer;
import cn.colins.rocketmqstarter.producer.RocketMqProducerService;
import cn.colins.rocketmqstarter.producer.RocketMqTransactionProducer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RocketmqStarterApplicationTests {

    @RocketResource(producerGroup = "TEST_PRODUCER_GROUP2")
    private RocketMqProducer mqProducer;

    @RocketResource(producerGroup = "TEST_PRODUCER_GROUP1")
    private RocketMqTransactionProducer transactionProducer;

    @Test
    void contextLoads() {
        mqProducer.syncProducerSend("ES_RESUME_SYNC_ZL_TOPIC_CZL","ES_RESUME_SYNC_ZL_TOPIC_CZL");
        transactionProducer.syncProducerSend("ES_RESUME_SYNC_ZL_TOPIC_CZL","ES_RESUME_SYNC_ZL_TOPIC_CZL");
        transactionProducer.transactionProducerSend("ES_RESUME_SYNC_ZL_TOPIC_CZL","*","transactionProducer",null);
        while (true){}
    }

}
