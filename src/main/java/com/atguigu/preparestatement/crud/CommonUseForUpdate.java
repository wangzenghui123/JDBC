package com.atguigu.preparestatement.crud;

import com.atguigu.utils.JDBCUtil;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Scanner;

// shift alt 上下 ：将当前行移动到上一行或下一行
// 另起一行 shift enter
public class CommonUseForUpdate {

    @Test
    public void test() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("输入姓名：");
        String name = scanner.next();
        System.out.println("输入邮箱：");
        String email = scanner.next();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(new java.util.Date().getTime());
        boolean updateResult = testUpdate("insert into customers(name,email,birth) values(?,?,?)", name, email, date);
        System.out.println(updateResult);
    }
    @Test
    public void testScanner(){
        Scanner scanner = new Scanner(System.in);
        String next = scanner.next();
        System.out.println(next);
    }
    @Test
    public void testDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date date = new java.util.Date();
        Date date1 = new Date(date.getTime());
        System.out.println(date);
        System.out.println(date1);
        String format = sdf.format(date1);
        System.out.println(format);
        String format1 = sdf.format(date);
        System.out.println(format1);
    }
    public boolean testUpdate(String sql,Object... args) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtil.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1,args[i]);
            }
            //查询操作有结果集，返回true,增删改操作返回false
            //并不代表执行成功或失败
            //不要用excute()方法 而是用excuteUpdate()

            //受影响的行数
            int count = ps.executeUpdate();
            return count > 0 ?true:false;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
        JDBCUtil.closeResource(conn,ps,null);

        }
        return false;
    }
}
