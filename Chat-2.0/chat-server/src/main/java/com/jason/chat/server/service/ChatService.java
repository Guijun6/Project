package com.jason.chat.server.service;

import com.jason.chat.server.dao.IChatInfoDao;
import com.jason.chat.server.dao.impl.ChatInfoDaoImpl;
import com.jason.chat.server.entity.ChatInfo;
import com.jason.chat.server.jdbcutil.JdbcUtil;
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

    // 获取一个JdbcUtil类的一个对象
    private final static JdbcUtil jdbcUtil = new JdbcUtil(
            "com.mysql.jdbc.Driver",
            "jdbc:mysql://127.0.0.1:3306/chat?user=root&password=root&charsetEncoding=utf-8&useSSL=false");

    private final static IChatInfoDao CHAT_INFO_DAO_IMPL = new ChatInfoDaoImpl(jdbcUtil);

    // 每个客户的 socket
    private Socket clientSocket;

    // 需要一个存储所有客户端的缓存仓库
     private static final WareHouse wareHouse = WareHouse.getInstance();


    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }


    @Override
    public void run() {
        try {
            PrintStream printToClient = new PrintStream(clientSocket.getOutputStream());

            Scanner scanner = new Scanner(clientSocket.getInputStream());
            boolean flag = false;
            String str = null;
            while (true) {
                if (scanner.hasNext()) {
                    str = scanner.next();
                    // register:lisi:123
                    // login:lisi:123
                    // P:lisi:hello
                    // G:hello

                    Pattern pattern = Pattern.compile("\r");    // 排除特殊符号
                    Matcher matcher = pattern.matcher(str);
                    str = matcher.replaceAll("");

                    // 如果以 register 开头，则是要进行注册
                    if (str.startsWith("register:")) {

                        String[] result = str.split(":");
                        if (result.length != 3) {
                            printToClient.println("注册格式输入有误");
                        } else {
                            // 拿到要注册的名字
                            String userName = result[1];
                            // 拿到要注册的密码
                            String password = result[2];
                            // 调用注册方法
                            if (userRegist(userName, password)) {
                                wareHouse.putMapClient(clientSocket, userName);
                                flag = true;
                                printToClient.println( userName + ", 你已注册成功");

                                System.out.println("一共注册：" + CHAT_INFO_DAO_IMPL.countAllNumber() + " 人。");
                                System.out.println("当前在线人数："+wareHouse.getMapClient().size() + "人 : " + wareHouse.getMapClient());

                            } else {
                                printToClient.println("警告: 该用户名已经被注册！");
                            }
                        }
                    }
                    // 如果以 login 开头，则是要登录
                    // login:lisi:123
                    else if (str.startsWith("login:")) {
                        String[] result = str.split(":");
                        if (result.length != 3) {
                            printToClient.println("登录格式输入有误");
                        } else {
                            String name = result[1];
                            String password = result[2];
                            // 首先进行判断该用户是否已经注册
                            if (checkRegist(name) == false) {
                                printToClient.println("您还没有注册，请先注册");
                            } else {
                                // 进行登录判定
                                // 1、判断用户名和密码是否正确
                                // 2、修改该用户表的 addr, port,
                                if(checkLogin(name, password)) {
                                    flag = true;
                                    wareHouse.putMapClient(clientSocket, name);
                                    printToClient.println("尊敬的"+ name + "用户，您你登录成功");
                                    System.out.println(name + "已上线，当前在线人数：" + wareHouse.getMapClient().size()+" 人。");
                                    // 给在线所有人发上线友情提示
                                    groupChat("上线啦，随时都可以撩我哦");
                                } else {
                                    flag = false;
                                    printToClient.println("输入的密码有误");
                                }
                            }
                        }
                    }
                    // 如果以 destroy 开头，则是要进行账户的注销
                    // destroy:userName:password
                    else if (str.startsWith("destroy:")) {
                        String[] result = str.split(":");
                        // 对参数进行校验
                        if (result.length != 3) {
                            printToClient.println("账户注销格式输入有误");
                        } else {
                            // 进行账户注销
                            String name = result[1];
                            String password = result[2];
                            if(checkRegist(name) == false) {
                                printToClient.println("该账户没有注册过");
                            } else if (checkLogin(name, password) == false) {
                                printToClient.println("输入的密码有误，注销失败");
                            } else {
                                // 进行注销
                                if(destroyUser(name))
                                    printToClient.println("注销成功");
                                else
                                    printToClient.println("不知道什么原因，注销失败");
                            }
                        }
                    }
                    // 走到这里说明不是注册，也不是登录，也不是注销
                    else {
                        // 首先进行判断该用户是否已经注册或者登录
                        if (flag == false) {
                            printToClient.println("请先注册或登录");
                        }
                        // 已经注册或登录过了
                        else {
                            // 如果以 G: 开头，则是要进行群聊
                            // G:hello
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
                            else if (str.equals("logout")) {
                                wareHouse.remove(clientSocket);
                                System.out.println("剩余在线人数：" + wareHouse.getMapClient().size() + "人。");
                            } else {
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
     * 判断用户是否注册过
     * @return 返回true 表示注册过，返回false 表示未注册
     */
    private boolean checkRegist(String userName) {
        ChatInfo result = CHAT_INFO_DAO_IMPL.queryChatInfoByName(userName);
        if(result.getName() == null)
            return false;
        return true;
    }
    /**
     * 用户注册方法
     * @param userName
     * @return 注册成功返回true, 失败返回false
     */
    private synchronized boolean userRegist(String userName, String password) {
        // 先利用查询，查询是否已经存在
        ChatInfo result = CHAT_INFO_DAO_IMPL.queryChatInfoByName(userName);
        if (result.getName() != null) {
            return false;
        }
        // 进行注册
        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setName(userName);
        chatInfo.setPassword(password);
        chatInfo.setLocalport(clientSocket.getLocalPort());
        chatInfo.setPort(clientSocket.getPort());
        chatInfo.setAddr(clientSocket.getLocalAddress().toString());
        CHAT_INFO_DAO_IMPL.insertChatInfo(chatInfo);
        return true;
    }
    /**
     * 用户登录密码检查
     * @param userName
     * @param password
     * @return
     */
    private boolean checkLogin(String userName, String password) {
        // 1、判断用户名和密码是否正确
        // 2、修改该用户表的 addr, port,
        ChatInfo chatInfo = CHAT_INFO_DAO_IMPL.queryChatInfoByName(userName);
        if (chatInfo.getName() == null) {
            return false;
        } else if (chatInfo.getPassword().equals(password)) {   // 判断密码是否正确
            // 要更新最近登录的时间
            if(CHAT_INFO_DAO_IMPL.updateChatInfo(userName, clientSocket.getLocalAddress().toString(), clientSocket.getPort()))
                return true;
        }
        return false;
    }
    /**
     * 群聊，将消息给每个人发一次
     * @param message
     */
    private void groupChat(String message) {

        Set<Map.Entry<Socket, String>> entrySet = wareHouse.getMapClient().entrySet();
        // 遍历仓库里面的每个用户，给他们发消息
        for (Map.Entry<Socket, String> entry : entrySet) {
            Socket socket = entry.getKey();
            if(!socket.equals(clientSocket)) {
                try {
                    PrintStream printStream = new PrintStream(socket.getOutputStream());
                    printStream.println("[ "+ wareHouse.getMapClientUserName(clientSocket) + " ] 发送群消息 ：" + message);
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
                    printStream.println("[ "+ wareHouse.getMapClientUserName(clientSocket)+ " ] 对我说："+ message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 用户注销
     * @param name
     * @return 注销成功返回true, 失败返回false
     */
    private boolean destroyUser(String name) {
        int result = CHAT_INFO_DAO_IMPL.deleteChatInfo(name);
        if(result == 1)
            return true;
        return false;
    }
}

