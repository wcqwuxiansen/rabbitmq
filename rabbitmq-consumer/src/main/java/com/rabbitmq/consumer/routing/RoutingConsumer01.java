package com.rabbitmq.consumer.routing;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RoutingConsumer01 {
    private static final String queue_name1 = "queue_routing_name01";

    private static final String exchange_routing_name = "exchang_routing";

    private static final  String routing_key_01 = "info";
    private static final  String routing_key_02 = "warn";


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

        channel.exchangeDeclare(exchange_routing_name,"direct");

        channel.queueBind(queue_name1,exchange_routing_name,routing_key_01);
        channel.queueBind(queue_name1,exchange_routing_name,routing_key_02);


       channel.basicConsume(queue_name1,true,new DefaultConsumer(channel){
           public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                   throws IOException {
               //System.out.println(envelope.getDeliveryTag()+"============================================");
               System.out.println(new String(body,0,body.length,"UTF-8"));
           }
       });
        System.in.read();

    }
}
