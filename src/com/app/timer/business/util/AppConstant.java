package com.app.timer.business.util;

import java.util.Locale;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 应用程序常量工具类
 * 
 * @author liuxf
 * 
 */
public class AppConstant {

	private static final Logger log = LoggerFactory.getLogger(AppConstant.class);
	
	/** 对象的状态-启用 */
	public final static String STATE_OPEN = "1";
	/** 对象的状态-禁用 */
	public final static String STATE_CLOSED = "0";
	
	/**FTP上传服务相关*/
	/** FTP-IP */
	public  static String FTP_IP = "";
	/** FTP-端口 */
	public  static int FTP_PORT = 21;
	/** FTP-用户名 */
	public  static String FTP_USER = "";
	/** FTP-密码 */
	public  static String FTP_PWD = "";
	/** FTP-工作目录 */
	public  static String FTP_WORKPATH = "";
	
	/** 图片预览-FTP-IP */
	public  static String FTP_IMAGE_IP = "";
	/** 图片预览-FTP-端口 */
	public  static int FTP_IMAGE_PORT = 21;
	/** 图片预览-FTP-用户名 */
	public  static String FTP_IMAGE_USER = "";
	/** 图片预览-FTP-密码 */
	public  static String FTP_IMAGE_PWD = "";
	/** 图片预览-FTP-工作目录 */
	public  static String FTP_IMAGE_WORKPATH = "";
	
	/** FTP-可以预览图片的目录 */
	public  static String FILE_VIEW_PATH = "";
	
	/** 统计报表上传图片的目录  该目录原则上需要有HTTPServer映射 */
	public  static String EXP_Chart_ImagePath = "";
	/** 本系统数据存储的外部目录，默认在Tomcat下，编译会丢失，建议放在本地磁盘隐秘处 */
	public  static String FILE_DATA_PATH = "";
	/** 本系统数据存储的默认目录，默认在Tomcat下，编译会丢失，建议放在本地磁盘隐秘处 */
	public  static String FILE_DATA_PATH_DEFAULT = "";
	
	/** 是否使用本地指定目录   true使用 ;  false 不使用则默认放TOMCAT下 */
	public  static boolean IS_USE_FILE_DATA_PATH = false;
	/** 报表图片文件是否使用本地指定目录   true使用  ;false 不使用则默认放TOMCAT下 images/charts */
	public  static boolean CHART_PIC_IS_PUT_OUT = false;
	
	
	/**读取配置文件*/
	private static final ResourceBundle APP_RESOURCE;

	static {
		log.info("第一次被使用,正在加载配置文件属性...");
		APP_RESOURCE = ResourceBundle.getBundle("application", Locale.getDefault());
		FTP_IP = APP_RESOURCE.getString("FTP_REMOTEHOST");
		FTP_PORT = Integer.valueOf(APP_RESOURCE.getString("FTP_REMOTEPORT"));
		FTP_USER = APP_RESOURCE.getString("FTP_USERNAME");
		FTP_PWD = APP_RESOURCE.getString("FTP_PASSWORD");
		FTP_WORKPATH = APP_RESOURCE.getString("FTP_WORKINGPATH");
		//图片因为需要在线预览个性化上传 
		FTP_IMAGE_IP = APP_RESOURCE.getString("FTP_IMAGE_REMOTEHOST");
		FTP_IMAGE_PORT = Integer.valueOf(APP_RESOURCE.getString("FTP_IMAGE_REMOTEPORT"));
		FTP_IMAGE_USER = APP_RESOURCE.getString("FTP_IMAGE_USERNAME");
		FTP_IMAGE_PWD = APP_RESOURCE.getString("FTP_IMAGE_PASSWORD");
		FTP_IMAGE_WORKPATH = APP_RESOURCE.getString("FTP_IMAGE_WORKINGPATH");
		
		FILE_VIEW_PATH = APP_RESOURCE.getString("FILE_VIEW_PATH");
		EXP_Chart_ImagePath = APP_RESOURCE.getString("EXP_Chart_ImagePath");
		FILE_DATA_PATH = APP_RESOURCE.getString("FILE_DATA_PATH");
		FILE_DATA_PATH_DEFAULT = APP_RESOURCE.getString("FILE_DATA_PATH_DEFAULT");
		IS_USE_FILE_DATA_PATH = Boolean.parseBoolean(APP_RESOURCE.getString("IS_USE_FILE_DATA_PATH"));
		CHART_PIC_IS_PUT_OUT = Boolean.parseBoolean(APP_RESOURCE.getString("CHART_PIC_IS_PUT_OUT"));
	}
	
	/**
	 * 根据key值在“application.properties”资源文件中查询对应的值，<br>
	 * application.properties文件中定义了系统将要用到的全局性常量。
	 * @param key
	 * @return
	 */
	public static String getValue(String key) {
		try {
			return APP_RESOURCE.getString(key);
		} catch (Exception e) {
			return "";
		}
	}
}
