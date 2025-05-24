package org.chat.client;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.mockito.Mockito.*;

public class ChatClientTest {

    @Test
    void testStart() throws IOException, TimeoutException {
        MessageHandler mockHandler = mock(MessageHandler.class);
        ConsoleUI mockUI = mock(ConsoleUI.class);

        ChatClient client = new ChatClient(mockHandler, mockUI);
        client.start();

        verify(mockHandler).connect();
        verify(mockUI).printWelcome();
        verify(mockUI).listen();
        verify(mockHandler).close();
    }

    @Test
    void testStartWithException() throws IOException, TimeoutException {
        MessageHandler mockHandler = mock(MessageHandler.class);
        ConsoleUI mockUI = mock(ConsoleUI.class);

        doThrow(new IOException("connect failed")).when(mockHandler).connect();

        ChatClient client = new ChatClient(mockHandler, mockUI);
        client.start();

        verify(mockHandler).connect();
        verify(mockHandler).close();
        verify(mockUI, never()).printWelcome();
        verify(mockUI, never()).listen();
    }
}
