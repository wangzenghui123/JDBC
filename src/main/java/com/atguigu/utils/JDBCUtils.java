package com.atguigu.utils;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

public class JDBCUtils {
    private static DataSource  dataSource= null;
    static{
        try{
        DruidDataSourceFactory  druidDataSourceFactory = new DruidDataSourceFactory();
        String file = ClassLoader.getSystemClassLoader().getResource("druid.properties").getFile();
        InputStream is = new FileInputStream(new File(file));
        Properties properties = new Properties();
        properties.load(is);
        dataSource = druidDataSourceFactory.createDataSource(properties);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws Exception{
        Connection conn = dataSource.getConnection();
        return conn;
    }
}
