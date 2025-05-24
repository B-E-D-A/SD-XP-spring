package org.chat.client;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;


public class MessageHandler {
    private final String host;
    private final ConnectionFactory factory;
    private String currentChannel;
    private Channel channel;
    private Connection connection;
    private String queueName;

    public MessageHandler(ConnectionFactory factory, String host, String channel) {
        this.factory = factory;
        this.host = host != null ? host : "localhost";
        this.currentChannel = channel != null ? channel : "general";
    }

    public MessageHandler(String host, String channel) {
        this(new ConnectionFactory(), host, channel);
    }

    public void connect() throws IOException, TimeoutException {
        factory.setHost(host);
        connection = factory.newConnection();
        channel = connection.createChannel();
        setupExchange(currentChannel);
        setupConsumer();
    }

    private void setupExchange(String channelName) throws IOException {
        channel.exchangeDeclare(channelName, BuiltinExchangeType.FANOUT);
        queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, channelName, "");
    }

    private void setupConsumer() throws IOException {
        DeliverCallback callback = (tag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("[" + delivery.getEnvelope().getExchange() + "] " + message);
        };
        channel.basicConsume(queueName, true, callback, tag -> {
        });
    }

    public void sendMessage(String message) throws IOException {
        String formatted = "[" + System.getProperty("user.name") + "] " + message;
        channel.basicPublish(currentChannel, "", null, formatted.getBytes(StandardCharsets.UTF_8));
    }

    public void close() {
        try {
            if (channel != null) channel.close();
            if (connection != null) connection.close();
        } catch (IOException | TimeoutException e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
    }

    public String getHost() {
        return host;
    }

    public String getCurrentChannel() {
        return currentChannel;
    }

    public void switchChannel(String newChannelName) throws IOException {
        if (newChannelName == null || newChannelName.isEmpty()) {
            System.out.println("Error: Channel name cannot be empty");
            return;
        }

        channel.queueUnbind(queueName, currentChannel, "");
        channel.exchangeDeclare(newChannelName, BuiltinExchangeType.FANOUT);
        queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, newChannelName, "");
        DeliverCallback callback = (tag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("[" + delivery.getEnvelope().getExchange() + "] " + message);
        };
        channel.basicConsume(queueName, true, callback, tag -> {
        });

        currentChannel = newChannelName;
        System.out.println("Switched to channel: " + currentChannel);
    }

}
