package com.chat.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ThreadServer extends Thread {
    private Socket socket;
    private ArrayList<ThreadServer> threadList;
    private PrintWriter output;

    public ThreadServer(Socket socket, ArrayList<ThreadServer> threads) {
        this.socket = socket;
        this.threadList = threads;
    }

    @Override
    public void run() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);

            while (true) {
                String outputString = input.readLine();
                if (outputString.equals("exit")) {
                    break;
                }
                printToALlClients(outputString);
                System.out.println("Server received " + outputString);
            }
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }

    private void printToALlClients(String outputString) {
        for (ThreadServer sT : threadList) {
            sT.output.println(outputString);
        }

    }
}
