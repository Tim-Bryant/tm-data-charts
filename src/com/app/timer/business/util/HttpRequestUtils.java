package com.app.timer.business.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * HTTP请求将参数放入到MAP对象工具
 * 
 * @author liuxf
 * 
 */
public class HttpRequestUtils {

	/**
	 * HTTP解析前台传递过来的参数(包含muti-part文件方式提交)封装MAP中
	 * 如果有文件，则文件会被上传到文件服务器
	 * 使用MultipartResolver会把Request资源提前释放,故不可以使用ServletFileUpload解析文件
	 * 
	 * 整个问题产生的原因是Spring框架先调用了MultipartResolver 来处理http multi-part的请求。
	 * 这里http multipart的请求已经消耗掉。
	 * 后面又交给ServletFileUpload ，那么ServletFileUpload 就获取不到相应的multi-part请求。
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String, String> parseRequest(HttpServletRequest request) throws FileUploadException, IOException {
		Map<String, String> map = new HashMap<String, String>();
	   
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (isMultipart) {// 带文件的表单
			DiskFileItemFactory factory = new DiskFileItemFactory(1024 * 1024 * 20, null);
			ServletFileUpload upload = new ServletFileUpload(factory);
			// 得到所有表单字段对象的集合
			List<FileItem> fileItems = null;
			try {
				fileItems = upload.parseRequest(request);
				// 迭代导入到表内数据
				Iterator it = fileItems.iterator();
				for (; it.hasNext();) {
					FileItem item = (FileItem) it.next();
					String name = item.getFieldName();
					if (item.isFormField()) {
						// 普通域
						InputStream stream = item.getInputStream();
						String value = Streams.asString(stream, "UTF-8");
						map.put(name, value);
						stream.close();
					} else {
						String fileName = item.getName();
						fileName = StringUtil.getFileName(fileName);
						String ext = StringUtil.getFileExt(fileName);
						// 有后缀,保证文件不为空
						if (!Empty.isEmpty(ext)) {
							// 此处需要处理一下文件流
							//文件上传到FTP
							//boolean uploadFile = FtpFileUpload.uploadFile(fileName,item.getInputStream());
							//boolean uploadFile = FtpFileUpload.uploadFileBuffered(fileName,item.getInputStream());
							//if(uploadFile){
							//	map.put("title",fileName);
							//	map.put("src",AppConstant.FILE_VIEW_PATH+"images/2017/8/15/"+fileName);
							//}
							// in=item.getInputStream();
						}
					}
				}
			} catch (FileUploadException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {// 普通的表单
			Enumeration<String> names = request.getParameterNames();
			for (; names.hasMoreElements();) {
				String name = (String) names.nextElement();
				String value = request.getParameter(name);
				map.put(name, value);
			}
		}
		return map;
	}
	
	/**
	 * HTTP解析前台传递过来的参数(包含muti-part文件方式提交)封装MAP中
	 * 如果有文件，则文件会被上传到文件服务器
	 * @param request
	 * @return
	 */
	public static Map<String, String> parseRequestByMultipartResolver(HttpServletRequest request) throws FileUploadException, IOException {
		Map<String, String> map = new HashMap<String, String>();

		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (isMultipart) {// 带文件的表单
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;    
			Iterator<String> fileNames = multipartRequest.getFileNames();
			for(;fileNames.hasNext();){
				CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile(fileNames.next()); 
				FileItem fileItem = file.getFileItem();
				String name=file.getName();
				if (fileItem.isFormField()) {
					// 普通域
					InputStream stream = fileItem.getInputStream();
					String value = Streams.asString(stream, "UTF-8");
					map.put(name, value);
					stream.close();
				} else {
					String fileName = fileItem.getName();
					fileName = StringUtil.getFileName(fileName);
					String ext = StringUtil.getFileExt(fileName);
					// 有后缀,保证文件不为空
					if (!Empty.isEmpty(ext)) {
						// 此处需要处理一下文件流
						//文件上传到FTP
						//boolean uploadFile = FtpFileUpload.uploadFile(fileName,item.getInputStream());
						boolean uploadFile;
						try {
							//uploadFile = FtpFileUpload.uploadFileBuffered(fileName,fileItem.getInputStream());
							//if(uploadFile){
							//	map.put("title",fileName);
							//	map.put("src",AppConstant.FILE_VIEW_PATH+"images/2017/8/15/"+fileName);
							//}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		} else {// 普通的表单
			Enumeration<String> names = request.getParameterNames();
			for (; names.hasMoreElements();) {
				String name = (String) names.nextElement();
				String value = request.getParameter(name);
				map.put(name, value);
			}
		}
		return map;
	}
	
}
