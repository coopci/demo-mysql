package tcpkeepalive;

import com.mysql.jdbc.SocketFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DemoTcpKeepAlive {

    // tcpkeepalive.DemoTcpKeepAlive
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
                    "jdbc:mysql://localhost/?socketFactory=tcpkeepalive.MySocketFactory&tcpKeepAlive=false", 
                    //"jdbc:mysql://localhost:3308/?", 
                    "", "");
            
            if (con instanceof com.mysql.cj.jdbc.ConnectionImpl) {
                com.mysql.cj.jdbc.ConnectionImpl ci = (com.mysql.cj.jdbc.ConnectionImpl) con;
            }
            Statement stmt = con.createStatement();
            
            System.out.println("Run `sudo iptables -A INPUT -p tcp --destination-port 3306 -j DROP`, you will see communication link failure.");
            ResultSet rs = stmt.executeQuery("select SLEEP(100);");
            while (rs.next())
                System.out.println(rs.getInt(1) + "  ");
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
           // System.out.println(e);
        }
    }

}
