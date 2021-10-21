package com.rabbitmq.consumer.topic;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class TopicConsumer02 {

    private static final String exchange_topic_name="exchange_topic_name";

    private static final String queue_topic_name2 ="queue_topic_name02";

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
        channel.queueDeclare(queue_topic_name2,true,false,false,null);


        channel.queueBind(queue_topic_name2,exchange_topic_name,routing_key03);


        channel.basicConsume(queue_topic_name2,true,new DefaultConsumer(channel){
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                //System.out.println(envelope.getDeliveryTag()+"============================================");
                System.out.println(new String(body,0,body.length,"UTF-8"));
            }
        });
        System.in.read();
    }

    public void test2(){
        System.out.println("test2=================");
    }
}
