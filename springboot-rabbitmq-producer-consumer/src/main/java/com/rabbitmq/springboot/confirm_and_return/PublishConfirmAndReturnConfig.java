package com.rabbitmq.springboot.confirm_and_return;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import sun.rmi.runtime.Log;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class PublishConfirmAndReturnConfig implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnsCallback{
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct //init阶段
    public void initMethod(){
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setMandatory(true);  //当交换机无法将消息路由到队列时 设置为true就是将消息返回给生产者，设置为false就是直接丢弃消息
        rabbitTemplate.setReturnsCallback(this);

    }

    /**
     *
     * @param correlationData 消息的相关性数据
     * @param ack  true表示生产者将消息发送到了交换机
     * @param cause
     */
    @Override
    public void confirm(@Nullable CorrelationData correlationData, boolean ack, @Nullable String cause) {
        if(ack){
            String id = correlationData.getId();
            log.info("消息到达交换机 id:{}",id);
        }else{
            System.out.println("消息未到达交换机");
        }

    }

    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        System.out.println("消息未到达Queue回调此方法");
    }
}
