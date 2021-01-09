package com.atguigu.dao;

import com.atguigu.dao.impl.CustomerDAOImpl;
import com.atguigu.entity.Customer;
import com.atguigu.utils.JDBCUtil;
import junit.framework.TestCase;

import java.io.File;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.Date;
import java.util.List;

public class CustomerDAOImplTest extends TestCase {
    private CustomerDAOImpl customerDAO = new CustomerDAOImpl();

    public void testQueryAllCustomer() throws Exception {
        Connection conn = JDBCUtil.getConnection();
        List<Customer> customers = customerDAO.queryAllCustomer(conn);
        System.out.println(customers);

    }

    public void testInsertOneCustomer() throws Exception{
        Connection conn = JDBCUtil.getConnection();
        Customer customer = new Customer();
        customer.setName("张十二");
        customer.setEmail("zhangshier@qq.com");
        customer.setBirth(new Date(new java.util.Date().getTime()));
        customerDAO.insertOneCustomer(conn,customer);
    }

    public void testDeleteOneCustomerById() throws Exception{
        Connection conn = JDBCUtil.getConnection();
        Customer customer = new Customer();
        customer.setId(22);
        customerDAO.deleteOneCustomerById(conn,customer);
    }

    public void testUpdateOneCutomerById() throws Exception{
        Connection conn = JDBCUtil.getConnection();
        Customer customer = new Customer();
        customer.setName("张八八");
        customer.setBirth(new Date(new java.util.Date().getTime()));
        customer.setEmail("zhangbaba@qq.com");
        String resource = CustomerDAOImplTest.class.getClassLoader().getResource("img/2.jpg").getFile();
        resource = URLDecoder.decode(resource,"utf-8");
        File file = new File(resource);
        customer.setPhoto(file);
        customer.setId(23);
        customerDAO.updateOneCutomerById(conn,customer);
    }

    public void testSelectCustomerById() throws Exception{
        Connection conn = JDBCUtil.getConnection();
        Customer customer = customerDAO.selectCustomerById(conn, 1);
        System.out.println(customer);
    }

    public void testGetCount() throws Exception{
        Connection conn = JDBCUtil.getConnection();
        long count = customerDAO.getCount(conn);
        System.out.println(count);
    }

    public void testGetMaxDate() throws Exception{
        Connection conn = JDBCUtil.getConnection();
        Date maxDate = customerDAO.getMaxDate(conn);
        System.out.println(maxDate);
    }
}