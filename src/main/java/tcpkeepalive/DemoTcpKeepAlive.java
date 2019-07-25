package tcpkeepalive;

import com.mysql.jdbc.SocketFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DemoTcpKeepAlive {

    
    public static void main(String[] args) {
        
        /**
         *  sysctl net.inet.tcp.keepidle=100
            sysctl net.inet.tcp.keepintvl=100
            sysctl net.inet.tcp.keepinit=100
            sysctl net.inet.tcp.keepcnt=3
         * 
         * */
        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3308/?socketFactory=tcpkeepalive.MySocketFactory&tcpKeepAlive=false", 
                    //"jdbc:mysql://localhost:3308/?", 
                    "root", "123456");
            
            if (con instanceof com.mysql.cj.jdbc.ConnectionImpl) {
                com.mysql.cj.jdbc.ConnectionImpl ci = (com.mysql.cj.jdbc.ConnectionImpl) con;
            }
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select SLEEP(1);");
            while (rs.next())
                System.out.println(rs.getInt(1) + "  ");
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
           // System.out.println(e);
        }
    }

}
