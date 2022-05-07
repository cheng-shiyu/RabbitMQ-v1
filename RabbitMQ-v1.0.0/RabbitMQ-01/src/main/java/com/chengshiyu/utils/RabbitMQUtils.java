package com.chengshiyu.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author 程世玉
 * @data 2022/5/6.
 */
public class RabbitMQUtils {
    public static Channel getChannel(){
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setPort(5672);
        connectionFactory.setHost("124.223.86.21");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("123456");
        Connection connection = null;
        try {
            connection = connectionFactory.newConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        Channel channel = null;
        try {
            channel = connection.createChannel();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return channel;
    }
}
