package com.atguigu.preparestatement.crud;

import com.atguigu.entity.Customer;
import org.junit.Test;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PrepareStatementQueryTest {
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
    public void testQuery() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Customer> list = null;
        try {
            connection = DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("user"), properties.getProperty("password"));
            String sql = "select id,name,email,birth from customers ";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            list = new ArrayList<>();
            while (resultSet.next()){
                int id = Integer.parseInt(resultSet.getString("id"));
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                Date birth = resultSet.getDate("birth");
                Customer customer = new Customer();
                customer.setId(id);
                customer.setName(name);
                customer.setEmail(email);
                customer.setBirth(birth);
                list.add(customer);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if(resultSet != null){
                    resultSet.close();
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                if(preparedStatement != null){
                    preparedStatement.close();
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                if(connection != null) {
                    connection.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        System.out.println(list);
    }
}
