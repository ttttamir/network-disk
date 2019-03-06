package xgen.fs;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Util {

	/**
	 * 生成的MD5值
	 * 
	 * @param path
	 *            文件的绝对路径
	 * @return
	 */
	public static String fileMD5(String path) {
		String md5 = null;

		// 读取文件中的数据-->字节数组
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(path))) {
			byte[] buf = new byte[1024 * 8];
			int size;
			while (-1 != (size = in.read(buf))) {
				out.write(buf, 0, size);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] input = out.toByteArray();

		// 消息摘要
		try {
			// 16个字节 -128~127
			byte[] output = MessageDigest.getInstance("MD5").digest(input);
			// 十六进制数 32
			md5 = new BigInteger(1, output).toString(16);

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return md5;
	}
}
