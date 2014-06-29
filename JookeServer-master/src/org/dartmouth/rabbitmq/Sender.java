package org.dartmouth.rabbitmq;

import java.io.IOException;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;


// reference: http://www.rabbitmq.com/tutorials/tutorial-one-java.html
public class Sender {
	private final static String QUEUE_NAME = "notification";

	public void send() throws IOException {

		// connect to the rabbitmq server
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		// declare the queue
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		String message = "Hello World!";
		channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
		
		// close the channel
		channel.close();
		connection.close();
	}

}
