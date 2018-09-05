package com.jason.chat.client.chatclient;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * 从服务器拿数据，时刻监听着
 */
public class ReadFromServer implements Runnable {

    private Socket socket;

    public ReadFromServer(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        Scanner scanner = null;
        try {
            scanner = new Scanner(socket.getInputStream());
            scanner.useDelimiter("\n");
            while (scanner.hasNext()) {
                System.out.println(scanner.next());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(scanner != null) {
                scanner.close();
            }
        }
    }
}
