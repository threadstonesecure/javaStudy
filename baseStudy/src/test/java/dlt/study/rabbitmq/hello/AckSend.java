package dlt.study.rabbitmq.hello;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import dlt.infrastructure.ThreadOut;

public class AckSend {

	private static String getMessage(String... strings) {
		if (strings.length < 1)
			return "Hello World!";
		return joinStrings(strings, " ");
	}

	private static String joinStrings(String[] strings, String delimiter) {
		int length = strings.length;
		if (length == 0)
			return "";
		StringBuilder words = new StringBuilder(strings[0]);
		for (int i = 1; i < length; i++) {
			words.append(delimiter).append(strings[i]);
		}
		return words.toString();
	}

	public static void main(String[] args) throws IOException,
			InterruptedException {
		ConnectionFactory factory = new ConnectionFactory();

		factory.setHost(QueueInfo.HOST);
		factory.setUsername(QueueInfo.USERNAME);
		factory.setPassword(QueueInfo.PASSWORD);
		factory.setVirtualHost(QueueInfo.VIRTUAL_HOST);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		boolean durable = true; // 是否持久化
		Map<String, Object> arguments = new HashMap<String, Object>();
		//arguments.put("x-max-length", 10000);
		channel.queueDeclare(QueueInfo.QUEUE_NAME, durable, false, false,
				arguments);
		channel.confirmSelect(); // Enables publisher acknowledgements on this channel
		               // 在生产者那里设置开启 confirm 模式之后，你每次写的消息都会分配一个唯一的 id，
		               // 然后如果写入了 RabbitMQ 中，RabbitMQ 会给你回传一个 ack 消息，告诉你说这个消息 ok 了。
		               //  如果 RabbitMQ 没能处理这个消息，会回调你的一个 nack 接口，告诉你这个消息接收失败，你可以重试。
		              //  而且你可以结合这个机制自己在内存里维护每个消息 id 的状态，如果超过一定时间还没接收到这个消息的回调，那么你可以重发
		channel.addConfirmListener(new PublishConfirmListener());

		// String message = "Hello World! again";
		long deliveryTag = channel.getNextPublishSeqNo(); // 在ConfirmListener中进行确认处理（Nack中对失败信息重发）

		String message = getMessage("NewTask", "First message.");
		channel.basicPublish("", QueueInfo.QUEUE_NAME,
				MessageProperties.PERSISTENT_BASIC, // 前提为queue的durable=ture
				message.getBytes());
		message = getMessage("NewTask", "Second  message..");
		channel.basicPublish("", QueueInfo.QUEUE_NAME,
				MessageProperties.PERSISTENT_BASIC, message.getBytes());
		message = getMessage("NewTask", "Third  message...");
		channel.basicPublish("", QueueInfo.QUEUE_NAME, null, message.getBytes());
		message = getMessage("NewTask", "Fourth  message....");
		channel.basicPublish("", QueueInfo.QUEUE_NAME, null, message.getBytes());

		message = getMessage("NewTask", "Fifth  message.....");
		channel.basicPublish("", // ""代表default exchange。The default exchange is
									// implicitly bound to every queue,
									// with a routing key equal to the queue
									// name. It is not possible to explicitly
									// bind to, or unbind from the default
									// exchange. It also cannot be deleted.
				QueueInfo.QUEUE_NAME, null, message.getBytes());

		channel.basicPublish("amq.direct", "hello.do", MessageProperties.PERSISTENT_BASIC, message.getBytes());
		System.out.println(" [x] Sent '" + message + "'");
/*		for (int i = 0; i < 50000; i++) {
			channel.basicPublish("", QueueInfo.QUEUE_NAME, null,
					("dlt" + i).getBytes());
		}
		*/
		channel.waitForConfirmsOrDie();
		System.out.println("finish publish.");
		channel.close();
		connection.close();
	}
}

class PublishConfirmListener implements ConfirmListener {
	@Override
	public void handleAck(long deliveryTag, boolean multiple)
			throws IOException {
		ThreadOut.println("PublishConfirmListener.handleAck:" + deliveryTag);

	}

	@Override
	public void handleNack(long deliveryTag, boolean multiple)
			throws IOException {
		ThreadOut.println("PublishConfirmListener.handleNack:" + deliveryTag);

	}
}
