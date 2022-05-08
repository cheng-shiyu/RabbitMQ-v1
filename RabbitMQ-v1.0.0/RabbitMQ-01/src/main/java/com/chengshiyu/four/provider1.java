package com.chengshiyu.four;

import com.chengshiyu.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author 程世玉
 * @data 2022/5/7.
 * 生产者
 */
public class provider1 {
    static final String QUEUE_VALUE = "ack";

    public static void main(String[] args) throws IOException {
        Channel channel = RabbitMQUtils.getChannel();

        /**
         * 1、队列的名字
         * 2、是不持久化， true ，表示持久化，会存盘，服务器重启仍然存在，false，非持久化
         * 3、是否排他的，true，排他。如果一个队列声明为排他队列，该队列公对首次声明它的连接可见，并在连接断开时自动删除，
         *     如果你想创建一个只有自己可见的队列，即不允许其它用户访问，RabbitMQ允许你将一个Queue声明成为排他性的（Exclusive Queue）。
         *     特点：
         *       3.1、只对首次声明它的连接（Connection）可见，如果试图在一个不同的连接中重新声明或访问（如publish，consume）该排他性队列，会得到资源被锁定的错误：
         *       3.2、会在其连接断开的时候自动删除。RabbitMQ会自动删除这个队列，而不管这个队列是否被声明成持久性的（Durable =true)。 也就是说即使客户端程序将一个排他性的队列声明成了Durable的，只要调用了连接的Close方法或者客户端程序退出了，RabbitMQ都会删除这个队列。注意这里是连接断开的时候，而不是通道断开。这个其实前一点保持一致，只区别连接而非通道。
         * 4、是否自动删除,true，自动删除，自动删除的前提：至少有一个消息者连接到这个队列，之后所有与这个队列连接的消息都断开时，才会自动删除，，备注：生产者客户端创建这个队列，或者没有消息者客户端连接这个队列时，不会自动删除这个队列
         * 5、其他参数
         */
        /*是否开启持久化*/
        boolean durable = true;

        /*创建一个队列*/
        channel.queueDelete(QUEUE_VALUE); // 提前删除一下
        channel.queueDeclare(QUEUE_VALUE, durable, false, false, null);



        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入要发送的消息：");
        while (scanner.hasNext()) {
            String message = scanner.nextLine();
            /**
             * 1、exchange 交换机
             * 2、路由，哪一个队列
             * 3、其他配置信息
             * 4、发送的消息，字节流
             */
            /*x消息不进行持久化*/
//            channel.basicPublish("", QUEUE_VALUE, null, message.getBytes(StandardCharsets.UTF_8));
            /*消息进行持久化操作*/
            channel.basicPublish("", QUEUE_VALUE, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes(StandardCharsets.UTF_8));
            System.out.print("请输入要发送的消息：");
        }
    }
}
