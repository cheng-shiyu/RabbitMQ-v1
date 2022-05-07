package com.chengshiyu.two;

import com.chengshiyu.utils.RabbitMQUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;

/**
 * @author 程世玉
 * @data 2022/5/6.
 * 消费者
 */
public class Worker01 {
    static final String QUEUE_VALUE = "hello";
    public static void main(String[] args) throws IOException {
        Channel channel = RabbitMQUtils.getChannel();

        DeliverCallback deliverCallback=(consumerTag, delivery)->{
            String receivedMessage = new String(delivery.getBody());
            System.out.println("接收到消息:"+receivedMessage);
        };
        CancelCallback cancelCallback=(consumerTag)-> {
            System.out.println(consumerTag + "消费者取消消费接口回调逻辑");
        };

         /*
              消费者消费消息
              1.消费哪个队列
              2.消费成功之后是否要自动应答 true 代表自动应答 false 手动应答
              3.消费者未成功消费的回调
              4、消费者取消消费的回调
             */
        System.out.println("C3工作线程等待......");
        channel.basicConsume(QUEUE_VALUE,false,deliverCallback,cancelCallback);
    }
}
