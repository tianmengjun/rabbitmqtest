package com.biz.fanout;


import java.io.IOException;
/**   
* @version 1.0   
* @author TianMengJun
* @since JDK 1.8.0_20
* Create at:   2018年2月3日 下午1:56:51   
* Description:  
*
*@param     
*/
public class Main {
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws Exception{
		Consumer consumer = new Consumer();
		consumer.start();
		
		Producer producer = new Producer();	
		producer.start();
	
	}
}