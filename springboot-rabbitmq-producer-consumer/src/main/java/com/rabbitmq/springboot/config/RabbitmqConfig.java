package com.rabbitmq.springboot.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {

    @Bean("directExchange")
    public DirectExchange directExchange(){
        return new DirectExchange("springboot-directExchange",true,false);
    }

    @Bean("springbootQueue")
    public Queue springbootQueue(){
        return new Queue("springboot-queue",true,false,false);
    }

    @Bean
    public Binding bindQueueToExchange(@Qualifier("springbootQueue") Queue queue,@Qualifier("directExchange") DirectExchange directExchange){
        return BindingBuilder.bind(queue).to(directExchange).with("info");
    }
}
