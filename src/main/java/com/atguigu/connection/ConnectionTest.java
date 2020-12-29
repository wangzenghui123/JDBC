package com.atguigu.connection;

import com.mysql.cj.jdbc.Driver;
import org.junit.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class ConnectionTest {

    @Test
    public void testConnection1() throws Exception {
        Driver driver = new com.mysql.cj.jdbc.Driver();
        Properties properties = new Properties();
        properties.setProperty("user","root");
        properties.setProperty("password","123456");
        String url = "jdbc:mysql://localhost:3306/test?serverTimezone=GMT%2B8";
        Connection connect = driver.connect(url, properties);
        System.out.println(connect);
    }

    //移除第三方jar包
    @Test
    public void testConnection2() throws Exception{
        Class clazz = Class.forName("com.mysql.cj.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();
        Properties properties = new Properties();
        properties.setProperty("user","root");
        properties.setProperty("password","123456");
        String url = "jdbc:mysql://localhost:3306/test?serverTimezone=GMT%2B8";
        Connection connect = driver.connect(url, properties);
        System.out.println(connect);
    }

    //用DriverManager获取Driver
    //把Driver,Connection交给DriverManager管理
    @Test
    public void testConnection3() throws Exception{
        Class clazz = Class.forName("com.mysql.cj.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();
        String url = "jdbc:mysql://localhost:3306/test?serverTimezone=GMT%2B8";
        String user = "root";
        String password = "123456";
        DriverManager.registerDriver(driver);
        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);
    }

    //优化代码
    //在mysql的Driver实现类中，静态代码块中DriverManager注册了驱动
    //shift Enter 另起一行
    @Test
    public void testConnection4() throws Exception{
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/test?serverTimezone=GMT%2B8";
        String user = "root";
        String password = "123456";
        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);
    }

    //db.properties应该放在resources目录下。这是maven工程
    @Test
    public void testConnection5() throws Exception{
        InputStream resourceAsStream = ConnectionTest.class.getClassLoader().getResourceAsStream("db.properties");
        Properties properties = new Properties();
        properties.load(resourceAsStream);
        Class.forName(properties.getProperty("class"));
        Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("user"), properties.getProperty("password"));
        System.out.println(connection);

    }

}
