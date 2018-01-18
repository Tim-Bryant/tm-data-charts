package com.app.timer.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.app.timer.business.util.AppConstant;
import com.app.timer.listener.TimerListener;

/**
 * @author 作者 E-mail:xiaofeng09happy@sina.com
 * @version 创建时间：2017年11月15日 下午2:09:31 类说明:文件路径处理工具类
 */
public class FilePathUtils {

	private static final Logger log = LoggerFactory.getLogger(FilePathUtils.class);
	/**
	 * 获取数据文件读写路径
	 * 
	 * @param request
	 * @return
	 */
	public static String getDataFilesRealPath(HttpServletRequest request) {
		String path = "";
		if (AppConstant.IS_USE_FILE_DATA_PATH) {// 使用外部路径开启
			path = AppConstant.getValue("FILE_DATA_PATH");
		} else {// 使用外部路径关闭
			path = request.getSession().getServletContext().getRealPath(AppConstant.getValue("FILE_DATA_PATH_DEFAULT"));
		}
		return path;
	}

	/**
	 * 对文件路径进行处理
	 * 
	 * @param array
	 * @param dateName
	 * @throws IOException
	 */
	public static String filePathHander(ServletContext sc) {
		String rootPath = "";
		// 从用户自定义目录读取
		if (AppConstant.IS_USE_FILE_DATA_PATH) {
			log.info("当前使用自定义数据目录...");
			File desfile = new File(AppConstant.FILE_DATA_PATH);
			// 指定的目录不存在就新建
			if (!desfile.exists()) {
				desfile.mkdirs();
			}
			// 将项目包中初始化的文件复制过来
			String realPath = sc.getRealPath("/data");
			File sourcePath = new File(realPath);
			try {
				// 复制文件 如果目录中存在就不复制 避免数据覆盖
				copy(sourcePath, desfile);
			} catch (Exception e) {
				e.printStackTrace();
			}
			rootPath = desfile.getAbsolutePath();
		} else {// 从系统的Tomcat目录读取(主要是演示用，数据会因为编译而丢失)
			log.info("当前使用应用服务器项目下的默认目录，该方案只适合开发不适合正式使用...");
			rootPath = sc.getRealPath("/data");
		}
		return rootPath;
	}

	public static void copy(File file, File toFile) throws Exception {
		byte[] b = new byte[1024];
		int a;
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			if (file.isDirectory()) {
				String filepath = file.getAbsolutePath();
				filepath = filepath.replaceAll("\\\\", "/");
				String toFilepath = toFile.getAbsolutePath();
				toFilepath = toFilepath.replaceAll("\\\\", "/");
				int lastIndexOf = filepath.lastIndexOf("/");
				toFilepath = toFilepath + filepath.substring(lastIndexOf, filepath.length());
				File copy = new File(toFilepath);
				// 复制文件夹
				if (!copy.exists()) {
					copy.mkdir();
				}
				// 遍历文件夹
				for (File f : file.listFiles()) {
					copy(f, copy);
				}
			} else {
				if (toFile.isDirectory()) {
					String filepath = file.getAbsolutePath();
					filepath = filepath.replaceAll("\\\\", "/");
					String toFilepath = toFile.getAbsolutePath();
					toFilepath = toFilepath.replaceAll("\\\\", "/");
					int lastIndexOf = filepath.lastIndexOf("/");
					toFilepath = toFilepath + filepath.substring(lastIndexOf, filepath.length());
					// 写文件
					File newFile = new File(toFilepath);
					// 如果文件存在就不复制 避免覆盖了备份的历史数据,这种情况建议手动修改文件
					if (!newFile.exists()) {
						fis = new FileInputStream(file);
						fos = new FileOutputStream(newFile);
						while ((a = fis.read(b)) != -1) {
							fos.write(b, 0, a);
						}
					}
				} else {
					// 写文件
					fis = new FileInputStream(file);
					fos = new FileOutputStream(toFile);
					while ((a = fis.read(b)) != -1) {
						fos.write(b, 0, a);
					}
				}
			}
		} finally {
			// 切记关闭流
			if (fis != null) {
				fis.close();
			}
			if (fos != null) {
				fos.close();
			}
		}
	}
}
