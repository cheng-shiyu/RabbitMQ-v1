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
public class provider1 {
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
        // 单点登录功能实现
        t1(channel);
    }

    /*单点确定*/
    public static void t1(Channel channel) throws IOException, InterruptedException {
        long begin = System.currentTimeMillis();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < MESSAGE_COUNT; i++){
            stringBuffer.append(i).append("、");
            channel.basicPublish("",QUEUE_VALUE,null,stringBuffer.toString().getBytes(StandardCharsets.UTF_8));
            //服务端返回 false 或超时时间内未返回，生产者可以消息重发
            boolean flag = channel.waitForConfirms();
        }
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个单独确认消息,耗时" + (end - begin) +
                "ms");
    }



}
