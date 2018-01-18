package com.app.timer.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.app.timer.business.util.Empty;

/**   
* @author 作者 E-mail:xiaofeng09happy@sina.com   
* @version 创建时间：2017年11月6日 下午1:00:18   
* 类说明:Json文件读取等操作工具类
*/
public class JsonUtils {
   
	private static final String UTF_8 = "UTF-8";
	
	/**
	 * 把json文件读取为String字符串
	 * @param dir
	 * @return
	 */
	public static String readFileString(String dir){
		String laststr = "";
		File file = new File(dir);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			// int line=1;
			while ((tempString = reader.readLine()) != null) {
				laststr = laststr + tempString;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException el) {
				}
			}
		}
		return laststr;
	}
	
	
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
	
	
	/**
	 * 删除一个json文件
	 * @param path
	 * @param content
	 */
	public static void deleteJsonFile(String path,String filename) {
		 File file=new File(path+"/"+filename);
         if(file.exists()&&file.isFile()){
        	 file.delete();
         }
	}
	
	
	/**
	 * 删除Temp文件夹下面的所有临时文件json文件
	 * @param path
	 * @param content
	 */
	public static void deleteJsonTempFiles(String path) {
		 File file=new File(path);
         if(file.exists()&&file.isDirectory()){
        	 File[] listFiles = file.listFiles();
        	 for(File fe:listFiles){
        		 fe.delete();
        	 }
         }
	}
	
	/**
	 * 给json文件加密
	 * @param path
	 * @param content
	 */
	public static void encryptJson(String path,String filename) {
		String readFileString = readFileString(path+"/"+filename);
		//System.out.println("JSON文件加密操作");
		//System.out.println(readFileString);
		String encodeData = encodeData(readFileString);
		//System.out.println(encodeData);
		WriteConfigJson(path+"/"+filename,encodeData);
	}
	
	/**
	 * 给json文件解密
	 * @param path
	 * @param content
	 */
	public static void unencryptJson(String path,String filename) {
		String readFileString = readFileString(path+"/"+filename);
		String encodeData = decodeData(readFileString);
		WriteConfigJson(path,filename);
	}
	
	
	/**
     * 对给定的字符串进行base64解码操作
     */
    public static String decodeData(String inputData) {
        try {
            if (null == inputData) {
                return null;
            }
            return new String(Base64.decodeBase64(inputData.getBytes(UTF_8)), UTF_8);
        } catch (UnsupportedEncodingException e) {
           e.printStackTrace();
        }
        return "";
    }

    /**
     * 对给定的字符串进行base64加密操作
     */
    public static  String encodeData(String inputData) {
        try {
            if (null == inputData) {
                return null;
            }
            return new String(Base64.encodeBase64(inputData.getBytes(UTF_8)), UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
    
    /**
     * 对JSONArray进行倒序输出
     * @param array
     * @param dateName
     */
    public static JSONArray sortJsonArrayRelect(JSONArray array){
    	JSONArray newlist=new JSONArray();
        for(int i=array.size();i>0;i--){
        	Object object = array.get(i-1);
        	newlist.add(object);
        }
        return newlist;
    }
    
    
    
    /**
	 * 把外部目录不存在的json文件从服务器目录复制一份出来
	 * @param sourcedir 目标文件夹
	 * @param destdir 目标文件夹
	 * @return
	 */
	public static void copyFileWhenOuterPathisNotFromServerPath(String sourcedir, String destdir) {
		if (!Empty.isEmpty(sourcedir) && !Empty.isEmpty(destdir)) {
			File file = new File(sourcedir);
			File destfile = new File(destdir);
			if (!file.exists()) {
				try {
					FilePathUtils.copy(file, destfile);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
