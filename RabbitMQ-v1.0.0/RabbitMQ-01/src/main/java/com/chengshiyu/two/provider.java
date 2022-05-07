package com.chengshiyu.two;

import com.chengshiyu.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author 程世玉
 * @data 2022/5/6.
 * 发消息
 */
public class provider {
    static final  String QUEUE_VALUE = "hello";

    public static void main(String[] args) throws IOException {
        Channel channel = RabbitMQUtils.getChannel();

        /**
         * 生成一个队列
         * 1.队列名称
         * 2.队列里面的消息是否持久化 默认消息存储在内存中
         * 3.该队列是否只供一个消费者进行消费 是否进行共享 true 可以多个消费者消费
         * 4.是否自动删除 最后一个消费者端开连接以后 该队列是否自动删除 true 自动删除
         * 5.其他参数
         */
        channel.queueDeclare(QUEUE_VALUE,false,false,false,null);
        // 从控制台接受代码
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()){
            String s = scanner.nextLine();
            /**
             * 发送一个消息
             * 1.发送到那个交换机
             * 2.路由的 key 是哪个
             * 3.其他的参数信息
             * 4.发送消息的消息体
             */
            channel.basicPublish("",QUEUE_VALUE,null,s.getBytes(StandardCharsets.UTF_8));
        }
    }
}
