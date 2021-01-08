package com.atguigu.preparestatement.blob;

import com.atguigu.utils.JDBCUtil;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;

//插入Blob数据
//如果插入一个图片大小超过1M，会插入失败
//有一个Packet 的限制
//my.ini文件中设置 max_allowed_packet=16M
//重新启动mysql

public class TestInsertBlob {
    @Test
    public void testBlob() throws Exception {
        String sql = "insert into customers(name,email,birth,photo) values(?,?,?,?)";
        Date date = new Date(new java.util.Date().getTime());
        String filePath = TestInsertBlob.class.getClassLoader().getResource("img/2.jpg").getFile();
        boolean b = insertFile(sql, "张十二", "zhangsher@qq.com", date, URLDecoder.decode(filePath,"utf-8"));
    }
    @Test
    public void testDate(){
        Date date = new Date(new java.util.Date().getTime());
        System.out.println(date);
        String resource = TestInsertBlob.class.getClassLoader().getResource("报名照片.jpg").getFile();
        System.out.println(resource);
    }
    @Test
    public void changeName(){
        String filePath = "C:\\Users\\wzh\\Desktop\\pp";
        File file = new File(filePath);
        String path = file.getParent();
        System.out.println(path);

        boolean b = file.renameTo(new File(path+"\\1"));
        System.out.println(b);
    }
    @Test
    public void testFile(){
        String filePath = "C:/Users/wzh/Desktop/1";
        File file = new File(filePath);
        System.out.println(file.listFiles());
        System.out.println(file.exists());
        rename(file);
    }
    //获得文件全名才能改
    //递归修改文件名
    public void rename(File file){
        File[] files = file.listFiles();
        if(files != null && files.length > 0){
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()){

                    files[i].renameTo(new File(files[i].getPath()+"p"));
                    files[i] = new File(files[i].getPath()+"p");
                    rename(files[i]);
                };
            }
        };


    }
    public boolean insertFile(String sql,Object ...args) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        int count = 0;
        try {
            conn = JDBCUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setObject(1,args[0]);
            ps.setObject(2,args[1]);
            ps.setObject(3,args[2]);
            FileInputStream fis = new FileInputStream(new File((String) args[3]));
            ps.setBlob(4,fis);
            count = ps.executeUpdate();
            return count > 0 ? true:false;

        } catch (Exception throwables) {
            throwables.printStackTrace();
        } finally {

        JDBCUtil.closeResource(conn,ps,null);
        }
        return false;
    }
}
