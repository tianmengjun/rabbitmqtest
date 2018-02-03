
package com.biz.topic;

import com.rabbitmq.client.*;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**   
* @version 1.0   
* @author TianMengJun
* @since JDK 1.8.0_20
* Create at:   2018年2月3日 下午1:56:51   
* Description:  
*
*@param     
*/
public class Receive extends Thread{

	private static final String USERNAME = "guest";
	private static final String PASSWORD = "guest";
	private static final String HOST = "localhost";
	private static final String EXCHANGE_NAME = "topic_chat";

  Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
	public void run() {
		try {
			this.receive();
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (TimeoutException e) {

			e.printStackTrace();
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	public void receive() throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(HOST);
		factory.setUsername(USERNAME);
		factory.setPassword(PASSWORD);
		Connection connection = factory.newConnection();
		final Channel channel = connection.createChannel();

		channel.exchangeDeclare(EXCHANGE_NAME, "topic");
		String queueName = channel.queueDeclare().getQueue();

		String[] routingKeys = new String[] { "wechat.rabbit" };

		for (String bindingKey : routingKeys) {
			channel.queueBind(queueName, EXCHANGE_NAME, bindingKey);
		}

		boolean autoAck = false;
		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				logger.info("received:"+envelope.getRoutingKey() + "':'" + message);
				channel.basicAck(envelope.getDeliveryTag(), false);
			}
		};
		channel.basicConsume(queueName, autoAck, "", consumer);
	}
}