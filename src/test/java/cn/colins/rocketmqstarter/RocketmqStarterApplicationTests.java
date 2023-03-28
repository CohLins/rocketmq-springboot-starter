package cn.colins.rocketmqstarter;

import cn.colins.rocketmqstarter.annotation.RocketResource;
import cn.colins.rocketmqstarter.producer.RocketMqProducerService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RocketmqStarterApplicationTests {

    @RocketResource(producerGroup = "TEST_PRODUCER_GROUP1")
    private RocketMqProducerService rocketMqProducerService1;

    @RocketResource(producerGroup = "TEST_PRODUCER_GROUP2")
    private RocketMqProducerService rocketMqProducerService2;

    @Test
    void contextLoads() {
        System.out.println(rocketMqProducerService1.producerGroup);
        System.out.println(rocketMqProducerService2.producerGroup);
    }

}
