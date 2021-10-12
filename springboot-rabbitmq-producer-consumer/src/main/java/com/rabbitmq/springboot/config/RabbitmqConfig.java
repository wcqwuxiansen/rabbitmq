package com.rabbitmq.springboot.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitmqConfig {

    @Bean("directExchange")//业务交换机
    public DirectExchange directExchange(){
        return new DirectExchange("springboot-directExchange",true,false);
    }

    @Bean("springbootQueue") //业务队列
    public Queue springbootQueue(){
        Map map = new HashMap();
        map.put("x-dead-letter-exchange","springboot-deadDirectExchange"); //业务队列绑定死信交换机
        map.put("x-dead-letter-routing-key","dead-info");//业务队列绑定死信交换机的路由key
        map.put("x-message-ttl",5000);//延时队列的属性   使用延时队列时   业务队列不能连接消费者  延时时间到  信息转到死信队列  由死信队列消费消息
        return new Queue("springboot-queue",true,false,false,map);
    }

    @Bean //业务队列绑定到业务交换机
    public Binding bindQueueToExchange(@Qualifier("springbootQueue") Queue queue,@Qualifier("directExchange") DirectExchange directExchange){
        return BindingBuilder.bind(queue).to(directExchange).with("info");
    }

    @Bean("deadDirectExchange") //死信交换机
    public DirectExchange deadDirectExchange(){
        return new DirectExchange("springboot-deadDirectExchange",true,false);
    }

    @Bean("deadSpringbootQueue")  //死信队列
    public Queue deadSpringbootQueue(){
        return new Queue("dead-springboot-queue",true,false,false);
    }

    @Bean //死信队列绑定到死信交换机
    public Binding bindDeadQueueToDeadExchange( @Qualifier("deadSpringbootQueue") Queue deadSpringbootQueue,@Qualifier("deadDirectExchange") DirectExchange deadDirectExchange){
        return BindingBuilder.bind(deadDirectExchange).to(deadDirectExchange).with("dead-info");
    }
}
