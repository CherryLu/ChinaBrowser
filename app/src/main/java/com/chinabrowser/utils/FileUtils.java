/****************************
文件名:FileUtils.java

创建时间:2013-3-27
所在包:
作者:罗泽锋
说明:文件模块通用工具类
 ****************************/

package com.chinabrowser.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

import com.chinabrowser.APP;

import org.apache.http.util.EncodingUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

import static com.chinabrowser.utils.CommUtils.convert2int;


public class FileUtils {

	/**
	 * 获取该文件或者该文件夹的大小
	 * 
	 * @param path
	 * @return
	 */
	public static String getFileSize(String path) {
		// 传入文件路径
		File file = new File(path);
		double totalSize = 0;
		// 测试此文件是否存在
		if (file.exists()) {
			// 如果是文件夹
			// 这里只检测了文件夹中第一层 如果有需要 可以继续递归检测
			if (file.isDirectory()) {
				int size = 0;
				for (File zf : file.listFiles()) {
					if (zf.isDirectory())
						continue;
					size += zf.length();
				}
				totalSize = size / 1000d;
				// System.out.println("文件夹 "+file.getName()+" Size:
				// "+(size/1024f)+"kb");
			} else {
				totalSize = file.length() / 1000d;
				// System.out.println(file.getName()+" Size:
				// "+(file.length()/1024f)+"kb");
			}
			// 如果文件不存在
		} else {
			// System.out.println("此文件不存在");
			 totalSize = 0;
		}
		String result = "0K";
		if(totalSize<1){

		}else if(totalSize>=1&&totalSize<1000){
			result =  new BigDecimal(totalSize).setScale(0, BigDecimal.ROUND_HALF_UP).intValue()+"K";
		}else if(totalSize>=1000&&totalSize<1000000){
			result = new BigDecimal(totalSize/1000).setScale(0, BigDecimal.ROUND_HALF_UP).intValue()+"M";
		}else{
			result = new BigDecimal((totalSize/1000)/1000).setScale(0, BigDecimal.ROUND_HALF_UP).intValue()+"G";
		}
		return result;
		
	}
	// 获取应用程序基础路径，TODO：需要考虑有卡和无卡的情况
	public static String getAppBasePath() {
		String path = APP.gFilePath;
		return path;
	}

	// 获取指定utf-8编码文件内容，返回String
	public static String getFileDataString(String path) {
		byte[] buffer = getFileDataByte(path);
		if (buffer != null)
			return EncodingUtils.getString(buffer,
					APP.ENCODE_UTF);
		return "";
	}

