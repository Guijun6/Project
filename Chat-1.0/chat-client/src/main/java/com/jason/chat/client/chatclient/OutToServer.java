package com.jason.chat.client.chatclient;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * 输出消息
 */
public class OutToServer implements Runnable {

    private Socket socket;

    public OutToServer(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        PrintStream printStream = null;
        Scanner scanner = null;
        try {
            printStream = new PrintStream(socket.getOutputStream());
            scanner = new Scanner(System.in);
            scanner.useDelimiter("\n");
            while (true) {
                if (scanner.hasNext()) {
                    String string = scanner.next();
                    printStream.println(string);
                    if (string.equals("logout")) {
                        System.out.println("我下线了");
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (printStream != null) {
                printStream.close();
            }
            if (scanner != null) {
                scanner.close();
            }
        }
    }
}
