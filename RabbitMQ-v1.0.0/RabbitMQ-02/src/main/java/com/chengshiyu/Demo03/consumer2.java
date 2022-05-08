package com.chengshiyu.Demo03;

import com.chengshiyu.Utils.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author 程世玉
 * @data 2022/5/8.
 * 消费者2
 */
public class consumer2 {
    private static final String EXCHANGE_VALUE = "logs";
    public static void main(String[] args) throws IOException {
        Channel channel = RabbitMQUtils.getChannel();

        /*设置交换机的模式*/
        channel.exchangeDeclare(EXCHANGE_VALUE, BuiltinExchangeType.FANOUT);

        // 采用随机队列
        String queueName = channel.queueDeclare().getQueue();

        // 设置消费未成功回调
        DeliverCallback deliverCallback = (consumerTag,message)->{
            String m = new String(message.getBody());
            File file = new File("/rabbitmq_info.txt");
            if (file.exists()){
                file.createNewFile();
            }
            FileUtils.writeStringToFile(file,m,"UTF-8",true);
            System.out.println("写入数据成功");
        };

        CancelCallback cancelCallback = (consumerTag)->{
            System.out.println("C1取消消息");
        };
        // 绑定，将exchange绑定到我们队列中
        /**
         * 队列名字
         * 交换机名字
         * 绑定路由
         */
        channel.queueBind(queueName,EXCHANGE_VALUE,"");
        System.out.println("C1等待接受消息ing，将接收到的消息存储在磁盘......");
        channel.basicConsume(queueName,true,deliverCallback,cancelCallback);
    }
}
