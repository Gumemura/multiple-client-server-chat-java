package com.chat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        String reply;
        String name = "empty";
        Socket socket = new Socket("localhost", 2000);
        BufferedReader cin = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter cout = new PrintWriter(socket.getOutputStream(), true);

        ThreadClient threadClient = new ThreadClient(socket);
        Thread thread = new Thread(threadClient);
        thread.start();

        do {
            if (name.equals("empty")) {
                System.out.println("Enter your name: ");
                reply = sc.nextLine();
                name = reply;
                cout.println("Welcome to chat room " + reply);
            } else {
                String existingClientName = name + ": ";
                System.out.println(existingClientName);
                reply = sc.nextLine();
                System.out.println(reply);
                System.out.println(reply);
                cout.println(existingClientName + reply);
            }

            if (reply.equals("stop")) {
                break;
            }
        } while (reply.equals("stop"));
    }
}
