package com.chengshiyu.Demo04;

import com.chengshiyu.Utils.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;

/**
 * @author 程世玉
 * @data 2022/5/8.
 */
public class consumer1 {
    private static final String EXCHANGE_VALUE = "direct";

    public static void main(String[] args) throws IOException {
        Channel channel = RabbitMQUtils.getChannel();
        // 声明一个交换机
        channel.exchangeDeclare(EXCHANGE_VALUE, BuiltinExchangeType.DIRECT);

        // 创建一个临时队列
        String queueName = channel.queueDeclare().getQueue();

        // 绑定队列和交换机
        /*这个是大家都有的路由，看看共有的会不会报错，如果不报错，那就类似于广播了*/
        channel.queueBind(queueName,EXCHANGE_VALUE,"info");
        /*这个是你自己私有的*/
        channel.queueBind(queueName,EXCHANGE_VALUE,"debug");
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
