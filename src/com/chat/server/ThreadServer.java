package com.chat.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Thread for server
 */
public class ThreadServer extends Thread {
    private Socket socket;
    private ArrayList<ThreadServer> threads;
    private PrintWriter output;

    public ThreadServer(Socket socket, ArrayList<ThreadServer> threads) {
        this.socket = socket;
        this.threads = threads;
    }

    @Override
    public void run() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            while (true) {
                String outputString = input.readLine();
                if (outputString.equals("stop")) {
                    break;
                }
                showMessageToALlClients(outputString);
                System.out.println(outputString);
            }
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }

    private void showMessageToALlClients(String message) {
        for (ThreadServer threadServer : threads) {
            threadServer.output.println(message);
        }
    }
}
