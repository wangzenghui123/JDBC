package com.atguigu.pool;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import com.sun.xml.internal.stream.buffer.sax.SAXBufferCreator;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.List;

public class C3P0Test {
    @Test
    public void testConnection() throws Exception {
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        cpds.setDriverClass( "com.mysql.jdbc.Driver" ); //loads the jdbc driver
        cpds.setJdbcUrl( "jdbc:mysql://localhost:3306/test" );
        cpds.setUser("root");
        cpds.setPassword("123456");
        cpds.setInitialPoolSize(10);
        Connection conn = cpds.getConnection();
        System.out.println(conn);
        //关闭连接池
        DataSources.destroy(cpds);
    }

    @Test
    public void testConnection1() throws Exception{
        ComboPooledDataSource cpds = new ComboPooledDataSource("helloApp");
        Connection conn = cpds.getConnection();
        System.out.println(conn);
    }

    @Test
    public void testXML() throws SAXException, DocumentException {
        SAXReader saxReader = new SAXReader();
        InputStream is = C3P0Test.class.getClassLoader().getResourceAsStream("c3p0-config.xml");
        Document read = saxReader.read(is);
        Element rootElement = read.getRootElement();
        String name = rootElement.getName();
        System.out.println(name);
        List elements = rootElement.elements();
        System.out.println(elements.size());
    }
}
