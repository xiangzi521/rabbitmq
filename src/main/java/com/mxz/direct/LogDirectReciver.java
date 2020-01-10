package com.mxz.direct;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Administrator on 2020/1/10.
 */
public class LogDirectReciver {
    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = null;
        Channel channel = null;

        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            //声明交换机名字和类型
            channel.exchangeDeclare(LogDirectSender.EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            // 创建随机名字队列
            String queue = channel.queueDeclare().getQueue();
            //绑定交换机和队列
            String[] bindingKeys = {"error","info","debug"};
//            String[] bindingKeys = {"error"};
            for (int i = 0; i < bindingKeys.length; i++) {
                channel.queueBind(queue,LogDirectSender.EXCHANGE_NAME,bindingKeys[i]);
                System.out.println("==》 LogDirectReciver keep alive ,waiting for " + bindingKeys[i]);
            }

            Consumer consumer = new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String s = new String(body, "UTF-8");
                    System.out.println("==》 LogDirectReciver "+ " get message :[" + s + "]");
                }
            };

            channel.basicConsume(queue,true,consumer);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }
}
