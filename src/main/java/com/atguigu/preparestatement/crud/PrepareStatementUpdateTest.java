package com.atguigu.preparestatement.crud;

import org.junit.Test;

import javax.security.sasl.SaslServer;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

public class PrepareStatementUpdateTest {
    //1.获取连接 Connection                 获取连接
    public static Properties properties = null;
    static {
        InputStream resourceAsStream = ClassLoader.getSystemClassLoader().getResourceAsStream("db.properties");
        properties = new Properties();
        //加载驱动
        try {
            properties.load(resourceAsStream);
            Class.forName(properties.getProperty("class"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testUpdate(){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("user"), properties.getProperty("password"));
            String sql = "update customers set name = ? where id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,"张思");
            preparedStatement.setString(2,"2");
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if(preparedStatement != null){
                    preparedStatement.close();
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

    @Test
    public void testDelete(){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean execute = false;
        try {
            connection = DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("user"), properties.getProperty("password"));
            String sql = "delete from customers where id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,"19");
            execute = preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {

                if(preparedStatement != null){
                    preparedStatement.close();
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

        System.out.println(execute);

    }
    @Test
    public void testInsert(){
        Connection connection = null;
        PreparedStatement ps = null;
        try {

            //DriverManager获取连接
            connection = DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("user"), properties.getProperty("password"));

            //2.用Connection获取PrepareStatement    写sql
            //先准备好sql语句
            String sql = "insert into customers(name,email,birth) values(?,?,?)";
            ps = connection.prepareStatement(sql);
            ps.setString(1,"哪吒");
            ps.setString(2,"necha@email.com");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parse = simpleDateFormat.parse("1000-10-11");
            ps.setDate(3,new java.sql.Date(parse.getTime()));
            boolean execute = ps.execute();
            //System.out.println(execute);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
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
    @Test
    public void testTxt() throws IOException {
        InputStream resourceAsStream = ClassLoader.getSystemClassLoader().getResourceAsStream("1.txt");
        byte[] bytes = new byte[2048];
        int read = resourceAsStream.read(bytes);
        System.out.println(Arrays.toString(bytes));
        System.out.println(read);

    }
}
