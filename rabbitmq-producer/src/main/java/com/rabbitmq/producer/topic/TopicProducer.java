package com.rabbitmq.producer.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class TopicProducer {

    private static final String exchange_topic_name="exchange_topic_name";

    private static final String queue_topic_name1 ="queue_topic_name01";
    private static final String queue_topic_name2 ="queue_topic_name02";

    private static final String routing_key01 =  "info.*";
    private static final String routing_key02=  "warn.*";
    private static final String routing_key03 =  "error.#";

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
        channel.queueDeclare(queue_topic_name2,true,false,false,null);

        channel.queueBind(queue_topic_name1,exchange_topic_name,routing_key01);
        channel.queueBind(queue_topic_name1,exchange_topic_name,routing_key02);
        channel.queueBind(queue_topic_name2,exchange_topic_name,routing_key03);

        for(int i=0;i<5;i++){
            channel.basicPublish(exchange_topic_name,"info.info",null,"info info".getBytes("utf-8"));
        }

        for(int i=0;i<5;i++){
            channel.basicPublish(exchange_topic_name,"warn.warn",null,"warn warn".getBytes("utf-8"));
        }

        for(int i=0;i<5;i++){
            channel.basicPublish(exchange_topic_name,"error.error.error",null,"error error eeror".getBytes("utf-8"));
        }
    }
}
