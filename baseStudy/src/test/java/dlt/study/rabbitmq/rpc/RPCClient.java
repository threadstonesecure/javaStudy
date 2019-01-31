package dlt.study.rabbitmq.rpc;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import dlt.study.rabbitmq.RabbitMQInfo;

public class RPCClient {
    private Connection connection;
    private Channel channel;
    private String requestQueueName = "rpc_queue";
    private String replyQueueName;

    public RPCClient() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost(RabbitMQInfo.HOST);
        factory.setUsername(RabbitMQInfo.USERNAME);
        factory.setPassword(RabbitMQInfo.PASSWORD);
        factory.setVirtualHost(RabbitMQInfo.VHOST);
        connection = factory.newConnection();
        channel = connection.createChannel();
    }

    public String call(String message) throws Exception {
        replyQueueName = channel.queueDeclare() // Actively declare a server-named exclusive, autodelete, non-durable queue
                .getQueue();
        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(replyQueueName, true, consumer);

        String response ;
        String corrId = java.util.UUID.randomUUID().toString();

        BasicProperties props = new BasicProperties.Builder()
                .correlationId(corrId).replyTo(replyQueueName) // 返回消息的队列名称
                .build();

        channel.basicPublish("", requestQueueName, props, message.getBytes());

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                response = new String(delivery.getBody());
                break;
            }
        }

        return response;
    }

    public void close() throws Exception {
        connection.close();
    }

    public static void main(String[] args) throws Exception {
        RPCClient fibonacciRpc = new RPCClient();

        System.out.println(" [x] Requesting fib(30)");
        String response = fibonacciRpc.call("30");
        System.out.println(" [.] Got '" + response + "'");

        fibonacciRpc.close();
    }
}
