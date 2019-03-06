package xgen.fs;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashSet;

/**
 * 处理文件上传的任务
 * 
 * @author muodo
 *
 */
public class UploadService implements Runnable {

	// 依赖
	Socket socket;
	HashSet<String> files;

	// 构造方法注入了依赖的资源
	public UploadService(Socket socket, HashSet<String> files) {
		this.socket = socket;
		this.files = files;
	}

	@Override
	public void run() {

		InputStream in;
		OutputStream out;

		try {
			in = socket.getInputStream();
			out = socket.getOutputStream();

			// 接收文件的 md5
			byte[] buffer = new byte[32];
			int s = in.read(buffer);
			String md5 = new String(buffer, 0, s);
			
			if (files.contains(md5)) {
				System.out.println("存在：" + md5);
				finish(out);
			} else {
				upload(in, out, md5);
				files.add(md5);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void upload(InputStream in, OutputStream out, String md5) throws IOException {
		// 回复客户端继续上传
		out.write(1);
		out.flush();

		int s;
		// 接收文件的后缀
		byte[] b = new byte[4];
		s = in.read(b);
		String postfix = new String(b, 0, s);

		// 命名文件
		String file = String.format("%s.%s", md5, postfix);

		// 保存路径
		File path = new File("D:/files");
		if (!path.exists())
			path.mkdirs();

		// 接收客户端网络发来的数据
		// 写入本地路径
		File filename = new File(path, file);

		try (BufferedOutputStream fout = new BufferedOutputStream(new FileOutputStream(filename))) {

			byte[] buf = new byte[1024 * 8];
			int size;
			while (-1 != (size = in.read(buf))) {
				fout.write(buf, 0, size);
			}
			fout.close();
			files.add(file);
			System.out.println("保存完毕");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void finish(OutputStream out) {
		try {
			out.write(0);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
