package com.atguigu.dao;

import com.atguigu.entity.Customer;
import com.atguigu.utils.JDBCUtil;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseDao {
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
    public void updateByTransaction(Connection conn,String sql,Object ...args){
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                String className = args[i].getClass().getName();
                System.out.println(className);
                if(className == "java.io.File"){
                    File file = (File) args[i];
                    FileInputStream fis = new FileInputStream(file);
                    ps.setBlob(i + 1,fis);
                    continue;
                }
                ps.setObject(i + 1,args[i]);
            }
            ps.executeUpdate();
        } catch (Exception throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtil.closeResource(null,ps,null);

        }
    }
    public <T> T getValue(Connection conn,String sql,Object ...args){
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1,args[i]);
            }
            rs = ps.executeQuery();
            if(rs.next()){
                return (T) rs.getObject(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtil.closeResource(null,ps,rs);
        }
        return null;
    }
    public long getCount(Connection conn,String sql){
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if(rs.next()){
                return rs.getLong(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtil.closeResource(null,ps,rs);
        }
        return 0;
    }
}
