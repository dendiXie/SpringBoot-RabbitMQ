package com.study.rabbitmq.uilt;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @Auther: allen
 * @Date: 2019/2/14 17:56
 */
public class ConsumerApp {
    public static void main(String[] args)
    {
        Connection connection = null;
        Channel channel = null;
        try
        {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("rabbitmq.dn.com");
            factory.setPort(5672);
            factory.setUsername("guest");
            factory.setPassword("guest");
            factory.setVirtualHost("/");
            connection = factory.newConnection();
            channel = connection.createChannel();

            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                        throws IOException {
                    String message = new String(body, "UTF-8");
                    System.out.println(" Consumer have received '" + message + "'");
                }
            };
            channel.basicConsume("firstQueue", true, consumer);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
