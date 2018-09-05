package com.jason.chat.server.jdbcutil;

import java.sql.*;

public class JdbcUtil {

    private String driverClass;
    private String url;

    /**
     * 构造函数初始化加载驱动的参数
     * @param driverClass
     * @param url
     */
    public JdbcUtil(String driverClass, String url) {
        this.driverClass = driverClass;
        this.url = url;

        try {
            Class.forName(this.driverClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 连接数据库
     * @return
     */
    public Connection connection() {
        try {
            return DriverManager.getConnection(this.url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 预命令准备
     * @param connection
     * @param sql
     * @return
     */
    public PreparedStatement preparedStatement(Connection connection, String sql) {
        if (connection != null) {
            try {
                return connection.prepareStatement(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取查询结果
     * @param preparedStatement
     * @return
     */
    public ResultSet query(PreparedStatement preparedStatement) {
        if (preparedStatement != null) {
            try {
                return preparedStatement.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取更新结果
     * @param preparedStatement
     * @return
     */
    public int update(PreparedStatement preparedStatement) {
        if (preparedStatement != null) {
            try {
                return preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    /**
     * 关闭连接
     * @param connection
     * @param preparedStatement
     * @param resultSet
     */
    public void close(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
