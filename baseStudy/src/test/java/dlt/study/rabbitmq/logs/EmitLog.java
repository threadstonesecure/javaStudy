package dlt.study.rabbitmq.logs;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import dlt.study.rabbitmq.RabbitMQInfo;

public class EmitLog {

	private static final String EXCHANGE_NAME = "logs";

	public static void main(String[] args) throws IOException,
			InterruptedException {
		ConnectionFactory factory = new ConnectionFactory();

		factory.setHost(RabbitMQInfo.HOST);
		factory.setUsername(RabbitMQInfo.USERNAME);
		factory.setPassword(RabbitMQInfo.PASSWORD);
		factory.setVirtualHost(RabbitMQInfo.VHOST);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

		String message="denglt send info !!!!!!!!!!!!";
		channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
		System.out.println(" [x] Sent '" + message + "'");
		
		channel.close();
		connection.close();
	}
}
