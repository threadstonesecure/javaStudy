package dlt.study.kafka.spring;

import dlt.utils.spring.SpringContextUtils;


public class ConsumerDemo {

    public static void main(String[] args) throws  Exception {
        String[] paths = new String[] { "springtest/kafkaConsumer.xml" };
        SpringContextUtils.init(paths);
        System.out.println("消费者启动成功！");
        Thread.currentThread().join();
    }
}
