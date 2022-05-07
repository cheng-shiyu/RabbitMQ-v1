package com.chengshiyu.customer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author 程世玉
 * @data 2022/5/5.
 * 消费者1
 */
public class Consumer1 {
    private final static String QUEUE_NAME = "hello";
    public static void main(String[] args) {
        /*创建连接工厂*/
        ConnectionFactory connectionFactory = new ConnectionFactory();
        /*设置地址，端口号，账号，密码等*/
        connectionFactory.setPassword("123456");
        connectionFactory.setUsername("admin");
        connectionFactory.setHost("124.223.86.21");
        connectionFactory.setPort(5672);

        /*获取连接对象*/
        try {
            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();
            System.out.println("开始等待信息");
            /*声明接受消息*/
            DeliverCallback deliverCallback = (consumnerTag,delivery)->{
                String message = new String(delivery.getBody());
                System.out.println("message = " + message);
            };

            /*取消消息时的回调*/
            CancelCallback cancelCallback = (consumerTag)->{
                System.out.println("消费被中断了！！！");
            };

            /*
              消费者消费消息
              1.消费哪个队列
              2.消费成功之后是否要自动应答 true 代表自动应答 false 手动应答
              3.消费者未成功消费的回调
              4、消费者取消消费的回调
             */
            channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }


    }
}
