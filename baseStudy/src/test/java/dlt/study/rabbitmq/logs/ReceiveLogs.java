package dlt.study.rabbitmq.logs;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import dlt.study.rabbitmq.RabbitMQInfo;

public class ReceiveLogs {
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
		String queueName = channel.queueDeclare().getQueue();
		channel.queueBind(queueName, EXCHANGE_NAME, "");

		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(queueName, true, consumer);

		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String message = new String(delivery.getBody());

			System.out.println(" [x] Received '" + message + "'");
		}
	}

}
