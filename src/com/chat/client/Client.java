package com.chat.client;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5000)) {
            PrintWriter cout = new PrintWriter(socket.getOutputStream(), true);
            Scanner sc = new Scanner(System.in);
            String reply;
            String name = "empty";

            ThreadClient threadClient = new ThreadClient(socket);
            new Thread(threadClient).start();
            do {
                if (name.equals("empty")) {
                    System.out.println("Enter your name : ");
                    reply = sc.nextLine();
                    name = reply;
                    cout.println(reply + " Joined Chat-room.");
                } else {
                    String message = (name + " : ");
                    reply = sc.nextLine();
                    cout.println(message + reply);
                }

                if (reply.equals("exit")) {
                    break;
                }
            } while (!reply.equals("exit"));
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }
}