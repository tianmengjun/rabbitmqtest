package com.biz.fanout;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;


/**
 * @version 1.0
 * @author TianMengJun
 * @since JDK 1.8.0_20 Create at: 2018年2月3日 下午1:47:33 Description:
 *
 * @param
 */

public class Producer extends Thread {
	private static final String USERNAME = "guest";
	private static final String PASSWORD = "guest";
	private static final String HOST = "localhost";
	private static String EXCHANGE_NAME = "chat";
	private  String nikName =null; //把路由键当作用户名 ROUTING_KEY
	//private static String QUEUE_NAME;
	private  Channel channel;
	private  Connection connection;
public void run(){
	  while(nikName==null) {
	  System.out.println("请输入你的昵称>");
	  Scanner s = new Scanner(System.in);
	  nikName = s.nextLine();
	  }
	while(true) {
	 synchronized(this){
	   System.out.println(nikName+"：>");
	  Scanner scan = new Scanner(System.in);
	  String read = scan.nextLine();
	
	  this.sendMessage(read);
		}
	}
}
	public void sendMessage(String message) {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(HOST);
		factory.setUsername(USERNAME);
		factory.setPassword(PASSWORD);
		try {
			connection = factory.newConnection();
			channel = connection.createChannel();
			channel.exchangeDeclare(EXCHANGE_NAME, "fanout", true);
			// 设置消息持久化
			AMQP.BasicProperties.Builder properties = new AMQP.BasicProperties().builder();
			properties.deliveryMode(2); // 设置消息是否持久化，1： 非持久化 2：持久化
			// 方式二设置消息持久化：MessageProperties.PERSISTENT_TEXT_PLAIN
			channel.basicPublish(EXCHANGE_NAME, nikName, properties.build(), message.getBytes());
		} catch (IOException e1) {

			e1.printStackTrace();
		} catch (TimeoutException e1) {

			e1.printStackTrace();
		}/*finally {
			
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
			
		}*/

	}

}