package tcpkeepalive;

import com.mysql.cj.conf.PropertySet;
import com.mysql.cj.protocol.ServerSession;
import com.mysql.cj.protocol.SocketConnection;
import com.mysql.cj.protocol.StandardSocketFactory;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;

public class MySocketFactory extends StandardSocketFactory {

    @Override
    public <T extends Closeable> T connect(String host, int portNumber, PropertySet props, int loginTimeout) throws IOException {
        // TODO Auto-generated method stub
        T conn = super.connect(host, portNumber, props, loginTimeout);
        if (conn instanceof Socket) {
            Socket socket = (Socket) conn;
            boolean keepAlive = socket.getKeepAlive();
            // socket.setKeepAlive(false);
            return conn;
            // Unfortunately since TCP connections are managed on the OS level, Java does not support configuring timeouts on a per-socket level such as in java.net.Socket. I have found some attempts3 to use Java Native Interface (JNI) to create Java sockets that call native code to configure these options, but none appear to have widespread community adoption or support.
        }
        return conn;
    }
    @Override
    public <T extends Closeable> T performTlsHandshake(SocketConnection socketConnection, ServerSession serverSession) throws IOException {
        return super.performTlsHandshake(socketConnection, serverSession);
    }


}
