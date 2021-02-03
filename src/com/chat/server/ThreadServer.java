package com.chat.server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ThreadServer extends Thread {
    private Socket socket;
    private ArrayList<Socket> clients;
    private PrintWriter output;

    public ThreadServer(Socket socket, ArrayList<Socket> clients) {
        this.socket = socket;
        this.clients = clients;
    }

    @Override
    public void run() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);

            while (true) {
                String outputString = input.readLine();
                if (outputString.equals("logout")) {
                    break;
                }
                showMessageToAllClients(socket, outputString);
                System.out.println(outputString);
            }
        } catch (EOFException e) {
            clients.remove(socket);
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }

    private void showMessageToAllClients(Socket sender, String outputString) {
        Socket socket;
        PrintWriter printWriter;
        int i = 0;
        while (i < clients.size()) {
            socket = clients.get(i);
            i++;
            try {
                if (socket != sender) {
                    printWriter = new PrintWriter(socket.getOutputStream(), true);
                    printWriter.println(outputString);
                }
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
    }
}
