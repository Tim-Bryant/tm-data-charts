package com.app.timer.business.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FtpFileUpload {

	private static final Logger log = LoggerFactory.getLogger(FtpFileUpload.class);
	
	public static boolean uploadFile(String fileName, InputStream inputStream) throws Exception {
		FTPClient ftpClient = openFtp();
		cdToday(ftpClient);
		OutputStream outputStream = null;
		int bytes;
		byte[] b = new byte[1024];
		ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
		ftpClient.setBufferSize(1024*1024); //增大缓存区
		ftpClient.enterLocalPassiveMode();

		outputStream = ftpClient.storeFileStream(new String(fileName.getBytes("GBK"), "ISO8859_1"));
		// log.debug("FTP 链接模式：" + ftpClient.getDataConnectionMode());
		try {
			while ((bytes = inputStream.read(b)) != -1) {
				outputStream.write(b, 0, bytes);
			}
			outputStream.flush();
			outputStream.close();
			inputStream.close();
			boolean flag;
			flag = ftpClient.completePendingCommand();
			return flag;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 带缓冲区的读写
	 * @param fileName
	 * @param inputStream
	 * @return
	 * @throws SocketException
	 * @throws IOException
	 */
	public static boolean uploadFileBuffered(String fileName, InputStream inputStream) throws Exception {
		FTPClient ftpClient = openFtp();
		cdToday(ftpClient);
		OutputStream outputStream = null;
		int bytes;
		byte[] b = new byte[8096];
		ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
		ftpClient.setBufferSize(1024*1024); //增大缓存区
		ftpClient.enterLocalPassiveMode();

		outputStream = ftpClient.storeFileStream(new String(fileName.getBytes("GBK"), "ISO8859_1"));
		// log.debug("FTP 链接模式：" + ftpClient.getDataConnectionMode());
		try {
			BufferedInputStream bis = new BufferedInputStream(inputStream); 
			BufferedOutputStream bos = new BufferedOutputStream(outputStream);  
			while ((bytes = bis.read(b)) != -1) {
				bos.write(b, 0, bytes);
			}
			bos.flush();
			bos.close();
			bis.close();
			boolean flag;
			flag = ftpClient.completePendingCommand();
			return flag;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * FTP文件的删除
	 * 
	 * @param fileName
	 * @param inputStream
	 * @return
	 * @throws SocketException
	 * @throws IOException
	 */
	public static boolean deleteFile(String pathName) throws SocketException, IOException {
		FTPClient ftpClient = openFtp();
		if (!StringUtils.isBlank(pathName)) {
			FTPFile[] listFiles = ftpClient.listFiles(new String(pathName.getBytes("GBK"), "ISO8859_1"));
			boolean deleteFile = false;
			if (listFiles.length <= 0) {
				System.out.println("文件不存在！");
			} else {
				deleteFile = ftpClient.deleteFile(new String(pathName.getBytes("GBK"), "ISO8859_1"));
			}
			return deleteFile;
		}
		return false;
	}

	/**
	 * FTP登录和目录切换
	 * 
	 * @return
	 * @throws SocketException
	 * @throws IOException
	 */
	private static FTPClient openFtp() throws SocketException, IOException {
		String ftpServerIp = AppConstant.FTP_IP;
		int ftpPort = AppConstant.FTP_PORT;
		String userName = AppConstant.FTP_USER;
		String password = AppConstant.FTP_PWD;
		String workingPath = AppConstant.FTP_WORKPATH;

		FTPClient ftpClient = null;
		if (ftpClient == null) {
			ftpClient = new FTPClient();
		}
		ftpClient.setBufferSize(1 * 1024 * 1024);
		if (!ftpClient.isConnected()) {
			// ftpClient.connect(ftpServerIp, ftpPort);
			ftpClient.connect(ftpServerIp, ftpPort);// 新版本是int型 原来是string
			ftpClient.login(userName, password);

		}
		if (!StringUtils.isBlank(workingPath)) {
			boolean b = ftpClient.changeWorkingDirectory(workingPath);
			/**
			 * 解决配置的路径不存在，自动创建的bug(配置路径时，账号的根路径不需要写进来)
			 */
			if (!b) {
				String[] directy = workingPath.split("/");
				StringBuffer buf = new StringBuffer();
				for (int i = 0; i < directy.length; i++) {
					if (i == directy.length - 1) {
						buf.append(directy[i].toString());
					} else {
						buf.append(directy[i].toString());
						buf.append("/");
					}
				}
				boolean isTure = ftpClient.makeDirectory(buf.toString());
				// 目录创建成功，转换到该目录
				if (isTure) {
					ftpClient.changeWorkingDirectory(workingPath);
				}
			}
			
			/**
			 * 当前目录下新建日期目录 
			 */
		}
		return ftpClient;
	}
	
	/**
	 * 转到今日文件夹，如果不存在则创建
	 * 
	 * @throws IOException
	 * @return today path
	 */
	private static String cdToday(FTPClient ftpClient) throws Exception {
		//FTPClient ftpClient = openFtp();
		Calendar cal = Calendar.getInstance();
		boolean makeDirectory=true;
		StringBuffer buf = new StringBuffer();
		buf.append(cal.get(Calendar.YEAR));
		if (makeDirectory == true) {
			ftpClient.makeDirectory(buf.toString());

			buf.append("/");
			buf.append(cal.get(Calendar.MONTH) + 1);
			ftpClient.makeDirectory(buf.toString());

			buf.append("/");
			buf.append(cal.get(Calendar.DAY_OF_MONTH));
			ftpClient.makeDirectory(buf.toString());
		}else{
			buf.append("/");
			buf.append(cal.get(Calendar.MONTH) + 1);
			buf.append("/");
			buf.append(cal.get(Calendar.DAY_OF_MONTH));
		}
		/*
		 * if (!success) { log.debug("文件夹已存在:" + path); }
		 */
		String path = buf.toString();
		boolean b=ftpClient.changeWorkingDirectory(path);
        log.info("ftp切换工作目录是否成功：" +b);
        log.info("ftp切换的工作目录：" +path);
		return path;
	}
}
