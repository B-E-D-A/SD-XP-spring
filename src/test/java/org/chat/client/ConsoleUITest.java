package org.chat.client;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.mockito.Mockito.*;

class ConsoleUITest {

    @Test
    void testExitCommand() throws IOException {
        MessageHandler mockHandler = mock(MessageHandler.class);

        String input = "!exit\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ConsoleUI ui = new ConsoleUI(mockHandler);
        ui.listen();

        verify(mockHandler, never()).sendMessage(anyString());
    }

    @Test
    void testSendMessageCommand() throws IOException {
        MessageHandler mockHandler = mock(MessageHandler.class);

        String input = "Hello\n!exit\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ConsoleUI ui = new ConsoleUI(mockHandler);
        ui.listen();

        verify(mockHandler).sendMessage("Hello");
    }

    @Test
    void testSwitchChannelCommand() throws IOException {
        MessageHandler mockHandler = mock(MessageHandler.class);

        String input = "!switch mychannel\n!exit\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ConsoleUI ui = new ConsoleUI(mockHandler);
        ui.listen();

        verify(mockHandler).switchChannel("mychannel");
    }
}
