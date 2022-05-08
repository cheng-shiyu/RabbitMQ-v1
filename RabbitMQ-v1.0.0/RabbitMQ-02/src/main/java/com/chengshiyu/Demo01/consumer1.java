package com.chengshiyu.Demo01;

import com.chengshiyu.Utils.RabbitMQUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;

/**
 * @author 程世玉
 * @data 2022/5/7.
 * 消费者1
 */
public class consumer1 {
    private static final String QUEUE_VALUE = "单点确定";
    public static void main(String[] args) throws IOException {
        /*获取到channel*/
        Channel channel = RabbitMQUtils.getChannel();

        DeliverCallback deliverCallback = (consumerTag,message)->{
            System.out.println("C1收到消息：" + new String(message.getBody()));
            // 进行响应
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        };

        CancelCallback cancelCallback = consumerTag ->{
            System.out.println("C1取消消息接受");
        };
        boolean autoAck = false;

        channel.basicConsume(QUEUE_VALUE, autoAck,deliverCallback,cancelCallback);
    }
}
