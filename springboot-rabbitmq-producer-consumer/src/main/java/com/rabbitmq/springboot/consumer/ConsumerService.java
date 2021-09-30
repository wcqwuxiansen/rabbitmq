package com.rabbitmq.springboot.consumer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class ConsumerService {

    @RabbitListener(queues = {"springboot-queue"})
    public void receiveMsg(Channel channel, String msg, Message message) throws IOException {
        System.out.println("收到消息==========================="+msg);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false); //手动ACk的两个参数  第一个为消息的index，第二个表示是否批量确认消息

    }
}
