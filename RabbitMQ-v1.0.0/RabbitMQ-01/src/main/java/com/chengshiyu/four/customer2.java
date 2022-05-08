package com.chengshiyu.four;

import com.chengshiyu.utils.RabbitMQUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author 程世玉
 * @data 2022/5/7.
 * 消费者
 */
public class customer2 {
    static final String QUEUE_VALUE = "ack";
    public static void main(String[] args) throws IOException {
        Channel channel = RabbitMQUtils.getChannel();
        System.out.println("C1服务器性能差，处理数据慢，正在等待数据ing......");

        DeliverCallback deliverCallback = (consumerTag,message)->{
            try {
                /*休眠20s*/
                TimeUnit.SECONDS.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String m = new String(message.getBody());
            System.out.println("C2收到消息：" + m);

            /*设置手动应答*/
            // 前面是消息的标识，后面是设置是否批量处理，很显然，我们是不批量处理的
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        };

        CancelCallback cancelCallback = (consumerTag) ->{
            System.out.println("取消消息回调函数");
        };

        // 设置非公平锁
        int prefectchCount = 1;
        channel.basicQos(prefectchCount);

        boolean autoAck = false;
        channel.basicConsume(QUEUE_VALUE,autoAck,deliverCallback,cancelCallback);
    }
}
