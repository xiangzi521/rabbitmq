package com.mxz.WorkQueue;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Administrator on 2020/1/10.
 */
public class Work {

    public static void main(String[] args) {
        System.out.println("*** Work ***");
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = null;
        Channel channel = null;
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();

            /**
             * 当消费者在消费的时候，rabbitMQ突然挂了，然后所有的消息都被清空，这是服务端丢失。
             * 解决服务端消息丢失：
             *      队列持久化，在声明队列的时候，把第二个参数设置为true  NewTask.DURABLE = true
             *      消息持久化，在发送消息的时候，把第三个参数这是为2       在生产者端进行设置===》消息持久化写法
             */
            channel.queueDeclare(NewTask.QUEUE_NAME, NewTask.DURABLE, false, false, null);

            // 回调生成消费者
            Channel finalChannel = channel;
            Consumer consumer = new DefaultConsumer(finalChannel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String msg = new String(body, "UTF-8");
                    /**
                     * 为了解决客户端消息丢失，当消费者处理完消息之后，会发送一个确认消息给rabbitMQ，消息处理完了，你可以删掉它了
                     */
                    try {
                        doWork(msg);
                    }finally {
                        // 消息处理完后在通过basicAck()进行消息确认
                        finalChannel.basicAck(envelope.getDeliveryTag(),false);
                    }
                }
            };
            /**
             * 接受消息，消费消息
             * Provider.Queue_NAME：队列名称
             * true:自动确认默认为true
             *      自动确认为true，每次rabbitMQ向消费者发送消息之后，会自动确认消息，如果这个时候会立即从内存种删除，如果工作者挂了，那将会丢失它正在处理和
         *      和未处理的所有的工作，而且这些工作还不能在交由其他工作者处理，这种丢失属于客户端丢失。
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

