package com;

import javax.jms.Connection;
import javax.jms.JMSException;

import org.apache.qpid.jms.JmsConnectionFactory;

public class jmsConnectionUtil {


	static Connection connection = null;
	
	private jmsConnectionUtil() {
		
		getConnection();
	}
	
	private static jmsConnectionUtil jmsConnectionUtil = null;
	
	public static  jmsConnectionUtil getInstance() {
		if (jmsConnectionUtil == null) {
			jmsConnectionUtil = new jmsConnectionUtil();
		}
		return jmsConnectionUtil;
		
	}
	
	private static Connection getConnection() {

        JmsConnectionFactory factory = new JmsConnectionFactory("amqp://localhost:5672");
        
		try {
			connection = factory.createConnection("admin", "admin");
		System.out.println(connection);
        return connection;
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}
	
	public static void main(String[] args) {
		getConnection();
	}
}
