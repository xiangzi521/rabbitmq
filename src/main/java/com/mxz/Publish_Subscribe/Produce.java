package com.mxz.Publish_Subscribe;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Administrator on 2020/1/10.
 */
public class Produce {

    public static String EXCHANGE_NAME = "exchange";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = null;
        Channel channel = null;

        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            // 声明exchange 和 exchange的类型
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

            String msg = "hello rabbitmq, this is publish/subscribe mode";
            /** 发送消息到指定exchange
             * s1 代表的是队列。指定为空，有exchange根据情况判断需要发送到那些队列
             */
            channel.basicPublish(EXCHANGE_NAME,"", null,msg.getBytes());
            System.out.println("produce send msg :" + msg);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }finally {
            try {
                channel.close();
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }

    }

}
