package com.chengshiyu.three;

import com.chengshiyu.utils.RabbitMQUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author 程世玉
 * @data 2022/5/6.
 * 消费者
 */
public class worker1 {
    static final String QUEUE_VALUE = "hello";
    public static void main(String[] args) throws IOException {
        Channel channel = RabbitMQUtils.getChannel();
        System.out.println("C2线程进入等待ing......");

        DeliverCallback deliverCallback = (consumerTag,message)->{
//            System.out.println("收到消息message：" + message);
            System.out.println("收到消息message：" + new String(message.getBody()));
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            /**
             * 1.消息标记 tag
             * 2.是否批量应答未应答消息
             *
             */
//            System.out.println("message.getProperties() = " + message.getProperties());
//            System.out.println("message.getEnvelope().getDeliveryTag() = " + message.getEnvelope().getDeliveryTag());
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);

        };
        CancelCallback cancelCallback = consumerTag->{
            System.out.println(consumerTag + "消费者取消消费接口回调逻辑");
        };

        /**
         * 1、消息的key
         * 2、是否采用自动应答
         * 3、消费者未成功消费的回调
         * 4、取消消费的回调函数
         */
        boolean autoAck = false;
        channel.basicConsume(QUEUE_VALUE,autoAck,deliverCallback,cancelCallback);
    }
}
