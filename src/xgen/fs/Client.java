package xgen.fs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * 客户端
 * 
 * @author muodo
 *
 */
public class Client {

	// 要连接的服务器的端口
	int port = 9000;

	// 服务器的地址
	InetAddress address;

	// 客户端 TCP 套接字
	Socket socket;

	public Client() {
		try {
			address = InetAddress.getByName("127.0.0.1");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		try {
			socket = new Socket(address, port);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void start() {

		try {
			Scanner scanner = new Scanner(System.in);
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			
			// 先发送
//			 String msg = "hello";
			out.write(scanner.nextLine().getBytes());
			scanner.close();
			out.flush();
			
			// 再接收
			Thread.sleep(2000);
			byte[] buf = new byte[128];
			int size = in.read(buf);
			String now = new String(buf, 0, size);
			System.out.println("当前时间：" + now);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void main(String[] args) {
		
		Client client = new Client();
		Client client2 = new Client();
		Client client3 = new Client();
		
		client.start();
		client2.start();
		client3.start();
	}
	

}
