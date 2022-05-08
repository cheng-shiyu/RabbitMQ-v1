package com.chengshiyu.Demo08;

import com.chengshiyu.Utils.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author 程世玉
 * @data 2022/5/8.
 * 消费者1 (启动之后关闭该消费者 模拟其接收不到消息)
 */
public class consumer1 {
    // 设置死信队列名称
    private static final String QUEUE_DEAD_VALUE = "dead-queue";
    // 设置死信交换机名称
    private static final String QUEUE_DEAD_EXCHANGE = "dead-exchange";
    // 设置死信路由名称
    private static final String QUEUE_DEAD_KEY = "dead-key";
    // 设置普通队列名称
    private static final String QUEUE_VALUE = "queue";
    // 设置普通队列交换机名称
    private static final String QUEUE_EXCHANGE = "exchange";
    // 设置普通队列路由名称
    private static final String QUEUE_KEY = "key";

    public static void main(String[] args) throws IOException {
        // 建立连接
        Channel channel = RabbitMQUtils.getChannel();

        // 创建死信交换机和普通交换机
        /*设置普通交换机*/
        channel.exchangeDeclare(QUEUE_EXCHANGE, BuiltinExchangeType.DIRECT);
        /*设置死信交换机*/
        channel.exchangeDeclare(QUEUE_DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        // 设置普通交换机里面死信配置
        channel.queueDelete(QUEUE_VALUE);
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("x-dead-letter-exchange", QUEUE_DEAD_EXCHANGE);  // 设置死信交换机
        map.put("x-dead-letter-routing-key", QUEUE_DEAD_KEY);  // 设置死信路由
//        map.put("x-dead-letter-ttl", 10000);  // 设置未10s
//        map.put("x-message-ttl", 10000);  // 设置未10s  上面这种不行！！！！！
        map.put("x-max-length", 8);  // 设置正常队列最大长度限制
        // 创建俩队列
        channel.queueDeclare(QUEUE_VALUE, false, false, false, map);
        channel.queueDeclare(QUEUE_DEAD_VALUE, false, false, false, null);


        // 进行队列绑定
        channel.queueBind(QUEUE_VALUE, QUEUE_EXCHANGE, QUEUE_KEY);
        channel.queueBind(QUEUE_DEAD_VALUE, QUEUE_DEAD_EXCHANGE, QUEUE_DEAD_KEY);
        // 进行消费
        channel.basicConsume(QUEUE_VALUE, true,
                (consumerTag, message) -> {
                    System.out.println("C1用户收到消息：" + new String(message.getBody()));
                },
                consumerTag -> {
                    System.out.println("C1用户取消接受消息");
                });
    }
}
