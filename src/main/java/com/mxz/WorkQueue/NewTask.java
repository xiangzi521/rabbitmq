package com.mxz.WorkQueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**工作队列
 * Created by Administrator on 2020/1/10.
 */
public class NewTask {

    public static final String QUEUE_NAME = "TASK_QUEUE";

    public static final boolean DURABLE = false;

    public static final String[] msgs = {"sleep","task1","task2","task3","task4","task5"};


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
            channel.queueDeclare(QUEUE_NAME,DURABLE,false,false,null);
            // 发布消息

            for (int i = 0; i < msgs.length; i++) {
                /**
                 * "" 这里使用的是默认的交换机
                 */
                channel.basicPublish("",QUEUE_NAME,null,msgs[i].getBytes());
                /**
                 * 消息持久化写法
                 */
//                channel.basicPublish("",QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,msgs[i].getBytes());
                System.out.println("** new task ****:" + msgs[i]);
            }
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
