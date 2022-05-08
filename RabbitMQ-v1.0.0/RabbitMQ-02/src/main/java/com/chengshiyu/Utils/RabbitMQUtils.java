package com.chengshiyu.Utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author 程世玉
 * @data 2022/5/7.
 *
 */
public class RabbitMQUtils {
    public static Channel getChannel(){
        ConnectionFactory factory = new ConnectionFactory();
        factory.setPassword("123456");
        factory.setUsername("admin");
        factory.setHost("124.223.86.21");
        factory.setPort(5672);
        Connection connection = null;
        try {
            connection = factory.newConnection();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
        try {
            assert connection != null;
            return connection.createChannel();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
