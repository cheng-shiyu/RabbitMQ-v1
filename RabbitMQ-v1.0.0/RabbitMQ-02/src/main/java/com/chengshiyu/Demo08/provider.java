package com.chengshiyu.Demo08;

import com.chengshiyu.Utils.RabbitMQUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author 程世玉
 * @data 2022/5/8.
 */
public class provider {
    // 设置普通队列名称
    private static final String QUEUE_VALUE = "queue";
    // 设置普通队列交换机名称
    private static final String QUEUE_EXCHANGE = "exchange";
    // 设置普通队列路由名称
    private static final String QUEUE_KEY = "key";

    public static void main(String[] args) throws IOException {
        Channel channel = RabbitMQUtils.getChannel();

        channel.exchangeDeclare(QUEUE_EXCHANGE, BuiltinExchangeType.DIRECT);

        //设置消息的 TTL 时间 先设置着，没有用，因为消费者那边设置过了
        AMQP.BasicProperties properties = new
                AMQP.BasicProperties().builder().expiration("10000").build();


        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 100; i++){
            sb.append(i).append(" ");
            channel.basicPublish(QUEUE_EXCHANGE,QUEUE_KEY,null,sb.toString().getBytes(StandardCharsets.UTF_8));
        }
    }
}
