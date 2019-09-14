package updatesamerow;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class UpdateSameRow {

//	CREATE TABLE `tranx` (
//			   `id` int(11) NOT NULL AUTO_INCREMENT,
//			   `status` varchar(45) NOT NULL DEFAULT 'waiting',
//			   `amount` decimal(10,2) NOT NULL DEFAULT 0.00,
//			   PRIMARY KEY (`id`)
//			 ) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8

	static CyclicBarrier barrier = new CyclicBarrier(2);
	public static void updateRow() throws SQLException, InterruptedException, BrokenBarrierException {
		Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost/test", 
                //"jdbc:mysql://localhost:3308/?", 
                "root", "123qwe");
		try {
			con.setAutoCommit(false);
			PreparedStatement ps = con.prepareStatement("update tranx set `status` = 'approved' where id=1 and status='waiting'");
			barrier.await(); // �ŵ������֤�������̻߳Ტ������������֤������������Ҳ�����������update amount��
			int updateCount = ps.executeUpdate();
			// barrier.await(); ��������ᵼ��hangס�� 
			if (updateCount == 0) {
				System.out.println("Lost contention.");
				con.rollback();
				return;
			}
			// mysql�ڲ���֤������ͬʱ�ж���߳�ִ����δ��룬���ֻ��һ���߳����ߵ����
			// Ҳ����˵���ֻ��һ���̵߳õ���updateCount�ܵ���1��
			
			PreparedStatement ps2 = con.prepareStatement("update tranx set amount = amount + 100 where id=1 ");
			ps2.executeUpdate();
			// barrier.await();
			con.commit();
			System.out.println("Executed update.");
        } finally {
        	con.close();
        }
		
	}
	public static void main(String[] args) throws InterruptedException {
	        
	        /**
	         *  sysctl net.inet.tcp.keepidle=100
	            sysctl net.inet.tcp.keepintvl=100
	            sysctl net.inet.tcp.keepinit=100
	            sysctl net.inet.tcp.keepcnt=3
	         * 
	         * */
		
		ExecutorService executor = Executors.newFixedThreadPool(2);
	        
		executor.submit(new Runnable() {
			public void run() {
				try {
					updateRow();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (BrokenBarrierException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}});
		
		executor.submit(new Runnable() {
			public void run() {
				try {
					updateRow();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (BrokenBarrierException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}});
		executor.shutdown();
		executor.awaitTermination(1000, TimeUnit.SECONDS);
		
	 }
}
