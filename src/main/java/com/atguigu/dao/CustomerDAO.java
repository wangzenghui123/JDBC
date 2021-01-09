package com.atguigu.dao;

import com.atguigu.entity.Customer;
import sun.plugin2.main.client.DisconnectedExecutionContext;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

public interface CustomerDAO {
    List<Customer> queryAllCustomer(Connection conn);
    void insertOneCustomer(Connection conn,Customer customer);
    void deleteOneCustomerById(Connection conn,Customer customer);
    void updateOneCutomerById(Connection conn,Customer customer);
    Customer selectCustomerById(Connection conn,int id);
    long getCount(Connection conn);
    Date getMaxDate(Connection conn);
}
