package com.mxz.rabbitMQ;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**消息消费者
 * Created by Administrator on 2020/1/10.
 */
@Component
@RabbitListener(queues = "hello")
public class Receiver {

    @RabbitListener
    public void receiver(String str){
        System.out.println("Receiver says : [" + str+ "]" );
    }

}
