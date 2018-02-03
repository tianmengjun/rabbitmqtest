/**
 * TODO
 * 
 */
package com.biz.topic;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.rabbitmq.client.Channel;
/**   
* @version 1.0   
* @author TianMengJun
* @since JDK 1.8.0_20
* Create at:   2018年2月3日 下午1:56:51   
* Description:  
*
*@param     
*/
public class Send extends Thread{
	private static final String USERNAME = "guest";
	private static final String PASSWORD = "guest";
	private static final String HOST = "localhost";
	private static final String EXCHANGE_NAME = "topic_chat";
	 Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
public void run() {
	while(true) {
		 synchronized(this){
		   System.out.println("me >");
		  Scanner scan = new Scanner(System.in);
		  String read = scan.nextLine();
		
		  this.sendMessage(read);
			}
		}
	
}
	public  void sendMessage(String message) {
		Connection connection = null;
		Channel channel = null;
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(HOST);
			factory.setUsername(USERNAME);
			factory.setPassword(PASSWORD);	
			connection = factory.newConnection();
			channel = connection.createChannel();	
			channel.exchangeDeclare(EXCHANGE_NAME,"topic",false);
			String[] routingKeys = new String[]{"one.rabbit", 
												"two.kkkk", 
												"wechat.rabbit"
												};

	        for(String severity :routingKeys){
	        	channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes());
	        	logger.info("Sent: " + severity + ":" + message);
	        }
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (Exception ignore) {
				}
			}
		}
	}
}