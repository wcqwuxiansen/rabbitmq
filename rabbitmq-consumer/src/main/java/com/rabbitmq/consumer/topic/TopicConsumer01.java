package com.rabbitmq.consumer.topic;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class TopicConsumer01 {

    private static final String exchange_topic_name="exchange_topic_name";

    private static final String queue_topic_name1 ="queue_topic_name01";


    private static final String routing_key01 =  "info.*";
    private static final String routing_key02=  "warn.*";


    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.11.130");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("2021_vhost");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(exchange_topic_name,"topic");
        channel.queueDeclare(queue_topic_name1,true,false,false,null);


        channel.queueBind(queue_topic_name1,exchange_topic_name,routing_key01);
        channel.queueBind(queue_topic_name1,exchange_topic_name,routing_key02);

        channel.basicConsume(queue_topic_name1,true,new DefaultConsumer(channel){
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                //System.out.println(envelope.getDeliveryTag()+"============================================");
                System.out.println(new String(body,0,body.length,"UTF-8"));
            }
        });
        System.in.read();
    }
}
