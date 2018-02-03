package com.biz.topic;
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
	
	public static void main(String[] args) throws Exception{
		Send send = new Send();
		send.start();
		
		Receive receive = new Receive();	
		receive.start();
	
	}
}