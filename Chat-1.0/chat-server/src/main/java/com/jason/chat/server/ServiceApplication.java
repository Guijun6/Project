package com.jason.chat.server;

import com.jason.chat.server.service.ChatService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServiceApplication {

    // 服务端的线程池最大数量
    private final static int MAX_THREAD_COUNT = 20;
    /**
     * 服务器启动方法
     *
     * @param args
     */
    public static void main(String[] args) {
        // 创建一个线程池
        ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREAD_COUNT);

        // 创建一个服务器Socket
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(6666);

            System.out.println("等待客户端连接");
            for (int i = 0; i < MAX_THREAD_COUNT; i++) {

                // 与每个连接上的客户端建立稳定的连接
                Socket socket = serverSocket.accept();
                System.out.println(socket+"已连接");
                // 给连接上的客户端分派一个线程去执行任务
                ChatService chatService = new ChatService();
                chatService.setClientSocket(socket);
                // 交给线程池去处理
                executorService.execute(chatService);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
