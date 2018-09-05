package com.jason.chat.server.warehouse;


import java.net.Socket;
import java.util.HashMap;
import java.util.Map;


public class WareHouse {
    // 单例
    private final static WareHouse wareHouse = new WareHouse();

    // 存放所有注册了的用户信息的Map
    Map<Socket, String> mapClient = new HashMap<>();

    private WareHouse() {
    }

    public Map<Socket, String> getMapClient() {
        return mapClient;
    }

    public void putMapClient(Socket socket, String userName) {
        mapClient.put(socket,userName);
    }

    public String getMapClientUserName(Socket socket) {

        // 根据传进来的socket找到对应Map中对应的socket，然后返回
        String userName = mapClient.get(socket);

        return userName;
    }

    public void remove(Socket socket) {
        mapClient.remove(socket);
    }

    public static WareHouse getInstance() {
        return wareHouse;
    }
}
