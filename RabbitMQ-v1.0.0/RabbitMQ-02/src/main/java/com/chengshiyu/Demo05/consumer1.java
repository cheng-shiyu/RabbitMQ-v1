package com.chengshiyu.Demo05;

import com.chengshiyu.Utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;

/**
 * @author 程世玉
 * @data 2022/5/8.
 * 消费者
 */
public class consumer1 {
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException {
        Channel channel = RabbitMQUtils.getChannel();

        // 创建exchange
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");

        // 创建队列
        String queueName="Q1";
        channel.queueDeclare(queueName, false, false, false, null);

        // 绑定交换机和队列
        channel.queueBind(queueName, EXCHANGE_NAME, "*.orange.*");

        System.out.println("等待接收消息.....");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" 接收队列 :"+queueName+" 绑定键:"+delivery.getEnvelope().getRoutingKey()+",消息:"+message);
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
        };
        channel.basicConsume(queueName, false, deliverCallback, consumerTag -> {
        });

    }
}
