package com.mxz.rabbitMQ;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**消息生产者
 * Created by Administrator on 2020/1/10.
 */
@Component
public class Sender {

    @Autowired
    AmqpTemplate rabbitmqTemplate;

    public void send(){
        String content = "Send says : " + " hello , I'm coming";
        System.out.println(content);
        rabbitmqTemplate.convertAndSend("hello",content);
    }

}
