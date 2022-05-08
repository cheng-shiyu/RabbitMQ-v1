package com.chengshiyu.Demo01;

import com.chengshiyu.Utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author 程世玉
 * @data 2022/5/7".
 * 提供者
 */
public class provider3 {
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

    /*异步确认*/
    public static void t2(Channel channel) throws IOException, InterruptedException {
        long begin = System.currentTimeMillis();
        StringBuffer sb = new StringBuffer();
        int index = 1;
        /**
         * 线程安全有序的一个哈希表，适用于高并发的情况
         * 1.轻松的将序号与消息进行关联
         * 2.轻松批量删除条目 只要给到序列号
         * 3.支持并发访问
         */
        ConcurrentSkipListMap<Long, String> outstandingConfirms = new
                ConcurrentSkipListMap<>();
        /**
         * 确认收到消息的一个回调,多线程，新的一个线程
         * 1.消息序列号
         * 2.true 可以确认小于等于当前序列号的消息
         * false 确认当前序列号消息
         */
        ConfirmCallback ackCallback = (sequenceNumber, multiple) -> {
            if (multiple) {
                //返回的是小于等于当前序列号的未确认消息 是一个 map
                ConcurrentNavigableMap<Long, String> confirmed =
                        outstandingConfirms.headMap(sequenceNumber, true);
                //清除该部分未确认消息
                confirmed.clear();
            }else{
                //只清除当前序列号的消息
                outstandingConfirms.remove(sequenceNumber);
            }
        };

        /**
         * 没有收到消息，没有ack回调
         */
        ConfirmCallback nackCallback = (sequenceNumber, multiple) -> {
            String message = outstandingConfirms.get(sequenceNumber);
            System.out.println("发布的消息"+message+"未被确认，序列号"+sequenceNumber);
        };

        /**
         * 添加一个异步确认的监听器
         * 1.确认收到消息的回调
         * 2.未收到消息的回调
         */
        channel.addConfirmListener(ackCallback, null);

        for (int i = 1; i <= MESSAGE_COUNT; i++){
            sb.append(i).append("、");
            // 将其添加如我们公共的map中
            outstandingConfirms.put(channel.getNextPublishSeqNo(),sb.toString());
            channel.basicPublish("",QUEUE_VALUE,null,sb.toString().getBytes(StandardCharsets.UTF_8));
        }

        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个批量确认消息,耗时" + (end - begin) +
                "ms");
    }



}
