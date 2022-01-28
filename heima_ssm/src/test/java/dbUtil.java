

import org.junit.Test;

import java.sql.*;

public class dbUtil {
    @Test
    public void getConnection() throws SQLException {
        Connection conn=null;
        ResultSet rs;
        try {
            String url="jdbc:oracle:thin:@127.0.0.1:1521:orcl";
            String user="ssm";
            String password="itcast";

            Class.forName("oracle.jdbc.driver.OracleDriver");//加载数据驱动
            conn = DriverManager.getConnection(url, user, password);// 连接数据库

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("加载数据库驱动失败");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("连接数据库失败");
        }finally {
            System.out.println("测试完毕");;
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM product");
            rs = preparedStatement.executeQuery();
            while(rs.next()){
                System.out.println(rs.getString("ID"));
            }
        }

    }

    public static void close(Connection conn, PreparedStatement ps, ResultSet rs){
        try {
            if(rs!=null){
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if(ps!=null){
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if(conn!=null){
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}

