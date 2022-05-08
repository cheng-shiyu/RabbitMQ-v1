package com.chengshiyu.Demo04;

import com.chengshiyu.Utils.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author 程世玉
 * @data 2022/5/8.
 * 生产者
 */
public class provide1 {
    private static final String EXCHANGE_VALUE = "direct";

    public static void main(String[] args) throws IOException {
        Channel channel = RabbitMQUtils.getChannel();

        channel.exchangeDeclare(EXCHANGE_VALUE, BuiltinExchangeType.DIRECT);
        System.out.print("info：请输入您要发送的消息：");

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String s = scanner.nextLine();
            channel.basicPublish(EXCHANGE_VALUE,"info",null,s.getBytes(StandardCharsets.UTF_8));
            System.out.print("info：请输入您要发送的消息：");
        }
    }
}
