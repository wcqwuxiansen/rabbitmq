package com.rabbitmq.producer.publishsubscribe;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class PublishSubscribeProducer {
    private static final String exchang_fanout_name ="exchange_fanout_name";
    private static final String queue_name1 = "queue_name01";
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
        channel.queueDeclare(queue_name1,true,false,false,null);
        channel.queueDeclare(queue_name2,true,false,false,null);
        //发布订阅模式队列的routingkey应该设置成空串
        channel.queueBind(queue_name1,exchang_fanout_name,"");
        channel.queueBind(queue_name2,exchang_fanout_name,"");

        channel.basicPublish(exchang_fanout_name,"",null,"fanout交换机实现的发布订阅模式".getBytes("utf-8"));
    }
}
