package xgen.fs;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class FileClient {

	/**
	 * 端口号
	 */
	int port;

	/**
	 * 地址
	 */
	InetAddress address;

	/**
	 * TCP套接字
	 */
	Socket socket;

	public void start() {
		// 套接字；
		port = 9000;
		try {
			// address = InetAddress.getLocalHost();
			socket = new Socket("127.0.0.1", port);

			System.out.print("指定要上传的文件：");
			Scanner sc = new Scanner(System.in);
			String path = sc.nextLine();
			sc.close();

			// 从套接字获得输出流，发送/上传文件
			OutputStream out = socket.getOutputStream();
			InputStream in = socket.getInputStream();

			// 将文件内容转换成散列值
			String hash = Util.fileMD5(path);
			out.write(hash.getBytes());
			out.flush();
			
			// 接收
			int result = in.read();
			if (result == 0) {
				finish();
			} else {
				// 继续上传
				upload(path, out);
			}

			out.close();
			socket.close();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void upload(String path, OutputStream out) throws IOException, FileNotFoundException {
		// 取得文件的后缀
		int index = path.lastIndexOf(File.separator);
		String name = path.substring(index + 1);
		String postfix = name.substring(name.lastIndexOf(".") + 1);

		// 将文件后缀发送给服务端
		out.write(postfix.getBytes());
		out.flush();

		// 使用输入流，读取本地文件
		BufferedInputStream fin = new BufferedInputStream(new FileInputStream(path));

		byte[] buf = new byte[1024 * 8];
		int size;
		while (-1 != (size = fin.read(buf))) {
			out.write(buf, 0, size);
		}
		out.flush();
		fin.close();
		System.out.println(path + " 上传完毕");
	}

	private void finish() {
		System.out.println("秒传");
	}

}
