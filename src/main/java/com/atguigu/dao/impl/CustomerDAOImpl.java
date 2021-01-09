package com.atguigu.dao.impl;

import com.atguigu.dao.BaseDao;
import com.atguigu.dao.CustomerDAO;
import com.atguigu.entity.Customer;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

public class CustomerDAOImpl extends BaseDao implements CustomerDAO{
    @Override
    public List<Customer> queryAllCustomer(Connection conn) {
        String sql = "select id,name,email,birth from customers";
        List<Customer> customerList = queryWithTransaction(conn,sql,Customer.class);
        return customerList;
    }

    @Override
    public void insertOneCustomer(Connection conn, Customer customer) {
        String sql = "insert into customers(name,email,birth) values(?,?,?)";
        updateByTransaction(conn,sql,customer.getName(),customer.getEmail(),customer.getBirth());
    }

    @Override
    public void deleteOneCustomerById(Connection conn, Customer customer) {
        String sql = "delete from customers where id = ?";
        updateByTransaction(conn,sql,customer.getId());

    }

    @Override
    public void updateOneCutomerById(Connection conn, Customer customer) {
        String sql = "update customers set name = ?,email = ?,birth = ?,photo = ? where id = ?";
        updateByTransaction(conn,sql,customer.getName(),customer.getEmail(),customer.getBirth(),customer.getPhoto(),customer.getId());
    }

    @Override
    public Customer selectCustomerById(Connection conn, int id) {
        String sql = "select id,name,email,birth from customers where id = ?";
        List<Customer> customerList = queryWithTransaction(conn, sql, Customer.class, id);
        if(customerList != null && customerList.size() > 0) return customerList.get(0);
        return null;
    }

    @Override
    public long getCount(Connection conn) {
        String sql = "select count(*) from customers";
        long count = getCount(conn, sql);
        return count;
    }

    @Override
    public Date getMaxDate(Connection conn) {
        String sql = "select max(birth) from customers";
        Date date = getValue(conn, sql);
        System.out.println(date);
        return date;
    }
}
