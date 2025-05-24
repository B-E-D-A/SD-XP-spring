package org.chat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleUI {
    private final MessageHandler messageHandler;
    private volatile boolean isRunning = true;

    public ConsoleUI(MessageHandler handler) {
        this.messageHandler = handler;
    }

    public void printWelcome() {
        System.out.println("=== BRINA CHAT ===");
        System.out.println("Connected to server: " + messageHandler.getHost());
        System.out.println("Current channel: " + messageHandler.getCurrentChannel());
        System.out.println("Available commands:");
        System.out.println("!switch <channel> - Change channel");
        System.out.println("!exit - Quit chat");
        System.out.println("====================");
    }

    public void listen() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (isRunning) {
            String input = reader.readLine();
            if (input == null || input.isEmpty()) continue;

            if (input.startsWith("!switch ")) {
                messageHandler.switchChannel(input.substring(8).trim());
            } else if (input.equalsIgnoreCase("!exit")) {
                isRunning = false;
                System.out.println("Disconnecting...");
            } else {
                messageHandler.sendMessage(input);
            }
        }
    }
}
