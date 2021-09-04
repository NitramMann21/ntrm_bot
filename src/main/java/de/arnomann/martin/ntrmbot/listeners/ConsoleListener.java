package de.arnomann.martin.ntrmbot.listeners;

import de.arnomann.martin.ntrmbot.Bot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleListener {

    private static Thread listenerThread;

    public static void start() {
        listenerThread = new Thread(() -> {
            System.out.println("Started Console Listener");
            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                while((line = reader.readLine()) != null) {
                    if(Bot.jda != null && !line.isEmpty()) {
                        handleMessage(line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        listenerThread.setName("Console Listener Thread");
        listenerThread.start();
    }

    public static void shutdown() {
        listenerThread.interrupt();
    }

    private static void handleMessage(String message) {
        if(message.startsWith("exit") || message.startsWith("shutdown") || message.startsWith("quit")) { // EXIT
            try {
                Bot.shutdown();
            } catch (InterruptedException e) { e.printStackTrace(); }
        }
        else if(message.startsWith("help") || message.startsWith("cmds")) { // SHOW HELP
            help();
        }
    }

    private static void help() {
        System.out.println("[HELP] === List of console commands ===\n" +
                "[HELP] - exit / shutdown / quit || Stops the bot.\n" +
                "[HELP] - help / cmds || Prints this list.");
    }

}
