package com;

// JMS API types
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

class Producer {

    public static void main(String[] args) throws Exception {
    	sendMessage(args);
    	
    }
    
    private static void sendMessage(String[] args) throws JMSException {
    	/*  Every JMS provider (every library that implements the JMS API) 
    	 *  will have its own implementation of the javax.jms.ConnectionFactory. 
    	 * 
    	 *  The purpose of the ConnectionFactory is to create a network connection 
    	 *  to a specific JMS broker, such as ActiveMQ, or a specific protocol,
    	 *  such as AMQP.  This allows the JMS library to send and receive messages
    	 *  over a network from the broker.
    	 * 
    	 *  In this case we are using the Apache Qpid JMS library which is specific 
    	 *  to the protocol, AMQP. AMQP is only one of ten protocols currently supported by
    	 *  ActiveMQ.
    	 */
    	
    	Connection connection =  jmsConnectionUtil.getInstance().connection;
		//        JmsConnectionFactory factory = new JmsConnectionFactory("amqp://localhost:5672");
//        Connection connection = factory.createConnection("admin", "admin");
//        connection.start();
        /*  Every JMS Connection can have multiple sessions which manage things like
         *  transactions and message persistence separately.  In practice multiple sessions
         *  are not used much by developers but may be used in more sophisticated
         *  application servers to conserve resources.
         */
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        
        /*  A Destination is an address of a specific Topic or Queue hosted by the 
         *  JMS broker. The only difference between using JMS for a Topic (Pub/Sub) 
         *  and Queue (P2P) is this bit of code here - at least in the simplest 
         *  cases.  
         *  
         *  That said, there are significant differences between Topic- and 
         *  Queue-based messaging and understanding those differences is key to 
         *  understanding JMS and messaging systems in general. This is discussed in 
         *  more detail the blog post, "5 Minutes or Less: ActiveMQ with JMS Queues and Topics".
         */
        Destination destination = null;
        System.out.println(" Arguments:::"+args[0]);
        if(args.length > 0 && args[0].equalsIgnoreCase("QUEUE")) {        	
        	destination = session.createQueue("MyQueue");	
        }else if(args.length > 0 && args[0].equalsIgnoreCase("TOPIC"))  {        	
        	destination = session.createTopic("MyTopic");        	
        }else {
        	System.out.println("Error: You must specify Queue or Topic");
        	connection.close();
        	System.exit(1);
        }
        
        /*  A MessageProducer is specific to a destination - it can only send 
         *  messages to one Topic or Queue. 
		 */
        MessageProducer producer = session.createProducer(destination);

        
        /* This section of code simply reads input from the console and then sends that
         * input as JMS Message to the ActiveMQ broker.
         */
        
        for (int x=0;x<100; x++) {
        String message = "Welcome to new World:::ABCD"+x;
        TextMessage msg = session.createTextMessage(message);
        producer.send(msg);
        }
        /* As is the case with most enterprise resources, you want to shut a JMS connection
         * down when you are done using it.  This tells the JMS broker that it can free
         * up the network resources used for that connection making the whole system more 
         * scalable.
         */
        connection.close();
    }
}