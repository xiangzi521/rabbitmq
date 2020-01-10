package com.mxz.rabbitMQ;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**rabbitMQ配置类
 *  这里只配置了Queue,其他的先用默认的配置
 * Created by Administrator on 2020/1/10.
 */
@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue helloQueue(){
        return new Queue("hello");
    }

}
