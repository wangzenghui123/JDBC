import com.atguigu.entity.Customer;
import com.atguigu.utils.JDBCUtils2;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Class.forName()
 * DriverManager.getConnection()
 * PrepareStatement
 * ResultSet
 * 关闭资源
 */
public class test {
    public static void  main(String[] args) {
        FileReader reader = null;
        FileWriter writer = null;
        try {
            File a = new File("1.txt");
            File b = new File("2.txt");
            reader = new FileReader(a);
            writer = new FileWriter(b);
            char[] c = new char[3];
            int mark = reader.read(c);
            while(mark != -1){
                writer.write(c,0,mark);
                mark = reader.read(c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(writer != null)
                    writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(reader != null){
                    reader.close();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
    @Test
    public void test1(){
        /**
         * Class.forName()
         * DriverManager.getConnection()
         * PrepareStatement
         * ResultSet
         * 关闭资源
         */
            Connection conn = null;
            PreparedStatement ps =null;
            ResultSet rs = null;
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?serverTimezone=GMT%2B8&rewriteBatchedStatements=true","root","123456");
                String sql = "select * from user";
                ps = conn.prepareStatement(sql);
                //conn.commit();
                rs = ps.executeQuery();
                List<Customer> customerList = new ArrayList<>();
                while(rs.next()){
                    Customer customer = new Customer();
                    int id = rs.getInt(1);
                    String name = rs.getString(2);
                    customer.setId(id);
                    customer.setName(name);
                    customerList.add(customer);
                }
                System.out.println(customerList);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                if(rs != null){
                    try {
                        rs.close();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
                if(ps != null){
                    try {
                        ps.close();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
                if(conn != null){
                    try {
                        conn.close();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }


    }
    @Test
    public void test2(){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs= null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?serverTimezone=GMT%2B8&rewriteBatchedStatements=true","root","123456");
            String sql = "select * from user";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            List<Customer> list = new ArrayList<Customer>();
            while (rs.next()){
                Customer customer = new Customer();
                int id = rs.getInt(1);
                String name = rs.getString(2);
                customer.setId(id);
                customer.setName(name);
                list.add(customer);
            }
            System.out.println(list);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            if(rs != null){
                try {
                    rs.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            if(ps != null){
                try {
                    ps.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }

    }
    @Test
    public void test3(){
        Connection conn1 = JDBCUtils2.getConnection();
        Connection conn2 = JDBCUtils2.getConnection();
        System.out.println(conn1 == conn2);
        JDBCUtils2.closeResources(conn1,null,null);

    }
    @Test
    public void test4(){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils2.getConnection();
            String sql = "select * from user where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setObject(1,1);
            rs = ps.executeQuery();
            List<Customer> list = new ArrayList<>();
            while(rs.next()){
                Customer customer = new Customer();
                int id = rs.getInt(1);
                String name = rs.getString(2);
                customer.setId(id);
                customer.setName(name);
                list.add(customer);
            }
            System.out.println(list);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils2.closeResources(conn,ps,rs);
        }
    }
    @Test
    public void test5(){
        String sql = "select id,name from user where id < ? or id > ?";
        List<Customer> list = test8(Customer.class,sql,2,4);
        System.out.println(list);

    }

    public static <T> List<T> test6(Class<T> clazz,String sql,Object ...args){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<T> list = null;
        try {
            list = new ArrayList<>();
            conn = JDBCUtils2.getConnection();
            ps = conn.prepareStatement(sql);
            for(int i = 0; i < args.length;i++){
                ps.setObject(i + 1,args[i]);
            }
            rs = ps.executeQuery();
            while(rs.next()){
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            T t = clazz.newInstance();
                for(int i = 0;i < columnCount;i++){
                    String columnName = metaData.getColumnName(i+1);
                    Field field = clazz.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(t,rs.getObject(i+1));
                }
                list.add(t);
            }
            return list;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils2.closeResources(conn,ps,rs);
        }
        return list;
    }

    public static <T> List<T> test7(Class<T> clazz,String sql,Object ...args){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<T> list = null;
        try {
            list = new ArrayList<>();
            conn = JDBCUtils2.getConnection();
            ps = conn.prepareStatement(sql);
            for(int i = 0;i < args.length;i++){
                ps.setObject(i+1,args[i]);
            }
            rs = ps.executeQuery();
            while(rs.next()){
                T t = clazz.newInstance();
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                for(int i = 0 ;i < columnCount;i++){
                    String columnName = metaData.getColumnName(i + 1);
                    Field declaredField = clazz.getDeclaredField(columnName);
                    declaredField.setAccessible(true);
                    declaredField.set(t,rs.getObject(i+1));
                }
                list.add(t);
            }
            return list;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils2.closeResources(conn,ps,rs);
        }
        return list;
    }

    public static <T> List<T> test8(Class<T> clazz,String sql,Object ...args){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<T> list = null;
        try {
            list = new ArrayList<>();
            conn = JDBCUtils2.getConnection();
            ps = conn.prepareStatement(sql);
            for(int i = 0; i < args.length;i++){
                ps.setObject(i+1,args[i]);
            }
            rs = ps.executeQuery();
            while (rs.next()){
                T t = clazz.newInstance();
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                for(int i = 0;i < columnCount;i++){
                    String columnName = metaData.getColumnName(i + 1);
                    Field declaredField = clazz.getDeclaredField(columnName);
                    declaredField.setAccessible(true);
                    declaredField.set(t,rs.getObject(i+1));
                }
                list.add(t);

            }
            return list;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils2.closeResources(conn,ps,rs);
        }
        return list;
    }
}
