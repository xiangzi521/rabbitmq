package com.mxz.helloRabbitMQ;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Administrator on 2020/1/10.
 */
public class HelloConsumer {

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = null;
        Channel channel = null;
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();

            channel.queueDeclare(Provider.Queue_NAME, false, false, false, null);
            System.out.println("**** keep alive ,waiting for messages, and then deal them");

            // 回调生成消费者
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String msg = new String(body, "UTF-8");
                    System.out.println("*********** HelloConsumer" + " get message :[" + msg + "]");
                }
            };
            /**
             * 接受消息，消费消息
             * Provider.Queue_NAME：队列名称
             * true:自动确认默认为true
             * consumer:callback消费者
             */
            channel.basicConsume(Provider.Queue_NAME, true, consumer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

}
