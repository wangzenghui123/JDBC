package com.atguigu.utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;

public class JDBCUtil1 {
    private static ComboPooledDataSource cpds = new ComboPooledDataSource("helloApp");
    public static Connection getConnection() throws  Exception{
        Connection conn = cpds.getConnection();
        return conn;
    }
}
