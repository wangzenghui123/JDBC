package com.atguigu.preparestatement.crud;

import com.atguigu.entity.Customer;
import com.atguigu.utils.JDBCUtil;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerForQuery {

    @Test
    public void test() throws Exception {
        testQuery("select id,name,email from customers");
    }

    public void testQuery(String sql,Object... args) throws Exception {
        Connection connection = null;
        List<Object> list = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = JDBCUtil.getConnection();
            list = new ArrayList<>();
            ps = connection.prepareStatement(sql);
            for (int i = 0;i<args.length;i++) {
                ps.setObject(i+1,args[i]);
            }
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            while(rs.next()){
                Customer customer = new Customer();
                for (int i = 0; i < columnCount; i++) {
                    Object columnName = rsmd.getColumnName(i+1);
                    Object columnValue = rs.getObject(i+1);
                    Field declaredField = Customer.class.getDeclaredField((String) columnName);
                    declaredField.setAccessible(true);
                    declaredField.set(customer,columnValue);
                }
                list.add(customer);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeResource(connection,ps,rs);
            System.out.println(list);
        }



    }
}
