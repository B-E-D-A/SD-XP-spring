package org.chat;

import org.chat.client.ChatClient;

public class Main {
    public static void main(String[] args) {
        try {
            new ChatClient("localhost", "general").start();
        } catch (Exception e) {
            System.err.println("Failed to start chat client: " + e.getMessage());
            e.printStackTrace();
        }
    }
}