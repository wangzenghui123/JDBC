package com.atguigu.utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JDBCUtil {
    //1.获取连接 Connection                 获取连接
    public static Properties properties = null;
    static {
        InputStream resourceAsStream = ClassLoader.getSystemClassLoader().getResourceAsStream("db.properties");
        properties = new Properties();
        //加载驱动
        try {
            Class.forName(properties.getProperty("class"));
            properties.load(resourceAsStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static Connection getConnection() throws SQLException {
       Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("user"), properties.getProperty("password"));
        return connection;
    }
    public static void closeResource(Connection connection, Statement ps){
        try {
            if(ps != null){
                ps.close();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            if(connection != null){
                connection.close();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
