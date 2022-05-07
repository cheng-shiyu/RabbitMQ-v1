package com.chengshiyu.provider;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author 程世玉
 * @data 2022/5/5.
 * 生产者一
 */
public class Producer1 {
    /*ctrl + shift + u 变换大小写*/
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) {
        /*1、创建连接工厂*/
        ConnectionFactory connectionFactory = new ConnectionFactory();
        /*1.1、设置路径*/
        connectionFactory.setHost("124.223.86.21");
        /*1.2、设置端口号*/
//        connectionFactory.setPort(5672);
        /*1.3、设置账号密码*/
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("123456");
        /*2、创建连接对象*/
        try {
            /*2.1、Channel实现了自动关闭，我们不需要手动关闭*/
            Connection  connection = connectionFactory.newConnection();

            /*2.2、创建一个通道*/
            Channel channel = connection.createChannel();
            /*2.3、通道要连接队列，但是队列没有创建出来，我们需要创建出来*/
            /**
             * 生成一个队列
             * 1.队列名称
             * 2.队列里面的消息是否持久化 默认消息存储在内存中
             * 3.该队列是否只供一个消费者进行消费 是否进行共享 true 可以多个消费者消费
             * 4.是否自动删除 最后一个消费者端开连接以后 该队列是否自动删除 true 自动删除
             * 5.其他参数
             */
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);


            /*3、发送信息*/
            /*3.1、编写要发送的信息*/
            String message = "hello world!!!";

            /**
             * 发送一个消息
             * 1.发送到那个交换机
             * 2.路由的 key 是哪个
             * 3.其他的参数信息
             * 4.发送消息的消息体
             */
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("发送信息完毕");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
