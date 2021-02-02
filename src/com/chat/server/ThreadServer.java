package com.chat.server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ThreadServer extends Thread {
    private Socket socket;
    private ArrayList<ThreadServer> threadList;
    private ArrayList<Socket> clients;
    private PrintWriter output;

    public ThreadServer(Socket socket, ArrayList<ThreadServer> threads, ArrayList<Socket> clients) {
        this.socket = socket;
        this.threadList = threads;
        this.clients = clients;
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
                showMessageToAllClients(socket, outputString);
                System.out.println(outputString);
            }
        } catch (EOFException e) {
            clients.remove(socket);
            System.out.println("removed");
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }

    private void showMessageToAllClients(Socket sender, String outputString) {
        Socket s;
        PrintWriter p;
        for (int i = 0; i < clients.size(); i++) {
            s = clients.get(i);
            try {
                if (s != sender) {
                    p = new PrintWriter(s.getOutputStream(), true);
                    p.println(outputString);
                }
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
    }
}
