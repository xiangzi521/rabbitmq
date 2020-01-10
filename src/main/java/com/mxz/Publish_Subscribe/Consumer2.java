package com.mxz.Publish_Subscribe;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Administrator on 2020/1/10.
 */
public class Consumer2 {
    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = null;
        Channel channel = null;
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            //声明交换机和类型
            channel.exchangeDeclare(Produce.EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
            // 创建随机名字的队列
            String queue = channel.queueDeclare().getQueue();
            //绑定队列和交换机
            channel.queueBind(queue, Produce.EXCHANGE_NAME, "");
            System.out.println(" ==》 Consumer2 keep alive ,waiting for messages, and then deal them");
            //回调生成消费者进行监听
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                    String s = new String(body, "UTF-8");
                    System.out.println("====》 Consumer2 " + "get msg :[" + s + "]");
                }
            };
            //消费消息
            channel.basicConsume(queue,true,consumer);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }
}
