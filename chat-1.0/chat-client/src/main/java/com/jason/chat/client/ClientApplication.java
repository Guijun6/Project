package com.jason.chat.client;

import com.jason.chat.client.chatclient.OutToServer;
import com.jason.chat.client.chatclient.ReadFromServer;

import java.io.IOException;
import java.net.Socket;

public class ClientApplication {
    public static void main(String[] args) {

                // 打印聊天规则
        System.out.println("***************************************************");
        System.out.println("***用户注册：userName:同户名(仅限一次) ************");
        System.out.println("***发群消息使用：G:message 例：G:hello ************");
        System.out.println("***私聊：P:userName:message 例：P:zhangsan:hello***");
        System.out.println("***********退出聊天室：bye  ***********************");


        // 从服务端获取一个 Socket
        try {
            Socket socket = new Socket("127.0.0.1", 6666);

            Thread outThread = new Thread(new OutToServer(socket));
            Thread inThread = new Thread(new ReadFromServer(socket));

            outThread.start();
            inThread.start();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("服务出问题啦");
        }
    }
}