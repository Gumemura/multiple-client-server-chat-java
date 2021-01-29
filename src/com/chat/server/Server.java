package com.chat.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    public static void main(String[] args) {
        ArrayList<ThreadServer> threads = new ArrayList<>();
        try (ServerSocket serversocket = new ServerSocket(2000)) {
            System.out.println("Server is started...");
            while (true) {
                Socket socket = serversocket.accept();
                ThreadServer ThreadServer = new ThreadServer(socket, threads);
                threads.add(ThreadServer);
                ThreadServer.start();
                //get all the list of currently running thread
            }
        } catch (Exception e) {
            System.out.println("Error occurred in main: " + e.getStackTrace());
        }
    }
}
