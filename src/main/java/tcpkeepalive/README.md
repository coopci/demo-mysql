A demo shows how tcp keepalive effects mysql jdbc driver.

Before run this demo, set tcp keepalive shorter:
```
sudo sysctl net.ipv4.tcp_keepalive_intvl=1
sudo sysctl net.ipv4.tcp_keepalive_probes=2
sudo sysctl net.ipv4.tcp_keepalive_time=1
```

Run this demo with 
```
java -cp target/demo-mysql-0.0.1-SNAPSHOT-jar-with-dependencies.jar tcpkeepalive.DemoTcpKeepAlive
```

Then block traffics between mysqld and this demo:
```
sudo iptables -A INPUT -p tcp --destination-port 3306 -j DROP
```

You will see communication link failure.
