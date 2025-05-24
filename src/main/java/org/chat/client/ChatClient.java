package org.chat.client;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ChatClient {
    private final MessageHandler messageHandler;
    private final ConsoleUI consoleUI;

    public ChatClient(String host, String channel) {
        this(new MessageHandler(host, channel));
    }

    public ChatClient(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
        this.consoleUI = new ConsoleUI(this.messageHandler);
    }

    public ChatClient(MessageHandler messageHandler, ConsoleUI consoleUI) {
        this.messageHandler = messageHandler;
        this.consoleUI = consoleUI != null ? consoleUI : new ConsoleUI(messageHandler);
    }

    public void start() {
        try {
            messageHandler.connect();
            consoleUI.printWelcome();
            consoleUI.listen();
        } catch (IOException | TimeoutException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            messageHandler.close();
        }
    }
}
