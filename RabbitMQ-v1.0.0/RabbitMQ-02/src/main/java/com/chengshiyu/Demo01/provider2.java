package com.chengshiyu.Demo01;

import com.chengshiyu.Utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author 程世玉
 * @data 2022/5/7".
 * 提供者
 */
public class provider2 {
    private static final String QUEUE_VALUE = "单点确定";
    private static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws IOException, InterruptedException {
        Channel channel = RabbitMQUtils.getChannel();
        boolean durable = true;
        if (channel != null) {
            channel.queueDeclare(QUEUE_VALUE,true,false,false,null);
        }

        // 开启发布确认功能
        if (channel != null) {
            channel.confirmSelect();
        }
        // 批量确定
        t2(channel);
    }

    /*同步确定*/
    public static void t2(Channel channel) throws IOException, InterruptedException {
        long begin = System.currentTimeMillis();
        StringBuffer sb = new StringBuffer();
        int index = 1;
        for (int i = 1; i <= MESSAGE_COUNT; i++){

            index++;

            sb.append(i).append("、");

            channel.basicPublish("",QUEUE_VALUE,null,sb.toString().getBytes(StandardCharsets.UTF_8));

            // 进行校验，100次校验一次
            if (index % 100 == 0){
                boolean flag = channel.waitForConfirms();
            }
        }

        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个批量确认消息,耗时" + (end - begin) +
                "ms");
    }



}
