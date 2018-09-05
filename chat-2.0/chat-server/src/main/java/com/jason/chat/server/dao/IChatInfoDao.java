package com.jason.chat.server.dao;

import com.jason.chat.server.entity.ChatInfo;

import java.util.List;

public interface IChatInfoDao {

    /**
     * 插入一个 ChatInfo 对象
     * @param chatInfo
     * @return
     */
    int insertChatInfo(ChatInfo chatInfo);

    /**
     * 通过用户名进行查询
     * @param name
     * @return
     */
    ChatInfo queryChatInfoByName(String name);

    /**
     * 根据用户名更新 登陆地址和端口号
     * @param name
     * @param addr
     * @param port
     * @return
     */
    boolean updateChatInfo(String name, String addr, int port);

    /**
     * 查询所有结果
     * @return
     */
    List<ChatInfo> queryChatInfoAll();

    /**
     * 删除对应 ChatInfo
     * @param name
     * @return
     */
    int deleteChatInfo(String name);

    /**
     * 统计所有注册人数
     * @return
     */
    int countAllNumber();
}
