package cn.colins.rocketmqstarter;

import cn.colins.rocketmqstarter.annotation.RocketResource;
import cn.colins.rocketmqstarter.producer.service.RocketMqProducerDefaultService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class RocketmqStarterApplication{


    public static void main(String[] args) {
        SpringApplication.run(RocketmqStarterApplication.class, args);
    }

}
