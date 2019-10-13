package com.study.rabbitmq.uilt;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Auther: allen
 * @Date: 2019/2/14 17:53
 */
public class ProducerApp {
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = null;
        Channel channel = null;
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("rabbitmq.dn.com");
            factory.setPort(5672);
            factory.setUsername("guest");
            factory.setPassword("guest");
            factory.setVirtualHost("/");

            //创建与RabbitMQ服务器的TCP连接
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare("firstQueue", true, false, false, null);
            String message = "First Message";
            channel.basicPublish("", "firstQueue", null, message.getBytes());
            System.out.println("Send Message is:'" + message + "'");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (channel != null) {
                channel.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }
}
