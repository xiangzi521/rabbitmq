package com.mxz.helloRabbitMQ;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Administrator on 2020/1/10.
 */
public class Provider {
    static String Queue_NAME = "helloRabbit";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = null;
        Channel channel = null;
        try {
            // 创建连接和通道
            connection = factory.newConnection();
            channel = connection.createChannel();
            // 为通道申明队列
            channel.queueDeclare(Queue_NAME,false,false,false,null);
            // 发布消息
            String str = "hello world rabbit ";
            channel.basicPublish("",Queue_NAME,null,str.getBytes());
            System.out.println("provider send msg : "+str);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }finally {
            try {
                // 关闭连接
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
