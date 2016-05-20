package com.centit.support.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileSystemOpt {

	/**
	 * 获取指定目录下特定后缀名的文件
	 * 
	 * @param allList
	 *            指定目录
	 * @param endName
	 *            指定以“”结尾的文件
	 * @return 得到的文件列表
	 */
	public static List<File> findFilesByExt(String dir, String extName) {
		File dirFile = new File(dir);
		File[] fileArray = dirFile.listFiles();

		List<File> resFiles = new ArrayList<File>();
		// 如果传进来一个以文件作为对象的allList 返回0
		if (null == fileArray) {
			return resFiles;
		}

		// 偏历目录下的文件
		for (int i = 0; i < fileArray.length; i++) {
			// 如果是个目录
			if (fileArray[i].isFile()) {
				// 如果是以“”结尾的文件
				if (fileArray[i].getName().endsWith(extName)) {
					resFiles.add(fileArray[i]);
				}
			}
		}
		return resFiles;

	}

	/**
	 * 在本文件夹下查找
	 * 
	 * @param s
	 *            String 文件名
	 * @return File[] 找到的文件
	 */
	public static List<File> findFiles(String s) {
		return findFiles("./", s);
	}

	/**
	 * 获取文件 可以根据正则表达式查找
	 * 
	 * @param dir
	 *            String 文件夹名称
	 * @param s
	 *            String 查找文件名，可带*.?进行模糊查询
	 * @return File[] 找到的文件
	 */
	public static List<File> findFiles(String dir, String s) {
		// 开始的文件夹
		File file = new File(dir);

		s = s.replace('.', '#');
		s = s.replaceAll("#", "\\\\.");
		s = s.replace('*', '#');
		s = s.replaceAll("#", ".*");
		s = s.replace('?', '#');
		s = s.replaceAll("#", ".?");
		s = "^" + s + "$";

		Pattern p = Pattern.compile(s);
		return filePattern(file, p);

	}

	/**
	 * 根据通配符查找文件
	 * 
	 * @param file
	 *            File 起始文件夹
	 * @param p
	 *            Pattern 匹配类型
	 * @return ArrayList 其文件夹下的文件夹
	 */

	private static List<File> filePattern(File file, Pattern p) {
		if (file == null) {
			return null;
		} else if (file.isFile()) {
			Matcher fMatcher = p.matcher(file.getName());
			if (fMatcher.matches()) {
				ArrayList<File> list = new ArrayList<File>();
				list.add(file);
				return list;
			}
		} else if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (files != null && files.length > 0) {
				List<File> list = new ArrayList<File>();
				for (int i = 0; i < files.length; i++) {
					List<File> rlist = filePattern(files[i], p);
					if (rlist != null) {
						list.addAll(rlist);
					}
				}
				return list;
			}
		}
		return null;
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param sFilePath
	 * @return
	 */
	public static boolean existFile(String sFilePath) {
		File f = new File(sFilePath);
		return f.exists();
	}

	/**
	 * 创建目录
	 * 
	 * @param sDirPath
	 */
	public static void createDirect(String sDirPath) {
		File f = new File(sDirPath);
		if (f.exists())
			return;
		if (f.isDirectory())
			f.mkdir();
		else
			new File(f.getParent()).mkdir();
	}

	/**
	 * 获取文件名
	 * 
	 * @param filePath
	 * @return
	 */
	public static String extractFullFileName(String filePath) {
		if (filePath == null)
			return "";
		int nPos;
		int nl = filePath.length();
		nPos = nl;
		while (nPos > 0 && filePath.charAt(nPos - 1) != '\\' && filePath.charAt(nPos - 1) != '/')
			nPos--;

		return filePath.substring(nPos);
	}

	/**
	 * 获取文件名
	 * 
	 * @param filePath
	 * @return
	 */
	public static String extractFileName(String filePath) {
		if (filePath == null)
			return "";
		String fullFileName = extractFullFileName(filePath);

		int nl = fullFileName.length();
		int nPos = nl;
		while (nPos > 0 && fullFileName.charAt(nPos - 1) != '.')
			nPos--;
		if (nPos > 1)
			return fullFileName.substring(0, nPos - 1);
		else
			return fullFileName;
	}

	/**
	 * 使用NIO进行快速的文件拷贝
	 * 
	 * @param in
	 * @param out
	 * @throws IOException
	 */
	public static void fileCopy(File in, File out) throws IOException {
		FileInputStream fileInputStream = new FileInputStream(in);
		FileChannel inChannel = fileInputStream.getChannel();
		FileOutputStream fileOutputStream = new FileOutputStream(out);
		FileChannel outChannel = fileOutputStream.getChannel();
		try {
			// inChannel.transferTo(0, inChannel.size(), outChannel); //
			// original -- apparently has trouble copying large files on Windows

			// magic number for Windows, 64Mb - 32Kb)
			int maxCount = (64 * 1024 * 1024) - (32 * 1024);
			long size = inChannel.size();
			long position = 0;
			while (position < size) {
				position += inChannel.transferTo(position, maxCount, outChannel);
			}
		} finally {
			if (inChannel != null) {
				inChannel.close();
			}
			if (outChannel != null) {
				outChannel.close();
			}

			fileInputStream.close();
			fileOutputStream.close();
		}
	}

	public static boolean deleteDirect(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			// 递归删除目录中的子目录下
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDirect(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// 目录此时为空，可以删除
		return dir.delete();
	}

	/**
	 * 创建临时文件
	 * 
	 * @param inputStream
	 * @param name
	 *            文件名
	 * @param ext
	 *            扩展名
	 * @param tmpDirFile
	 *            临时文件夹目录
	 * @return
	 * @throws IOException
	 */
	public static File createTmpFile(InputStream inputStream, String name, String ext, File tmpDirFile)
			throws IOException {
		File tmpFile;
		if (tmpDirFile == null) {
			tmpFile = File.createTempFile(name, '.' + ext);
		} else {
			tmpFile = File.createTempFile(name, '.' + ext, tmpDirFile);
		}
		tmpFile.deleteOnExit();
		FileOutputStream fos = new FileOutputStream(tmpFile);
		int read = 0;
		byte[] bytes = new byte[1024 * 100];
		while ((read = inputStream.read(bytes)) != -1) {
			fos.write(bytes, 0, read);
		}
		fos.flush();
		fos.close();
		return tmpFile;

	}

	/**
	 * 创建临时文件
	 * 
	 * @param inputStream
	 * @param name
	 *            文件名
	 * @param ext
	 *            扩展名
	 * @return
	 * @throws IOException
	 */
	public static File createTmpFile(InputStream inputStream, String name, String ext) throws IOException {
		return createTmpFile(inputStream, name, ext, null);
	}

	/**
	 * 创建文件
	 * @param inputStream
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static boolean createFile(InputStream inputStream, String filePath) throws IOException {

		FileOutputStream fos = new FileOutputStream(filePath);
		int read = 0;
		byte[] bytes = new byte[1024 * 100];
		while ((read = inputStream.read(bytes)) != -1) {
			fos.write(bytes, 0, read);
		}
		fos.flush();
		fos.close();
		return true;
	}
}
