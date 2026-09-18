package com.BidNest.BiddingService;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class KVNode {
    public static void main(String[] args) throws IOException {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 7000;
        Store store = new Store();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("KVNode listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                new Thread(new ClientHandler(clientSocket, store)).start();
            }
        }
    }
}