package com.chengshiyu.Demo02;

import com.chengshiyu.Utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.Queue;

/**
 * @author 程世玉
 * @data 2022/5/8.
 */
public class provider1 {
    public static void main(String[] args) throws IOException {
        Channel channel = RabbitMQUtils.getChannel();
        String queue = channel.queueDeclare().getQueue();
        System.out.println("新队列的名字 ： " + queue );
    }
}
