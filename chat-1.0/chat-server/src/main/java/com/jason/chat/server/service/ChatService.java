package com.jason.chat.server.service;

import com.jason.chat.server.warehouse.WareHouse;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatService implements Runnable {

    // 每个客户的 socket
    private Socket clientSocket;

    // 需要一个存储所有客户端的仓库
    private static final WareHouse wareHouse = WareHouse.getInstance();


    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }


    @Override
    public void run() {
        try {
            PrintStream printToClient = new PrintStream(clientSocket.getOutputStream());

            Scanner scanner = new Scanner(clientSocket.getInputStream());
            String str = null;
            while (true) {
                if (scanner.hasNext()) {
                    str = scanner.next();

                    Pattern pattern = Pattern.compile("\r");    // 排除特殊符号
                    Matcher matcher = pattern.matcher(str);
                    str = matcher.replaceAll("");

                    // 如果以 userName 开头，则是要进行注册
                    if (str.startsWith("userName:")) {
                        // 拿到要注册的名字
                        String userName = str.split(":")[1];
                        // 调用注册方法
                        if (userRegist(userName)) {
                            // System.out.println(userName + "用户注册成功");
                            printToClient.println( userName + ", 你已注册成功");
                            System.out.println("一共注册："+wareHouse.getMapClient().size() + "人 : " + wareHouse.getMapClient());
                        } else {
                            printToClient.println("警告:一个客户端只能注册一个用户！");
                        }
                    }
                    //
                    else {
                        // 首先进行判断该用户是否已经注册
                        if (checkRegist() == false) {
                            printToClient.println("请先注册");
                        }
                        // 已经注册过
                        else {
                            // 如果以 G: 开头，则是要进行群聊
                            if (str.startsWith("G:")) {
                                String message = str.split(":")[1];
                                groupChat(message);
                            }
                            // 如果以 P: 开头，则是要进行私聊
                            else if (str.startsWith("P:")) {
                                String userName = str.split(":")[1];
                                String message = str.split(":")[2];
                                privateChat(userName, message);
                            }
                            // 如果 获取到的是 bye ，则说明用户要下线
                            else if (str.equals("bye")) {
                                System.out.println("用户 ：" + wareHouse.getMapClientUserName(clientSocket) + "已下线");
                                wareHouse.remove(clientSocket);
                                System.out.println("剩余在线人数："+wareHouse.getMapClient().size() + "人 : " + wareHouse.getMapClient());
                            }
                            else {
                                printToClient.println("输入格式有误");
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断用户是否注册
     * @return
     */
    private boolean checkRegist() {
        if(wareHouse.getMapClientUserName(clientSocket) == null)
            return false;
        return true;
    }

    /**
     * 用户注册方法
     *
     * @param userName
     * @return
     */
    private boolean userRegist(String userName) {
        // 先用 clientSocket 判断是否已经注册过
        Set<Map.Entry<Socket, String>> entrySet = wareHouse.getMapClient().entrySet();
        for (Map.Entry<Socket, String> entry : entrySet) {
            if (clientSocket.equals(entry.getKey())) {
                return false;
            }
        }
        wareHouse.putMapClient(clientSocket, userName);
        return true;
    }

    /**
     * 群聊，将消息给每个人发一次
     * @param message
     */
    private void groupChat(String message) {
        // 先拿到发消息人的用户名
        String userName = wareHouse.getMapClientUserName(clientSocket);

        Set<Map.Entry<Socket, String>> entrySet = wareHouse.getMapClient().entrySet();
        // 遍历仓库里面的每个用户，给他们发消息
        for (Map.Entry<Socket, String> entry : entrySet) {
            Socket socket = entry.getKey();
            if(!socket.equals(clientSocket)) {
                try {
                    PrintStream printStream = new PrintStream(socket.getOutputStream());
                    printStream.println("[ "+clientSocket + wareHouse.getMapClientUserName(clientSocket) + " ] 发送群消息 ：" + message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 用户间私聊
     * @param userName
     * @param message
     */
    private void privateChat(String userName, String message) {
        // 找到对应的 userName, 给他发消息
        Set<Map.Entry<Socket, String>> entrySet = wareHouse.getMapClient().entrySet();
        // 遍历仓库里面的每个用户，给他们发消息
        for (Map.Entry<Socket, String> entry : entrySet) {
            String user = entry.getValue();
            Socket socket = entry.getKey();
            if (user.equals(userName)) {
                try {
                    PrintStream printStream = new PrintStream(socket.getOutputStream());
                    printStream.println("[ "+clientSocket + wareHouse.getMapClientUserName(clientSocket)+ " ] 对我说："+ message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
