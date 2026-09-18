package com.BidNest.BiddingService;


import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final Store store;

    public ClientHandler(Socket socket, Store store) {
        this.socket = socket;
        this.store = store;
    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String line;
            while ((line = in.readLine()) != null) {
                String response = handleCommand(line.trim());
                out.println(response);
            }
        } catch (IOException e) {
            System.out.println("Connection closed: " + e.getMessage());
        } finally {
            try { socket.close(); } catch (IOException ignored) {}
        }
    }

    private String handleCommand(String line) {
        String[] parts = line.split(" ", 3); // command, key, value(optional)
        if (parts.length == 0 || parts[0].isEmpty()) {
            return "ERROR empty command";
        }

        String command = parts[0].toUpperCase();

        switch (command) {
            case "PUT":
                if (parts.length < 3) return "ERROR PUT requires key and value";
                store.put(parts[1], parts[2]);
                return "OK";

            case "GET":
                if (parts.length < 2) return "ERROR GET requires key";
                String value = store.get(parts[1]);
                return value != null ? "VALUE " + value : "NOT_FOUND";

            case "DELETE":
                if (parts.length < 2) return "ERROR DELETE requires key";
                boolean removed = store.delete(parts[1]);
                return removed ? "OK" : "NOT_FOUND";

            default:
                return "ERROR unknown command: " + command;
        }
    }
}