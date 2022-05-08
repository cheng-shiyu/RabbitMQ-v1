package com.chengshiyu.Demo04;

import com.chengshiyu.Utils.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;

/**
 * @author 程世玉
 * @data 2022/5/8.
 */
public class consumer2 {
    private static final String EXCHANGE_VALUE = "direct";

    public static void main(String[] args) throws IOException {
        Channel channel = RabbitMQUtils.getChannel();
        // 声明一个交换机
        channel.exchangeDeclare(EXCHANGE_VALUE, BuiltinExchangeType.DIRECT);

        // 创建一个临时队列
        String queueName = channel.queueDeclare().getQueue();

        // 绑定队列和交换机
        channel.queueBind(queueName,EXCHANGE_VALUE,"info");
        channel.queueBind(queueName,EXCHANGE_VALUE,"error");
        System.out.println(queueName + "等待消息ing.......");
        channel.basicConsume(queueName, false,
                (consumerTage, message) -> {
                    System.out.println(queueName + "收到消息：" + new String(message.getBody()));
                },
                consumerTag -> {
                    System.out.println(queueName + "取消消息");
                });
    }
}
