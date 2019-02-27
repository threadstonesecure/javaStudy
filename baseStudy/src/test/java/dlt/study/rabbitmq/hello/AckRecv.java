package dlt.study.rabbitmq.hello;

import java.io.IOException;

import com.rabbitmq.client.*;

public class AckRecv {

	public static void main(String[] args) throws IOException,
			InterruptedException {
		ConnectionFactory factory = new ConnectionFactory();

		factory.setHost(QueueInfo.HOST);
		factory.setUsername(QueueInfo.USERNAME);
		factory.setPassword(QueueInfo.PASSWORD);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		int prefetchCount = 1;
		channel.basicQos(prefetchCount);// don't dispatch a new message to a
										// worker until it has processed and
										// acknowledged the previous one.
		boolean durable = true; // 是否持久化
		channel.queueDeclare(QueueInfo.QUEUE_NAME, durable, false, false, null);
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
		QueueingConsumer consumer = new QueueingConsumer(channel);
		boolean autoAck = false; // 消费者是否自动确认，false 需要手工channel.basicAck确认
		channel.basicConsume(QueueInfo.QUEUE_NAME, autoAck, consumer);

/*		channel.basicConsume(QueueInfo.QUEUE_NAME, autoAck, "a-consumer-tag",
				new DefaultConsumer(channel) {
					@Override
					public void handleDelivery(String consumerTag,
											   Envelope envelope,
											   AMQP.BasicProperties properties,
											   byte[] body)
							throws IOException
					{
						long deliveryTag = envelope.getDeliveryTag();
						// positively acknowledge a single delivery, the message will
						// be discarded
						channel.basicAck(deliveryTag, false);
					}
				});*/
		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String message = new String(delivery.getBody());
			doWork(message);
			System.out.println(" [x] Received '" + message + "'");
			channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);// 手工确认.如果不确认，after the  worker  dies  all  unacknowledgedmessages will be redelivered
		}
	}

	private static void doWork(String task) throws InterruptedException {
		for (char ch : task.toCharArray()) {
			if (ch == '.')
				Thread.sleep(1000);
		}
	}
}
