package com.mxz.WorkQueue;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Administrator on 2020/1/10.
 */
public class Work2 {

    public static void main(String[] args) {
        System.out.println("*** Work2 ***");
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = null;
        Channel channel = null;
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();

            channel.queueDeclare(NewTask.QUEUE_NAME, NewTask.DURABLE, false, false, null);

            // 回调生成消费者
            Channel finalChannel = channel;
            Consumer consumer = new DefaultConsumer(finalChannel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String msg = new String(body, "UTF-8");
                    try {
                        doWork(msg);
                    }finally {
                        finalChannel.basicAck(envelope.getDeliveryTag(),false);
                    }
                }
            };
            /**
             * 接受消息，消费消息
             * Provider.Queue_NAME：队列名称
             * true:自动确认默认为true
             * consumer:callback消费者
             */
            boolean autoAck = false;
            /**
             * 公平分发
             */
            channel.basicQos(1);
            channel.basicConsume(NewTask.QUEUE_NAME, autoAck, consumer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    private static void doWork(String msg) {
        try {
            System.out.println("**** deal task begin :" + msg);

            //假装task比较耗时，通过sleep（）来模拟需要消耗的时间
            if ("sleep".equals(msg)) {
                Thread.sleep(1000 * 60);
            } else {
                Thread.sleep(1000);
            }

            System.out.println("**** deal task finish :" + msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

