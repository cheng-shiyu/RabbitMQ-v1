package com.chengshiyu.Demo06;

import com.chengshiyu.Utils.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author 程世玉
 * @data 2022/5/8.
 * 消费者2 用来接受死信队列里面的数据
 */
public class consumer2 {
    // 设置死信队列名称
    private static final String QUEUE_DEAD_VALUE = "dead-queue";
    // 设置死信交换机名称
    private static final String QUEUE_DEAD_EXCHANGE = "dead-exchange";
    // 设置死信路由名称
    private static final String QUEUE_DEAD_KEY = "dead-key";

    public static void main(String[] args) throws IOException {
        // 建立连接
        Channel channel = RabbitMQUtils.getChannel();

        /*设置死信交换机*/
        channel.exchangeDeclare(QUEUE_DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        channel.queueDeclare(QUEUE_DEAD_VALUE, false, false, false, null);


        // 进行队列绑定
        channel.queueBind(QUEUE_DEAD_VALUE, QUEUE_DEAD_EXCHANGE, QUEUE_DEAD_KEY);
        // 进行消费

        channel.basicConsume(QUEUE_DEAD_VALUE, true,
                (consumerTag, message) -> {
                    System.out.println("C1用户收到消息：" + new String(message.getBody()));
                },
                consumerTag -> {
                    System.out.println("C1用户取消接受消息");
                });
    }
}
