package cn.colins.rocketmqstarter.producer;

public interface RocketMqProducer {

    void oneWayProducerSend(String topic, String tag, String msg);

    void asyncProducerSend(String topic, String tag, String msg);

    boolean syncProducerSend(String topic, String tags, String msg, int delayTimeLevel);
}
