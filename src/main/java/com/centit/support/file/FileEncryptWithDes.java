package com.centit.support.file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;

import com.centit.support.security.DESSecurityUtils;

public abstract class FileEncryptWithDes {

	/**
	 * 文件file进行加密并保存目标文件destFile中
	 * 
	 * @param file
	 *            要加密的文件 如c:/test/srcFile.txt
	 * @param destFile
	 *            加密后存放的文件名 如c:/加密后文件.txt
	 */
	public static void encrypt(String sourceFileName, String diminationFileName,String keyValue) throws Exception {
		InputStream is = new FileInputStream(sourceFileName);
		OutputStream out = new FileOutputStream(diminationFileName);
		CipherInputStream cis = new CipherInputStream(is, DESSecurityUtils.createEncryptCipher(keyValue));
		byte[] buffer = new byte[1024];
		int r;
		while ((r = cis.read(buffer)) > 0) {
			out.write(buffer, 0, r);
		}
		cis.close();
		is.close();
		out.close();
	}

	/**
	 * 文件采用DES算法解密文件
	 * 
	 * @param file
	 *            已加密的文件 如c:/加密后文件.txt * @param destFile 解密后存放的文件名 如c:/
	 *            test/解密后文件.txt
	 */
	public void decrypt(String sourceFileName, String diminationFileName,String keyValue) throws Exception {
		InputStream is = new FileInputStream(sourceFileName);
		OutputStream out = new FileOutputStream(diminationFileName);
		CipherOutputStream cos = new CipherOutputStream(out, DESSecurityUtils.createDencryptCipher(keyValue));
		byte[] buffer = new byte[1024];
		int r;
		while ((r = is.read(buffer)) >= 0) {
			cos.write(buffer, 0, r);
		}
		cos.close();
		out.close();
		is.close();
	}

}
