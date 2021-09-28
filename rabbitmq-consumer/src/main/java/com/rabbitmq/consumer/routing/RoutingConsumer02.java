package com.rabbitmq.consumer.routing;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RoutingConsumer02 {

    private static final String queue_name2 = "queue_routing_name02";

    private static final String exchange_routing_name = "exchang_routing";

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

        channel.queueDeclare(queue_name2,true,false,false,null);
        channel.exchangeDeclare(exchange_routing_name,"direct");

        channel.queueBind(queue_name2,exchange_routing_name,routing_key_03);

        channel.basicConsume(queue_name2,true,new DefaultConsumer(channel){
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                //System.out.println(envelope.getDeliveryTag()+"============================================");
                System.out.println(new String(body,0,body.length,"UTF-8"));
            }
        });
        System.in.read();




    }
}
