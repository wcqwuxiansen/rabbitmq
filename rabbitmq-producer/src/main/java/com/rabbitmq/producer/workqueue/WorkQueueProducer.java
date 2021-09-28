package com.rabbitmq.producer.workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class WorkQueueProducer {
    private static final String work_queue_name = "work_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.11.130");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("2021_vhost");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(work_queue_name,true,false,false,null);

         //工作模式发布时交换机应该为空串，routing_key应该和队列名称保持一致
        for(int i=0;i<10;i++){
            channel.basicPublish("",work_queue_name,null,(i+"-hello work queue模式").getBytes("UTF-8"));
        }


    }
}
