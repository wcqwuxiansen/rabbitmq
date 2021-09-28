package com.rabbitmq.consumer.publishsubscribe;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class PublishSubscribeConsumer02 {
    private static final String exchang_fanout_name ="exchange_fanout_name";
    private static final String queue_name2 = "queue_name02";
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.11.130");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("2021_vhost");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(exchang_fanout_name,"fanout");
        channel.queueDeclare(queue_name2,true,false,false,null);

        //发布订阅模式队列的routingkey应该设置成空串
        channel.queueBind(queue_name2,exchang_fanout_name,"");
        channel.basicConsume(queue_name2,true,new DefaultConsumer(channel){
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                System.out.println(envelope.getDeliveryTag()+"============================================");
                System.out.println(new String(body,0,body.length,"UTF-8"));
            }
        });
        System.in.read();
    }
}
