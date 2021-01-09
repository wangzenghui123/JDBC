package com.atguigu.preparestatement.blob;

import com.atguigu.utils.JDBCUtil;
import org.junit.Test;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *使用PreparedStatement實現批量操作
 * update、delete本身就具有批量操作的效果
 * 此時的批量操作，主要指批量插入，使用prepareStatement實現批量操作
 *
 * 方式一：使用Statement
 * Connection conn = JDBCUtils.getConnection();
 * Statement st = conn.createStatement();
 * for(int i = 1;i < 20000;i++){
 *     String sql = "insert into goods(name) values('name_"+i+"')";
 * }
 *
 * 方式二：使用PrepareStatement
 */

public class TestBatchInsert {
    @Test
    public void testBatchInsert(){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "insert into goods(name) values(?)";
            ps = conn.prepareStatement(sql);
            long currentTimeMillis = System.currentTimeMillis();
            for (int i = 0; i < 2000; i++) {
                ps.setObject(1,"name_"+i);
                ps.executeUpdate();
                //System.out.println(i);
            }
            long l = System.currentTimeMillis();
            System.out.println((float)(l-currentTimeMillis)/1000);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {

        JDBCUtil.closeResource(conn,ps,null);
        }

    }

    //方式二与数据库有过多的IO交互，效率低，每提交一次数据就有一次IO

    //方式三：每隔500次执行一次IO,减少与数据库的交互
    //使用Batch
    //mysql默认不支持批处理的，需要加参数

    public static class BatchInsert extends Thread{
        @Override
        public void run() {

            Connection conn = null;
            PreparedStatement ps = null;
            try {
                conn = JDBCUtil.getConnection();
                String sql = "insert into goods(name) values(?)";
                ps = conn.prepareStatement(sql);
                long start = System.currentTimeMillis();
                for (int i = 0; i < 1000000; i++) {
                    ps.setObject(1,"name_"+i);
                    ps.addBatch();
                    if(i % 50000 == 0 || i == 999999){
                        ps.executeBatch();
                        ps.clearBatch();
                    }
                }
                long end = System.currentTimeMillis();
                System.out.println((double)(end-start)/1000);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {

                JDBCUtil.closeResource(conn,ps,null);
            }
        }



    }
    public void testBatchInsert01(){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "insert into goods(name) values(?)";
            ps = conn.prepareStatement(sql);
            long start = System.currentTimeMillis();
            for (int i = 0; i < 1000000; i++) {
                ps.setObject(1,"name_"+i);
                ps.addBatch();
                if(i % 50000 == 0 || i == 999999){
                    ps.executeBatch();
                    ps.clearBatch();
                }
            }
            long end = System.currentTimeMillis();
            System.out.println((double)(end-start)/1000);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {

            JDBCUtil.closeResource(conn,ps,null);
        }
    }

    //方式四:设置不允许自动提交
    //setAutoCommit()失效，我是用mysql可视化软件创建的数据库，创建数据表的时候数据库引擎默认用的是MyISAM不支持事务,要改成InnoDB才行
    @Test
    public void testBatchInsert02(){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            long start = System.currentTimeMillis();
            conn = JDBCUtil.getConnection();
            conn.setAutoCommit(false);
            String sql = "insert into goods(name) values(?)";
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < 1000000; i++) {
                ps.setObject(1,"name_"+i);
                ps.addBatch();
                if(i % 500 == 0){
                    ps.executeBatch();
                    ps.clearBatch();
                }
            }
            conn.commit();
            long end = System.currentTimeMillis();
            System.out.println((double)(end - start)/1000);
        } catch (Exception throwables) {
            try {
                if(conn != null) {
                    conn.rollback();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
        JDBCUtil.closeResource(conn,ps,null);
        }
    }

    public static void main(String[] args) {
        new BatchInsert().start();
        new BatchInsert().start();
    }
    //多线程批量插入
    @Test
    public void multiThreadInsert() throws InterruptedException {
        BatchInsert batchInsert = new BatchInsert();
        BatchInsert batchInsert1 = new BatchInsert();
        batchInsert.start();
        batchInsert1.start();
        //主线程需要睡眠，否则当主线程结束时，会调用System.exit(0),其他线程都会直接结束
        // https://blog.csdn.net/weixin_42779370/article/details/108157969
        Thread.sleep(1000000);


    }
}


