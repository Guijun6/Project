package com.jason.chat.server.dao.impl;

import com.jason.chat.server.entity.ChatInfo;
import com.jason.chat.server.jdbcutil.JdbcUtil;
import com.jason.chat.server.dao.IChatInfoDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChatInfoDaoImpl implements IChatInfoDao {

    private final JdbcUtil jdbcUtil;

    public ChatInfoDaoImpl(JdbcUtil jdbcUtil) {
        this.jdbcUtil = jdbcUtil;
    }

    @Override
    public int insertChatInfo(ChatInfo chatInfo) {
        if (chatInfo == null) {
            return -1;
        }
        // 取得连接
        Connection connection = this.jdbcUtil.connection();
        // 要执行的SQL语句
        String sql = "insert into user_register_info (name, password, signature, addr, port, localport) values (?,?,?,?,?,?)";
        // 建立预执行命令
        PreparedStatement preparedStatement = this.jdbcUtil.preparedStatement(connection, sql);
        // 给命令中的参数赋值
        try {
            preparedStatement.setString(1, chatInfo.getName());
            preparedStatement.setString(2, chatInfo.getPassword());
            preparedStatement.setString(3, chatInfo.getSignature());
            preparedStatement.setString(4, chatInfo.getAddr());
            preparedStatement.setInt(5, chatInfo.getPort());
            preparedStatement.setInt(6, chatInfo.getLocalport());

            return this.jdbcUtil.update(preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.jdbcUtil.close(connection, preparedStatement, null);
        }
        return -1;
    }

    @Override
    public ChatInfo queryChatInfoByName(String name) {

        if (name == null) {
            return null;
        }
        Connection connection = this.jdbcUtil.connection();
        String sql = "select * from user_register_info where name = ?";
        PreparedStatement preparedStatement = this.jdbcUtil.preparedStatement(connection, sql);
        ResultSet resultSet = null;
        ChatInfo result = null;
        try {
            preparedStatement.setString(1, name);
            resultSet = this.jdbcUtil.query(preparedStatement);
            result = this.handleResult(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.jdbcUtil.close(connection, preparedStatement, resultSet);
        }
        return result;
    }

    /**
     * 跟新登录信息
     * @param name
     * @param addr
     * @param port
     * @return
     */
    @Override
    public boolean updateChatInfo(String name, String addr, int port) {

        Connection connection = this.jdbcUtil.connection();
        String sql = "update user_register_info set addr = ?, port = ? where name = ?";
        PreparedStatement preparedStatement = this.jdbcUtil.preparedStatement(connection, sql);
        try {
            preparedStatement.setString(1, addr);
            preparedStatement.setInt(2, port);
            preparedStatement.setString(3, name);
            int result = this.jdbcUtil.update(preparedStatement);
            if (result == 1) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<ChatInfo> queryChatInfoAll() {

        Connection connection = this.jdbcUtil.connection();
        String sql = "select * from user_register_info";
        PreparedStatement preparedStatement = this.jdbcUtil.preparedStatement(connection, sql);

        ResultSet resultSet = this.jdbcUtil.query(preparedStatement);
        List<ChatInfo> result = this.handleResultAll(resultSet);
        if (result == null) {
            return new ArrayList<>();
        }
        this.jdbcUtil.close(connection, preparedStatement, resultSet);

        return result;
    }

    @Override
    public int deleteChatInfo(String name) {
        Connection connection = this.jdbcUtil.connection();
        String sql = "delete from user_register_info where name = ?";
        PreparedStatement preparedStatement = this.jdbcUtil.preparedStatement(connection, sql);
        try {
            preparedStatement.setString(1, name);
            return this.jdbcUtil.update(preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.jdbcUtil.close(connection, preparedStatement, null);
        }
        return -1;
    }

    @Override
    public int countAllNumber() {
        Connection connection = this.jdbcUtil.connection();
        String sql = "select count(*) from chat.user_register_info";
        PreparedStatement preparedStatement = this.jdbcUtil.preparedStatement(connection, sql);
        ResultSet result = this.jdbcUtil.query(preparedStatement);
        try {
            if(result.next()) {
                int count = result.getInt("count(*)");
                return count;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.jdbcUtil.close(connection, preparedStatement, result);
        }
        this.jdbcUtil.close(connection, preparedStatement, result);

        return -1;
    }

    /**
     * 将查询结果映射成 ChatInfo
     * @param resultSet
     * @return
     */
    private ChatInfo handleResult(ResultSet resultSet) {
        try {
            if (resultSet != null && resultSet.next()) {
                ChatInfo chatInfo = new ChatInfo();
                chatInfo.setName(resultSet.getString("name"));
                chatInfo.setPassword(resultSet.getString("password"));
                chatInfo.setSignature(resultSet.getString("signature"));
                chatInfo.setAddr(resultSet.getString("addr"));
                chatInfo.setPort(resultSet.getInt("port"));
                chatInfo.setLocalport(resultSet.getInt("localport"));
                chatInfo.setCreateTime(resultSet.getDate("create_time"));
                chatInfo.setModifyTime(resultSet.getTimestamp("modify_time"));
                return chatInfo;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ChatInfo();
    }

    private List<ChatInfo> handleResultAll(ResultSet resultSet) {
        List<ChatInfo> result = new ArrayList<>();
        try {
            while (resultSet != null && resultSet.next()) {
                ChatInfo chatInfo = new ChatInfo();
                chatInfo.setName(resultSet.getString("name"));
                chatInfo.setPassword(resultSet.getString("password"));
                chatInfo.setSignature(resultSet.getString("signature"));
                chatInfo.setAddr(resultSet.getString("addr"));
                chatInfo.setPort(resultSet.getInt("port"));
                chatInfo.setLocalport(resultSet.getInt("localport"));
                chatInfo.setCreateTime(resultSet.getDate("create_time"));
                chatInfo.setModifyTime(resultSet.getTimestamp("modify_time"));
                result.add(chatInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 进行了一波测试
     */
    public static void main(String[] args) {

        JdbcUtil jdbcUtil = new JdbcUtil(
                "com.mysql.jdbc.Driver",
                "jdbc:mysql://localhost:3306/chat?user=root&password=root&charsetEncoding=utf-8&useSSL=false");

        ChatInfoDaoImpl chatInfoMapper = new ChatInfoDaoImpl(jdbcUtil);

//        chatInfoMapper.deleteChatInfo("hehe");
//        ChatInfo chatInfo = new ChatInfo();
//        chatInfo.setName("hehe");
//        chatInfo.setPassword("123");
//        chatInfo.setAddr("127.0.0.1");
//        chatInfo.setPort(4816);
//        chatInfo.setLocalport(6666);
//        System.out.println(chatInfoMapper.insertChatInfo(chatInfo));
//        System.out.println(chatInfoMapper.queryChatInfoByName("jason"));
        System.out.println(chatInfoMapper.queryChatInfoByName("zhang").getName());

        System.out.println(chatInfoMapper.countAllNumber());
        System.out.println(chatInfoMapper.queryChatInfoAll());

    }
}
