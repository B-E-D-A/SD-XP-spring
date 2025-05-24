package org.chat.client;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class MessageHandlerTest {

    @Mock
    private ConnectionFactory factory;

    @Mock
    private Connection connection;

    @Mock
    private Channel channel;

    private MessageHandler handler;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        when(factory.newConnection()).thenReturn(connection);
        when(connection.createChannel()).thenReturn(channel);
        when(channel.queueDeclare()).thenReturn(mock(AMQP.Queue.DeclareOk.class));

        handler = new MessageHandler(factory, "localhost", "testChannel");
        handler.connect();
    }

    @Test
    void testSendMessage() throws Exception {
        handler.sendMessage("Test message");
        verify(channel).basicPublish(eq("testChannel"), eq(""), isNull(), any());
    }

    @Test
    void testClose() throws Exception {
        handler.close();
        verify(channel).close();
        verify(connection).close();
    }
}
