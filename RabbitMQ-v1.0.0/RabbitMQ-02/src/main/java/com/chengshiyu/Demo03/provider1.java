package com.chengshiyu.Demo03;

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
public class provider1 {
    private static final String EXCHANGE_VALUE = "logs";

    public static void main(String[] args) throws IOException {
        Channel channel = RabbitMQUtils.getChannel();

        // 声明一个交换机 交换机的名字，交换机的类型
        channel.exchangeDeclare(EXCHANGE_VALUE, BuiltinExchangeType.FANOUT);


        // 开始传送数据
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入您要发送的消息：");
        while (scanner.hasNext()){
            String line = scanner.nextLine();
            /**
             * 交换机的名字
             * 路由
             * 参数
             * 信息
             */
            channel.basicPublish(EXCHANGE_VALUE,"",null,line.getBytes(StandardCharsets.UTF_8));
            System.out.print("请输入您要发送的消息：");
        }


    }
}
