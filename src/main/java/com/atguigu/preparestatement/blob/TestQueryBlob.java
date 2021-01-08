package com.atguigu.preparestatement.blob;

import com.atguigu.entity.Customer;
import com.atguigu.utils.JDBCUtil;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

public class TestQueryBlob {
    @Test
    public void testResourcePath(){
        String resource = TestQueryBlob.class.getClassLoader().getResource("").getPath();
        System.out.println(resource);
    }
    @Test
    public void testQueryBlob(){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        InputStream bs = null;
        FileOutputStream fos = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "select id,name,email,birth,photo from customers where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setObject(1,26);
            rs = ps.executeQuery();
            while(rs.next()){
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String email = rs.getString("email");
            Date birth = rs.getDate("birth");
            Customer customer = new Customer();
            customer.setId(id);
            customer.setName(name);
            customer.setEmail(email);
            customer.setBirth(birth);
            System.out.println(customer);
            Blob photo = rs.getBlob("photo");
            bs = photo.getBinaryStream();
            String resource = TestQueryBlob.class.getClassLoader().getResource("").getPath();
            fos = new FileOutputStream(new File(resource+"img/2.jpg"));
            byte[] bytes = new byte[1024];
            int len;
            while((len = bs.read(bytes)) != -1){
                fos.write(bytes,0,len);
            }

            }
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }  finally {
            try {
                if(fos != null)
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(bs != null){
                bs.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        JDBCUtil.closeResource(conn,ps,rs);
        }

    }

}
