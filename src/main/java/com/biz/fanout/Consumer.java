package com.biz.fanout;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * @version 1.0
 * @author TianMengJun
 * @since JDK 1.8.0_20 
 * Create at: 2018年2月3日 下午1:51:32
 *  Description:
 *
 * @param
 */

public class Consumer extends Thread {
	private static final String USERNAME = "guest";
	private static final String PASSWORD = "guest";
	private static final String HOST = "localhost";
	private static final String EXCHANGE_NAME = "chat";
	private static final String ROUTING_KEY = "";
	private static String QUEUE_NAME;
	private static Connection connection;
	private static Channel channel;

	void receiveMassage() {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(HOST);
		factory.setUsername(USERNAME);
		factory.setPassword(PASSWORD);

		try {
			connection = factory.newConnection();
			channel = connection.createChannel();
			channel.exchangeDeclare(EXCHANGE_NAME, "fanout", true);
			QUEUE_NAME = channel.queueDeclare().getQueue();
			channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}

		// 设置手动应答
		boolean autoAck = false;
		String consumerTag = "";

		try {
			channel.basicConsume(QUEUE_NAME, autoAck, consumerTag, new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
						byte[] body) throws IOException {
					long deliveryTag = envelope.getDeliveryTag();
					String massage = new String(body, "UTF-8");
					System.out.println("receive:" + massage);
					
					channel.basicAck(deliveryTag, false);

				}
			});
		} catch (IOException e) {

			e.printStackTrace();
		}finally {
		
		if (connection != null) {
			try {
				connection.close();
			} catch (Exception ignore) {
			}
		}
		if (channel != null) {
			try {
				channel.close();
			} catch (Exception ignore) {
				
			}
			}
		
	}
	}

	public void run() {
		this.receiveMassage();
	}
}