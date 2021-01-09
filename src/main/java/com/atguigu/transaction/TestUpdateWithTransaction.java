package com.atguigu.transaction;

import com.atguigu.utils.JDBCUtil;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

//确保事务使用的是同一个连接Connection
//关闭资源，现在通用代码中关闭prepareStatement
//
public class TestUpdateWithTransaction {
    @Test
    public void testUpdateWithTransaction(){
        Connection conn = null;
        try {
            conn = JDBCUtil.getConnection();
            //查看事务的隔离级别
            System.out.println(conn.getTransactionIsolation());
            //修改事务的隔离级别
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            //执行update操作时会自动提交，要事先设置不用自动提交
            conn.setAutoCommit(false);
            String update1 = "update acount set banlance = banlance - 100 where name = ?";
            updateByTransaction(conn,update1,"AA");
            String update2 = "update acount set banlance = banlance + 100 where name = ?";
            updateByTransaction(conn,update2,"BB");
            //测试出现异常时是否回滚
            //int a = 1 / 0;
            conn.commit();
        } catch (Exception throwables) {
            throwables.printStackTrace();
            try {
                if(conn != null){
                    conn.rollback();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            //如果用数据库连接池，setAutoCommit(true)
            //方便别人使用，还原成默认值
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

    public void updateByTransaction(Connection conn,String sql,Object ...args){
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1,args[i]);
            }
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtil.closeResource(null,ps,null);

        }
    }
}
