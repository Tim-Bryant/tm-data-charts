package com.app.timer.listener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.app.timer.business.util.Empty;
import com.app.timer.utils.ExcelUtils;
import com.app.timer.utils.FilePathUtils;
import com.app.timer.utils.JsonUtils;

/**
 * @author 作者 E-mail:xiaofeng09happy@sina.com
 * @version 创建时间：2017年11月9日 下午4:34:43 类说明:TimerListener.java
 */
public class TimerListener implements ServletContextListener {

	private static final Logger log = LoggerFactory
            .getLogger(TimerListener.class);
	
	@Override
	public void contextDestroyed(ServletContextEvent sc) {
		log.info("Server App shutdown........");
		//服务器停止   删除全部临时文件
		JsonUtils.deleteJsonTempFiles(sc.getServletContext().getRealPath("/")+"/data/temp/");
	}

	@Override
	public void contextInitialized(ServletContextEvent sc) {
		log.info("Server App start........");
		//服务器启动对数据目录做初始化处理
		String filePathHander = FilePathUtils.filePathHander(sc.getServletContext());
		log.info(filePathHander);
	}

}
