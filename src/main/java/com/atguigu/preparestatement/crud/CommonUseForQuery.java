package com.atguigu.preparestatement.crud;

import com.atguigu.entity.Customer;
import com.atguigu.utils.JDBCUtil;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class CommonUseForQuery {
    @Test
    public void test() throws Exception {
        String sql = "select id,name,email from customers where id = ?";
        List<Customer> customers = testQuery(Customer.class, sql,1);
        System.out.println(customers);
    }

    public <T> List<T> testQuery(Class<T> clazz,String sql, Object... args) throws Exception{
        Connection conn = null;
        PreparedStatement ps = null;
        List<T> list = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getConnection();
            ps = conn.prepareStatement(sql);
            list = new ArrayList<>();
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1,args[i]);
            }
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            while(rs.next()){
                T t = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    String columnLabel = rsmd.getColumnLabel(i + 1);
                    Object columnValue = rs.getObject(i + 1);
                    Field declaredField = clazz.getDeclaredField(columnLabel);
                    declaredField.setAccessible(true);
                    declaredField.set(t,columnValue);
                }
                list.add(t);
            }
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }  finally {
            JDBCUtil.closeResource(conn,ps,rs);
        }

       return  list;
    }
}