	// 获取指定utf-8编码文件内容，返回字节数组
	public static byte[] getFileDataByte(String path) {
		byte[] res = null;
		try {
			FileInputStream fin = new FileInputStream(path);
			int length = fin.available();
			if (length > 0) {
				byte[] buffer = new byte[length];
				fin.read(buffer);
				res = buffer;
			}
			fin.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	// 复制打包进入的文件
	public static void copyAssetsFile(Context context, String srcName,
                                      String destName) {
		String fname = getAppBasePath() + destName;
		File file = new File(fname);

		// 如果目标文件已存在，则不复制
		if (file.exists()) {
			return;
		}

		try {
			makesureFileExist(fname);

			InputStream inReadFile = context.getAssets().open(srcName);
			int size = inReadFile.available();
			if (size > 0) {
				byte[] buffer = new byte[size];
				inReadFile.read(buffer);
				FileOutputStream fos = new FileOutputStream(file, false);
				fos.write(buffer);
				fos.close();
			}
			inReadFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 转换url地址为文件名，去掉http头和ip字段
	public static String convertFilenameFromUrl(String open_url, String param) {
		return convertFilenameFromUrl(open_url + "_" + param);
	}

	// 转换url地址为文件名，去掉http头和ip字段
	public static String convertFilenameFromUrl(String open_url) {
		String name = open_url;
		name = name.replace("http://", "");
		name = name.replace("https://", "");
		int pos = name.indexOf("/");
		if (pos > 0) {
			name = name.substring(pos);
		}
		name = name.replace("/", "_");
		name = name.replace("?", "_");
		name = name.replace("&", "_");
		name = name.replace("=", "_");
		return name;
	}

	// 数组保存为文件
	public static void saveAsFile(byte[] data, String fileName) {
		saveAsFile(data, fileName, false);
	}

	// 数组保存为文件
	public static void saveAsFile(byte[] data, String fileName, boolean append) {
		try {
			InputStream is = new ByteArrayInputStream(data);
			FileUtils.saveAsFile(is, fileName, false);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 输入流保存为文件
	public static void saveAsFile(InputStream inputStream, String fileName) {
		saveAsFile(inputStream, fileName, false);
	}

	// 输入流保存为文件
	public static void saveAsFile(InputStream inputStream, String fileName,
                                  boolean append) {
		try {
			if (TextUtils.isEmpty(fileName)) {
				return;
			}
			// make sure this file exist
			makesureFileExist(fileName);

			OutputStream os = new FileOutputStream(fileName, append);
			byte[] buf = new byte[255];
			int len = 0;
			while ((len = inputStream.read(buf)) != -1) {
				os.write(buf, 0, len);
			}
			inputStream.close();
			os.flush();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 确定指定文件是否存在，如果不存在，则创建空文件
	public static void makesureFileExist(String fileName) {
		if (TextUtils.isEmpty(fileName)) {
			return;
		}
		// file path
		int index = fileName.lastIndexOf("/");
		File file = null;
		if (index != -1) {
			String filePath = fileName.substring(0, index);
			file = new File(filePath);
			if (!file.exists()) {
				boolean ret = file.mkdirs();
				LogUtils.d("mkdirs", filePath);
			}
		}
		file = new File(fileName);
		if (!file.exists()) {// 确保文件存在
			try {
				file.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param fileName
	 */

	public static void deleteFile(String fileName) {
		File file = new File(getAppBasePath() + fileName);
		if (file.exists()) {
			file.delete();
		}
	}


	public static int getFileCount(File file) {
		int count = 0;
		File[] flist = file.listFiles();

		if (flist == null) {
			return 0;
		}

		int len = flist.length;

		for (int i = 0; i < len; i++) {
			boolean isPath = !flist[i].isFile();
			if (!isPath && !supportFile(flist[i].getName()))
				continue;
			count++;
		}
		return count;
	}

	public static int getProgramFileCount(File file) {
		int count = 0;
		File[] flist = file.listFiles();

		if (flist == null) {
			return 0;
		}

		int len = flist.length;

		for (int i = 0; i < len; i++) {
			boolean isPath = !flist[i].isFile();
			if (!isPath && !supportProgramFile(flist[i].getName()))
				continue;
			count++;
		}
		return count;
	}

	public static int getFloderCount(File file) {
		int count = 0;
		File[] flist = file.listFiles();

		if (flist == null) {
			return 0;
		}

		int len = flist.length;

		for (int i = 0; i < len; i++) {
			if (!flist[i].isFile())
				count++;
		}
		return count;
	}



	private static boolean isNullDir(File file) {
		if (file != null) {
			File files[] = file.listFiles();
			if (files != null && files.length > 0) {
				return false;
			}
		}
		return true;
	}


	public static boolean supportProgramFile(String fileName) {
		if (TextUtils.isEmpty(fileName))
			return false;

		if (fileName.length() < 4)
			return false;
		int begin = fileName.lastIndexOf(".");
		if (begin > 0) {
			String subString = fileName.substring(begin + 1, fileName.length());
			if (subString.toLowerCase().indexOf("aac") >= 0) {
				return true;
			}
		}
		// if (fileName.substring(fileName.length() - 4, fileName.length())
		// .toLowerCase().equals(".aac"))
		// return true;
		return false;
	}

	public static boolean supportFile(String fileName) {
		if (TextUtils.isEmpty(fileName))
			return false;

		if (fileName.length() < 4)
			return false;

		if (fileName.substring(fileName.length() - 4, fileName.length())
				.toLowerCase().equals(".mp3"))
			return true;
		return false;
	}

	public static String getMIMEType(File f) {
		String type = "";
		String fileName = f.getName();
		String end = fileName.substring(fileName.lastIndexOf(".") + 1,
				fileName.length()).toLowerCase();

		if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
				|| end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
			type = "audio/*";
		} else if (end.equals("3gp") || end.equals("mp4") || end.equals("avi")) {
			type = "video/*";
		} else if (end.equals("htm") || end.equals("html")) {
			type = "text/html";
		} else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg") || end.equals("bmp") || end.equals("ico")) {
			type = "image/*";
		} else if (end.equals("apk")) {
			type = "application/vnd.android.package-archive";
		} else {
			type = "text/plain";
		}
		return type;
	}



	// 递归删除文件及文件夹
	public static void deleteFileOrPath(String path) {
		File file = new File(path);
		deleteFileInfo(path);
		deleteFileOrPath(file);
	}

	/**
	 * 删除文件后缀是info的文件
	 * 
	 * @param path
	 */
	private static void deleteFileInfo(String path) {
		// TODO Auto-generated method stub
		if (path.endsWith(".tmp")) {
			path = path.substring(0, path.length() - 4);
		}
		File file = new File(path + ".info");
		if (file.exists()) {
			file.delete();
		}
	}

	// 递归删除文件及文件夹
	public static void deleteFileOrPath(File file) {
		if (file.isFile()) {
			file.delete();
			return;
		}

		if (file.isDirectory()) {
			File[] childFiles = file.listFiles();
			if (childFiles == null || childFiles.length == 0) {
				file.delete();
				return;
			}

			for (int i = 0; i < childFiles.length; i++) {
				deleteFileOrPath(childFiles[i]);
			}
			file.delete();
		}
	}

	private static class Sort implements Comparator<File> {
		@Override
		public int compare(File arg0, File arg1) {
			// TODO Auto-generated method stub
			String file1 = arg0.getName();
			String file2 = arg1.getName();
			Collator cmp = Collator.getInstance(Locale.CHINA);
			return cmp.compare(file1, file2);
		}
	}


	/*
    * 检测sdcard是否存在并创建目录
    */
	public static void InitSD() {
		Context context = APP.getContext();
		if (context == null)
			return;
		String path = Environment.getExternalStorageDirectory().getAbsolutePath();
		if (!HaveSdcard()) {
			path = context.getFilesDir().getPath() + "/";
		}
		APP.gFileFolder = path + "/" + APP.getAppBaseFolder();
		APP.gFileFolderAudio = path + "/" + APP.getAppBaseFolder() + "/audio";
		APP.gFileFolderAudioPack = path + "/" + APP.getAppBaseFolder() + "/audiopack";

		APP.gFileFolderUpImage = path + "/" + APP.getAppBaseFolder() + "/upImage";

		APP.gFileFolderDownImage = path + "/" + APP.getAppBaseFolder() + "/downImage";

		APP.gFileProtocolCachePath = path + "/" + APP.getAppBaseFolder() + "/"
				+ APP.ProtocolCachePath;

		APP.gFileFolderAudioAD = path + "/" + APP.getAppBaseFolder() + "/AudioAD";

		APP.gFileFolderHistoryPlay = path + "/" + APP.getAppBaseFolder()
				+ "/HistoryPlay";


		APP.gMyFilePath = context.getFilesDir().getPath() + "/";

		if (!HaveSdcard()) {
			APP.gFilePath = context.getFilesDir().getPath() + "/";
		} else {
			try {
				String pathString = Environment.getExternalStorageDirectory().getPath();
				StatFs statFs = new StatFs(pathString);
				long availableBlocks = statFs.getAvailableBlocks();
				long blockSize = statFs.getBlockSize();
				String[] available = fileSize(availableBlocks * blockSize);
				if (available[1].equals("KB")) {

					APP.gFilePath = context.getFilesDir().getPath() + "/";
				} else if (convert2int(available[0]) < 15 && available[1].equals("MB")) {

					APP.gFilePath = context.getFilesDir().getPath() + "/";
				} else {
					APP.gFilePath = path + "/" + APP.getAppBaseFolder() + "/";
					newFolder(APP.gFileFolder);
					newFolder(APP.gFileFolderAudio);
					newFolder(APP.gFileFolderAudioPack);
					newFolder(APP.gFileFolderUpImage);
					newFolder(APP.gFileFolderDownImage);
					newFolder(APP.gFileFolderAudioAD);
					newFolder(APP.gFileProtocolCachePath);
					newFolder(APP.gFileFolderHistoryPlay);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		// new Thread(new Runnable() {
		//
		// @Override
		// public void run() {
		// copyAllAssetsListFile(AnyRadioApplication.mContext);
		// }
		// }).start();
	}

	/*
     * 创建新文件夹
     */
	public static void newFolder(String folderPath) {
		try {
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			if (!myFilePath.exists()) {
				myFilePath.mkdirs();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	/*
     * 检测是否有sdcard卡
     */
	public static boolean HaveSdcard() {
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
			return true;
		else
			return false;
	}

	public static String convertFileSizeFromDouble(double size) {
		String str = "0";
		str = (long) (size / (1024 * 1024)) + "";
		return str;
	}

	public static String[] fileSizeFromDouble(double size) {

		String str = "";
		if (size >= 1024) {
			str = "KB";
			size /= 1024;
			if (size >= 1024) {
				str = "MB";
				size /= 1024;
			}
			if (size >= 1024) {
				str = "GB";
				size /= 1024;
			}
			if (size >= 1024) {
				str = "TB";
				size /= 1024;
			}
		}
		String result[] = new String[2];
		result[0] = "" + (long) size; // formatter.format(size);
		result[1] = str;
		return result;
	}

	private static String[] fileSize(long size) {
		return fileSizeFromDouble(size);
	}




}
