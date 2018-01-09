package com.funnel.svc.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class FileUtil {

	/**
	 * 把文件中的内容分行读出
	 * 
	 * @param fileName
	 *            文件名
	 * @return 一行一行的内容
	 */
	public static String[] getContentByLine(String fileName) {
		return getContentByLine(fileName, null);
	}

	public static String[] getContentByLine(String fileName, String encoding) {
		try {
			return getContentByLine(new FileInputStream(fileName), encoding);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return new String[] {};
	}

	public static String[] getContentByLine(InputStream stream, String encoding) {
		ArrayList<String> list = new ArrayList<String>();
		try {
			BufferedReader bfReader = null;
			if (encoding != null) {
				bfReader = new BufferedReader(new InputStreamReader(stream,
						encoding));
			} else {
				bfReader = new BufferedReader(new InputStreamReader(stream));
			}

			while (bfReader.ready()) {
				list.add(bfReader.readLine());
			}
			stream.close();
			bfReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return list.toArray(new String[] {});
	}

	/**
	 * 把一个文件夹下的文件移到另外一个文件夹下
	 * 
	 * @param sourceDic
	 *            源文件夹
	 * @param targetDic
	 *            目标文件夹
	 */
	public static void moveFiles(String sourceDic, String targetDic) {
		String[] files = new File(sourceDic).list();
		for (String file : files) {
			File oldFile = new File(sourceDic + "//" + file);
			File newFile = new File(targetDic + "//" + file);
			if (oldFile.exists()) {
				oldFile.renameTo(newFile);
			}
		}
	}

	/**
	 * 删除指定的文件
	 * 
	 * @param path
	 *            文件路径
	 */
	public static void deleteFile(String path) {
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 * 创建文件夹
	 * 
	 * @param path
	 *            文件夹路径
	 */
	public static void createFolder(String path) {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	/**
	 * 复制文件
	 * 
	 * @param sourcePath
	 *            源文件
	 * @param targetPath
	 *            目标文件
	 * @throws IOException
	 */
	public static void copyFile(String sourcePath, String targetPath)
			throws IOException {
		FileUtils.copyFile(new File(sourcePath), new File(targetPath));
	}

	public static boolean clearFolder(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		if (tempList == null || tempList.length <= 0) {
			return true;
		}
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				clearFolder(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}

	public static boolean clearFolder(String path, String exceptFileName) {
		File file = new File(path);
		if (!file.exists()) {
			return true;
		}
		if (!file.isDirectory()) {
			return true;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile() && !temp.getName().equals(exceptFileName)) {
				if (!temp.delete()) {
					return false;
				}
			}
			if (temp.isDirectory()) {
				if (!clearFolder(path + "/" + tempList[i])) {
					// 先删除文件夹里面的文件
					return false;
				}
				if (!delFolder(path + "/" + tempList[i])) {// 再删除空文件夹
					return false;
				}
			}
		}

		return true;
	}

	public static boolean delFolder(String folderPath) {
		try {
			clearFolder(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	public static void createFile(String filename) {
		File file = new File(filename);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void createFile(String dic, String filename) {
		File file = new File(dic, filename);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static boolean isFileExisted(String dic, String fileName) {
		File file = new File(dic, fileName);
		return file.exists();
	}

	public static int getAllFilesCount(String folder) {
		return getAllFileNames(folder).length;
	}

	public static String[] getAllFileNames(String folder) {
		LinkedList<File> list = new LinkedList<File>();
		LinkedHashSet<String> files = new LinkedHashSet<String>();
		File dir = new File(folder);

		File file[] = dir.listFiles();
		if (file == null) {
			return files.toArray(new String[] {});
		}
		for (int i = 0; i < file.length; i++) {
			if (file[i].isDirectory()) {
				list.add(file[i]);
			} else {
				files.add(file[i].getAbsolutePath());
			}
		}
		File tmp;
		while (!list.isEmpty()) {
			tmp = list.removeFirst();
			if (tmp.isDirectory()) {
				file = tmp.listFiles();
				if (file == null) {
					continue;
				}
				for (int i = 0; i < file.length; i++) {
					if (file[i].isDirectory())
						list.add(file[i]);
					else
						files.add(file[i].getAbsolutePath());
				}
			} else {
				files.add(tmp.getAbsolutePath());
			}
		}

		return files.toArray(new String[] {});
	}

	public static boolean deleteFile(String dic, String fileName) {
		File file = new File(dic, fileName);
		if (file.exists()) {
			return file.delete();
		}

		return true;
	}

	public static boolean copyFiles(String sourceDic, String targetDic) {
		try {
			FileUtils.copyDirectory(new File(sourceDic), new File(targetDic));
		} catch (IOException e) {
			return false;
		}

		return true;
	}

	public static String[] getFilesUnderFolder(String folder) {
		List<String> list = new ArrayList<String>();
		File f = new File(folder);
		File[] fs = f.listFiles();
		if (fs != null) {
			for (File file : fs) {
				if (!file.isDirectory()) {
					list.add(file.getAbsolutePath());
				}
			}
		}

		return list.toArray(new String[] {});
	}

	public static String[] getFoldersUnderFolder(String folder) {
		List<String> list = new ArrayList<String>();
		File f = new File(folder);
		File[] fs = f.listFiles();
		if (fs != null) {
			for (File file : fs) {
				if (file.isDirectory()
						&& !file.getAbsolutePath().equals(f.getAbsolutePath())) {
					list.add(file.getAbsolutePath());
				}
			}
		}

		return list.toArray(new String[] {});
	}

	public static long getLastModifiedTime(String folder) {
		File f = new File(folder);
		File[] fileArray = f.listFiles();
		long lastmodified = -1;
		if (fileArray != null) {
			for (int i = 0; i < fileArray.length; i++) {
				if (lastmodified < fileArray[i].lastModified()) {
					lastmodified = fileArray[i].lastModified();
				}
			}
		}
		return lastmodified;
	}

	public static void output(Hashtable<String, Integer> list, int condition,
			String filePath) {
		output(list, condition, false, filePath);
	}

	public static void output(Hashtable<String, Integer> list, int condition,
			boolean append, String filePath) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(
					filePath), append));
			Iterator<String> it = list.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				if (list.get(key) > condition) {
					bw.write(key + "," + list.get(key));
					bw.write("\n");
				}
			}
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getFileContent(String fileName, String encoding) {
		return StringUtils.getStringFromStrings(
				getContentByLine(fileName, encoding), "\n").trim();
	}

	public static void output(String[] list, String filePath, String encoding) {
		try {
			BufferedWriter bw = getWriter(filePath, encoding);
			if (bw != null) {
				if (list.length > 0) {
					bw.write(list[0]);
					for (int i = 1; i < list.length; i++) {
						if (list[i] != null) {
							bw.write("\n");
							bw.write(list[i]);
						}
					}
				}
				bw.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static BufferedWriter getWriter(String filePath, String encoding) {
		try {
			return new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(new File(filePath), false), encoding));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static BufferedReader getReader(String filePath, String encoding) {
		try {
			return new BufferedReader(new InputStreamReader(
					new FileInputStream(filePath), encoding));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}
}
