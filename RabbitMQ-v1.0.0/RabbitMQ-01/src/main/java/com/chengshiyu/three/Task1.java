package com.chengshiyu.three;

import com.chengshiyu.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author 程世玉
 * @data 2022/5/6.
 * 消费的提供者
 */
public class Task1 {
    static final String QUEUE_VALUE = "hello";
    public static void main(String[] args) throws IOException {
        /*获取连接*/
        Channel channel = RabbitMQUtils.getChannel();

        /**
         * 1、生成一个队列，队列的名称
         * 2、是否默认存储到内存中
         * 3、是否只对一个消费者进行提供服务，是否共享，true是可以多个消费者消费，说人话，就是是否只能被消费一次，true则是可以被消费多次
         * 4、是否自动删除  最后一个消费者端开连接以后 该队列是否自动删除 true 自动删除
         * 5、其他参数
         *
         */
        channel.queueDeclare(QUEUE_VALUE,false,false,false,null);


        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            System.out.print("请输入消息：");
            String message = scanner.nextLine();
            /**
             * 1、发送到哪一个交换机
             * 2、路由的key是哪一个
             * 3、其他的参数信息
             * 4、发送消息的消息体，
             */
            channel.basicPublish("",QUEUE_VALUE,false,null,message.getBytes(StandardCharsets.UTF_8));
        }

    }
}
