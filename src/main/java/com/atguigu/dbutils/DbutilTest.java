package com.atguigu.dbutils;

import com.atguigu.entity.Customer;
import com.atguigu.utils.JDBCUtil;
import com.atguigu.utils.JDBCUtils;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DbutilTest {
    @Test
    public void testInsert(){
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection();
            String sql = "insert into customers(name,email,birth)values(?,?,?)";
            int updateCount = runner.update(conn, sql, "张十三", "zhangshisan@qq.com", "2021-01-01");
            System.out.println(updateCount);
            JDBCUtil.closeResource(conn,null,null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    @Test
    public void testQuery() throws Exception{
        QueryRunner runner = new QueryRunner();
        Connection conn = JDBCUtils.getConnection();
        String sql = "select id,name,email,birth from customers where id = ?";
        BeanHandler<Customer> beanHandler = new BeanHandler<>(Customer.class);
        Customer customer = runner.query(conn, sql, beanHandler, 1);
        System.out.println(customer);
    }

    @Test
    public void testQuery1(){
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection();
            String sql = "select id,name,email,birth from customers where id < ?";
            BeanListHandler<Customer> beanHandler = new BeanListHandler<>(Customer.class);
            List<Customer> customerList = runner.query(conn, sql, beanHandler, 4);
            for (int i = 0; i < customerList.size(); i++) {
                System.out.println(customerList.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeResource(conn,null,null);
        }
        
    }
    @Test
    public void testQuery2(){
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection();
            String sql = "select id,name,email,birth from customers where id < ?";
            MapListHandler mapListHandler = new MapListHandler();
            List<Map<String, Object>> mapList = runner.query(conn, sql, mapListHandler, 5);
            mapList.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeResource(conn,null,null);
        }
    }


    //ScalarHandler:查询特殊值
    @Test
    public void testQuery3(){
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select count(*) from customers";
            ScalarHandler handler = new ScalarHandler();
            QueryRunner runner = new QueryRunner();
            Long count = (Long) runner.query(conn, sql, handler);
            System.out.println(count);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeResource(conn,null,null);
        }
    }

    @Test
    public void testQuery4(){
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select id,name,email,birth from customers where id < ?";
            ResultSetHandler<List<Customer>> handler = new ResultSetHandler<List<Customer>>() {
                @Override
                public List<Customer> handle(ResultSet resultSet) {
                    List<Customer> list = null;
                    try {
                        ResultSetMetaData metaData = resultSet.getMetaData();
                        int columnCount = metaData.getColumnCount();
                        list = new ArrayList<>();
                        Class clazz = Customer.class;
                        while (resultSet.next()) {
                            Object o = clazz.newInstance();
                            for (int i = 0; i < columnCount; i++) {
                                Object columnValue = resultSet.getObject(i + 1);
                                String columnClassName = metaData.getColumnName(i + 1);
                                Field declaredField = clazz.getDeclaredField(columnClassName);
                                declaredField.setAccessible(true);
                                declaredField.set(o,columnValue);
                            }
                            list.add((Customer) o);
                        }
                        return list;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return list;
                }
            };
            QueryRunner runner = new QueryRunner();
            List<Customer> customerList = runner.query(conn, sql, handler, 5);
            customerList.forEach(System.out::println);
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtil.closeResource(conn,null,null);
        }
    }

    //自定义ResultSetHandler
    @Test
    public void testQuery5(){
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select max(birth) from customers";
            ScalarHandler handler = new ScalarHandler();
            QueryRunner runner = new QueryRunner();
            Date date = (Date) runner.query(conn, sql, handler);
            System.out.println(date);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }
}
