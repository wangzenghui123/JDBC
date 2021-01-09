package com.atguigu.transaction;

import com.atguigu.entity.Customer;
import com.atguigu.utils.JDBCUtil;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TestQueryWithTransaction {
    @Test
    public void testQueryWithTransaction(){
        Connection conn = null;
        try {
            conn = JDBCUtil.getConnection();
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            conn.setAutoCommit(false);
            String sql = "select name,email,birth from customers where id = ?";
            List<Customer> customerList = queryWithTransaction(conn, sql, Customer.class, 1);
            conn.commit();
            System.out.println(customerList);
        } catch (SQLException throwables) {
            try {
                if(conn != null){
                    conn.rollback();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throwables.printStackTrace();
        } finally {
            try {
                if(conn != null){
                  conn.setAutoCommit(true);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            JDBCUtil.closeResource(conn,null,null);
        }
    }
    public <T> List<T> queryWithTransaction(Connection conn,String sql,Class<T> clazz,Object... args){
        List<T> list = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            list = new ArrayList<>();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1,args[i]);
            }

            rs = ps.executeQuery();
            ResultSetMetaData md = rs.getMetaData();
            int columnCount = md.getColumnCount();
            while(rs.next()){
                T t = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = rs.getObject(i + 1);
                    String columnName = md.getColumnName(i + 1);
                    Field declaredField = clazz.getDeclaredField(columnName);
                    declaredField.setAccessible(true);
                    declaredField.set(t,columnValue);
                }
                list.add(t);
            }

            return list;
        } catch (Exception throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtil.closeResource(null,ps,rs);
        }
        return null;
    }
}
