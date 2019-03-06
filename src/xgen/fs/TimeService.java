package xgen.fs;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;

public class TimeService implements Runnable {

	Socket socket;

	public TimeService(Socket socket) {
		super();
		this.socket = socket;
	}

	@Override
	public void run() {
		String now = new Date().toLocaleString();
		System.out.println(Thread.currentThread().getName() + "处理" + socket.getPort());

		try (OutputStream out = socket.getOutputStream(); InputStream in = socket.getInputStream()) {

			// 先接收
			byte[] buf = new byte[2014];
			int size = in.read(buf);
			StringBuilder msg = new StringBuilder(new String(buf, 0, size));
			
			// 反转字符串内容
//			StringBuilder builder = new StringBuilder();
//			byte[] m = msg.getBytes();
//			for (int i = msg.length() - 1; i >= 0; i--) {
//				builder.append(String.valueOf((char) m[i]));
//			}
			String s = msg.reverse().toString();

			// 再发送
			out.write(s.getBytes());
			out.flush();
			out.write(now.getBytes());
			out.flush();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
