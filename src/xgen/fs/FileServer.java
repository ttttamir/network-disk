package xgen.fs;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 文件服务器（Server) 软件：提供服务（对（客户端）请求提供响应）的程序
 * 
 * @author muodo
 *
 */
public class FileServer {

	// 地址
	// IP
	InetAddress address;

	// Port 端口
	int port = 9000;

	// 套接字
	// Socket socket; 客户端套接字

	ServerSocket serverSocket;

	ExecutorService pool;

	HashSet<String> fileList = new HashSet<>();
	private static File path = new File("D:\files");

	public FileServer() {
		try {
			address = InetAddress.getByName("127.0.0.1");

			// 另外一种
			// for(InetAddress ads : address.getAllByName("www.tamll.com")){
			// System.out.println(ads.getHostAddress());
			// }
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		// 创建服务端套接字
		// 在当前主机
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		FileServer server = new FileServer();
		server.start();

	}

	private void start() {
		pool = Executors.newCachedThreadPool();

		System.out.println("服务器启动...");

		// 加载文件列表
		loadFileList();

		// 接收客户端的请求
		while (true) {
			try {
				Socket socket = serverSocket.accept();

				// pool.execute(new TimeService(socket));
				pool.execute(new UploadService(socket, fileList));

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void loadFileList() {
		if (path.list() == null) {
			String[] files = null;
		} else {
			String[] files = path.list();
			for (String file : files) {
				fileList.add(file.substring(0, file.lastIndexOf(".")));
			}
			for (String f : fileList) {
				System.out.println(f);
			}
		}
	}
}
