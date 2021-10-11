package com.rabbitmq.springboot.consumer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class ConsumerService {
    @Autowired
    private RedisTemplate redisTemplate;

   @RabbitListener(queues = {"springboot-queue"})
    public void receiveMsg(Channel channel, String msg, Message message) throws IOException {
        Object messageId = message.getMessageProperties().getHeader("spring_returned_message_correlation");
        String messageIdStr = messageId.toString();
        String value = (String) redisTemplate.opsForValue().get(messageIdStr);
        if(!"1".equals(value)){

            try {
                //说明数据还没有插入到数据库
                //dao.insert();
                //redis记录下该消息已经被消费
                redisTemplate.opsForValue().set(messageIdStr,"1");
                //手动ACK失败
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            } catch (IOException e) {
                channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);
            }

        }else{
            System.out.println("收到消息==========================="+msg);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false); //手动ACk的两个参数  第一个为消息的index，第二个表示是否批量确认消息
        }

    }
}
