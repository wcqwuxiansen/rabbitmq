package com.rabbitmq.producer.routing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RoutingProducer {
    private static final String queue_name1 = "queue_routing_name01";
    private static final String queue_name2 = "queue_routing_name02";

    private static final String exchange_routing_name = "exchang_routing";

    private static final  String routing_key_01 = "info";
    private static final  String routing_key_02 = "warn";
    private static final  String routing_key_03 = "error";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.11.130");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("2021_vhost");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(queue_name1,true,false,false,null);
        channel.queueDeclare(queue_name2,true,false,false,null);
        channel.exchangeDeclare(exchange_routing_name,"direct");

        channel.queueBind(queue_name1,exchange_routing_name,routing_key_01);
        channel.queueBind(queue_name1,exchange_routing_name,routing_key_02);
        channel.queueBind(queue_name2,exchange_routing_name,routing_key_03);

        for(int i=0;i<5;i++){
            channel.basicPublish(exchange_routing_name,routing_key_01,null,"info".getBytes("UTF-8"));
        }


        for(int i=0;i<5;i++){
            channel.basicPublish(exchange_routing_name,routing_key_02,null,"warn".getBytes("UTF-8"));
        }


        for(int i=0;i<5;i++){
            channel.basicPublish(exchange_routing_name,routing_key_03,null,"error".getBytes("UTF-8"));
        }

    }
}
