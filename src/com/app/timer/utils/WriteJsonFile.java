package com.app.timer.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author 作者 E-mail:xiaofeng09happy@sina.com
 * @version 创建时间：2017年11月6日 下午10:37:05 类说明:写json 文件的工具类
 */
public class WriteJsonFile {
	/**
	 * 写一个新的json文件
	 * @param path
	 * @param content
	 */
	public static void WriteConfigJson(String path, String content) {
		File file = new File(path);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		try {
			file.delete();
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			FileWriter fw = new FileWriter(file, true);
			fw.write(content);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		WriteJsonFile.WriteConfigJson("d:/test.json", "{id:1,name:22:code:333}");
	}
}
