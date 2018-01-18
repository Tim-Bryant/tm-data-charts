package com.app.timer.business.controller;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.beetl.ext.fn.Json;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.app.timer.business.pojo.User;
import com.app.timer.business.util.AppConstant;
import com.app.timer.business.util.Empty;
import com.app.timer.business.util.FtpFileUpload;
import com.app.timer.business.util.HttpRequestUtils;
import com.app.timer.business.util.RandomGUID;
import com.app.timer.business.util.SessionData;
import com.app.timer.business.util.StringUtil;
import com.app.timer.utils.ExcelUtils;
import com.app.timer.utils.FilePathUtils;
import com.app.timer.utils.JsonUtils;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * 数据导入对象的控制层
 * @author liuxf
 *
 */
@Controller
@RequestMapping("/dataimp")
public class DataImportController {

	/**SQLSERVER数据库驱动类*/
	public static final String SQLSERVER_DRIVER_NAME="com.microsoft.sqlserver.jdbc.SQLServerDriver";
	/**Oracle数据库驱动类*/
	public static final String ORACLE_DRIVER_NAME="oracle.jdbc.driver.OracleDriver";
	
	@RequestMapping("/excel")
	public ModelAndView goList(HttpServletRequest request){
		ModelAndView mView=new ModelAndView("dataimp/excel");
		String destfile = FilePathUtils.getDataFilesRealPath(request)+"/data/excelmanager.json";
		String readJsonString = JsonUtils.readFileString(destfile);
		JSONObject json=JSONObject.parseObject(readJsonString);
		if(!Empty.isEmpty(json)){
			mView.addObject("params", json);
			if(json.containsKey("allowmaxrows")){
			  long max=	Integer.valueOf((String)json.get("allowmaxrows"));
			  ExcelUtils.allowmaxrows=max;
			}
			if(json.containsKey("pageNumbers")){
			  long pagesize=	Integer.valueOf((String)json.get("pageNumbers"));
			  ExcelUtils.pageNumbers=pagesize;
		    }
		}
		return mView;
	}
	
	@RequestMapping("/sqlserver")
	public ModelAndView gosqlserver(HttpServletRequest request){
		ModelAndView mView=new ModelAndView("dataimp/sqlserver");
		return mView;
	}
	
	@RequestMapping("/oracle")
	public ModelAndView goOracle(HttpServletRequest request){
		ModelAndView mView=new ModelAndView("dataimp/oracle");
		return mView;
	}
	
	@RequestMapping("/welcome")
	public ModelAndView goWelcome(HttpServletRequest request){
		ModelAndView mv=new ModelAndView("welcome");
		return mv;
	}
	
	@RequestMapping("/excelmanager")
	public ModelAndView goExcelManager(HttpServletRequest request){
		ModelAndView mView=new ModelAndView("dataimp/excelmanager");
		String sourcefile = request.getSession().getServletContext().getRealPath("/")+"/data/excelmanager.json";
		String destfile = FilePathUtils.getDataFilesRealPath(request)+"/data/excelmanager.json";
		//为了避免文件不存在先复制
		JsonUtils.copyFileWhenOuterPathisNotFromServerPath(sourcefile, destfile);
		String readJsonString = JsonUtils.readFileString(destfile);
		JSONObject json=JSONObject.parseObject(readJsonString);
		if(!Empty.isEmpty(json)){
			mView.addObject("params", json);
		}
		return mView;
	}
	
	
	@RequestMapping(value="/excelCharts",produces="application/json;charset=UTF-8")
	public ModelAndView doCharts(HttpServletRequest request){
		ModelAndView mView=new ModelAndView("dataimp/excelCharts");
		String filename = request.getParameter("filename");
		String dir = FilePathUtils.getDataFilesRealPath(request)+"/data/menu/"+filename;
		String readFileString = JsonUtils.readFileString(dir);
		readFileString=readFileString.replace("\"", "\'");
		//System.out.println(readFileString);
		JSONObject json=JSONObject.parseObject(readFileString);
		if(!Empty.isEmpty(json)){
			mView.addObject("data", json);
		}else{
			mView.addObject("data", "{tableTitle:[],tableBody:[],cells:[]}");
		}
		
		mView.addObject("pageSize", ExcelUtils.pageNumbers);
		mView.addObject("filename",filename);
		SessionData sessionData = SessionData.getSessionData(request);
		User user = sessionData.getUser();
		mView.addObject("currentUser",user);
		return mView;
	}
	
	@RequestMapping(value="parserExcel",produces="application/json;charset=UTF-8")
	public @ResponseBody Map<String, Object> parserExcel(HttpServletRequest request) throws IOException{
		Map<String, Object> result=new HashMap<String, Object>();
		Map<String, Object> map=new HashMap<String, Object>();
		String dataJson=new String();
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
					if (!Empty.isEmpty(ext)&&(".xlsx".equals(ext)||".xls".equals(ext))) {
						Map<String, Object> readExcelMap;
						try {
							if(".xlsx".equals(ext)){
								readExcelMap = ExcelUtils.readExcelByXlsx(fileItem.getInputStream());
							}else{
								readExcelMap = ExcelUtils.readExcelByXls(fileItem.getInputStream());
							}
							//该json参数放到前台是为了显示导入数据的表格
							dataJson = JSONObject.toJSONString(readExcelMap);
							result.put("excelData", dataJson);
							result.put("pageSize", ExcelUtils.pageNumbers);
							//为了解决POSt参数在不同服务器内大小的限制  不要使用前台将参数传递过来的方法   将数据放到临时目录中
							String datafileurl="excel_temp_"+System.currentTimeMillis()+".json";
							String dir = request.getSession().getServletContext().getRealPath("/")+"/data/temp/"+datafileurl;
							JsonUtils.WriteConfigJson(dir, dataJson);
							result.put("tempfilename", datafileurl);
							result.put("success", true);
							//String realPath = request.getSession().getServletContext().getRealPath("/");
							//System.out.println(realPath);
							//System.out.println(dataJson);
						} catch (Exception e) {
							e.printStackTrace();
							result.put("success", false);
							result.put("errermessage", "对不起 ，您上传的Excel行数超过允许的最大值【"+ ExcelUtils.allowmaxrows+"】");
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
		return result;
	}
	
	@RequestMapping(value="excelSave")
	public @ResponseBody Map<String, Object> saveExcelInfo(HttpServletRequest request) throws IOException{
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Map<String, String> parseRequest = HttpRequestUtils.parseRequest(request);
			
			String tempfilename=parseRequest.get("tempfilename");
			String menunodename=parseRequest.get("menunodename");
			if(!Empty.isEmpty(tempfilename)){
				String tempfiledir = request.getSession().getServletContext().getRealPath("/")+"/data/temp/"+tempfilename;
				//从临时文件中加载需要的数据
				String datas=JsonUtils.readFileString(tempfiledir);
				//用完以后删除临时文件
				JsonUtils.deleteJsonFile(request.getSession().getServletContext().getRealPath("/")+"/data/temp/", tempfilename);
				
				String datafileurl="datalist_"+System.currentTimeMillis()+".json";
				String dir = FilePathUtils.getDataFilesRealPath(request)+"/data/menu/"+datafileurl;
				JsonUtils.WriteConfigJson(dir, datas);
				String menulist = FilePathUtils.getDataFilesRealPath(request)+"/data/menulist.json";
				String readJsonString = JsonUtils.readFileString(menulist);
				JSONArray array=JSONArray.parseArray(readJsonString);
				//Excel导入格式生成菜单
				JSONObject currentMenu=JSONObject.parseObject("{'menuname':'"+menunodename+"','url':'dataimp/excelCharts','filename':'"+datafileurl+"','type':'excel'}");
				array.add(currentMenu);
				JsonUtils.WriteConfigJson(menulist, array.toJSONString());
				map.put("message", "保存成功");
			}else{
				map.put("message", "保存失败");
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	@RequestMapping(value="menudelete")
	public @ResponseBody Map<String, Object> menudelete(HttpServletRequest request) throws IOException{
		try {
			Map<String, String> parseRequest = HttpRequestUtils.parseRequest(request);
			String filename=parseRequest.get("filename");
			String menulist = FilePathUtils.getDataFilesRealPath(request)+"/data/menulist.json";
			String readJsonString = JsonUtils.readFileString(menulist);
			JSONArray array=JSONArray.parseArray(readJsonString);
			for(int i=0;i<array.size();i++){
				JSONObject jsonobj=array.getJSONObject(i);
				if(jsonobj.containsKey("filename")){
					if(jsonobj.containsValue(filename)){
						array.remove(i);
					}
				}
			}
			String datadir = FilePathUtils.getDataFilesRealPath(request)+"/data/menu";
			JsonUtils.deleteJsonFile(datadir, filename);
			JsonUtils.WriteConfigJson(menulist, array.toJSONString());
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", "删除成功");
		return map;
	}
	
	@RequestMapping(value="parserSqlServer",produces="application/json;charset=UTF-8")
	public @ResponseBody Map<String, Object> parserSqlServer(HttpServletRequest request) throws IOException{
		 Map<String, Object> resultMap=new HashMap<String, Object>();  
		 Map<String, Object> queryMap=new HashMap<String, Object>();  
		 Connection dbConn=null;
		 Statement createStatement=null;
		try
		  {
		   Map<String, String> parseRequest = HttpRequestUtils.parseRequest(request);
		   
		   String sqlserver_ip = parseRequest.get("sqlserver_ip");
		   String sqlserver_port = parseRequest.get("sqlserver_port");
		   String sqlserver_user = parseRequest.get("sqlserver_user");
		   String sqlserver_pwd = parseRequest.get("sqlserver_pwd");
		   String sqlserver_dbname = parseRequest.get("sqlserver_dbname");
		   String sql_query = parseRequest.get("sql_query");
		   Class.forName(SQLSERVER_DRIVER_NAME);
		   String dbURL="jdbc:sqlserver://"+sqlserver_ip+":"+sqlserver_port+";DatabaseName="+sqlserver_dbname;
		   String userName=sqlserver_user;
		   String userPwd=sqlserver_pwd;
		   
		   //System.out.println(sql_query);
		    dbConn=DriverManager.getConnection(dbURL,userName,userPwd);
		    createStatement = dbConn.createStatement();
		   ResultSet executeQuery = createStatement.executeQuery(sql_query);
		   // 表格的头信息
		   List<String> tableTitle = new ArrayList<String>();
		   //内容
		   List<Map<String,String>> bodyContent = new ArrayList<Map<String,String>>();
		   
		   //获取字段名信息
		   ResultSetMetaData metaData = executeQuery.getMetaData();
		   if(!Empty.isEmpty(metaData)){
			   queryMap.put("cells", metaData.getColumnCount());
			   for(int i=1;i<=metaData.getColumnCount();i++){
				   String columnName = metaData.getColumnName(i);
				   int columnType = metaData.getColumnType(i);
				   //System.out.println(columnName+":"+columnType);
				   tableTitle.add(columnName);
			   }
		   }
		    int rowCount =0;
		    DecimalFormat  df= new DecimalFormat("#.######");  
		    SimpleDateFormat timeformatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    SimpleDateFormat dateformatter = new SimpleDateFormat("yyyy/MM/dd");
		    while(executeQuery.next()){
		    	//保存每一列数据
		    	Map<String,String> cellMap=new HashMap<String,String>();
		    	rowCount++;
		    	if(!Empty.isEmpty(tableTitle)&&tableTitle.size()>0){
		    		//表格的表头信息
		    		for(int index=0;index<tableTitle.size();index++){
		    			String columuName=tableTitle.get(index);
		    			//如果是字符串
		    			if(Types.NVARCHAR==metaData.getColumnType(index+1)){
		    				cellMap.put(""+index+"", Empty.isEmpty(executeQuery.getString(columuName.toUpperCase()))?"":executeQuery.getString(columuName.toUpperCase()).trim());
		    			}else if(Types.DOUBLE==metaData.getColumnType(index+1)){
		    				cellMap.put(""+index+"", Empty.isEmpty(executeQuery.getDouble(columuName.toUpperCase()))?"":df.format(executeQuery.getDouble(columuName.toUpperCase())));
		    			}else if(Types.FLOAT==metaData.getColumnType(index+1)){
		    				cellMap.put(""+index+"", Empty.isEmpty(executeQuery.getFloat(columuName.toUpperCase()))?"":df.format(executeQuery.getFloat(columuName.toUpperCase())));
		    			}else if(Types.INTEGER==metaData.getColumnType(index+1)){
		    				cellMap.put(""+index+"", Empty.isEmpty(executeQuery.getInt(columuName.toUpperCase()))?"":String.valueOf(executeQuery.getInt(columuName.toUpperCase())));
		    			}else if(Types.DECIMAL==metaData.getColumnType(index+1)){
		    				cellMap.put(""+index+"", Empty.isEmpty(executeQuery.getBigDecimal(columuName.toUpperCase()))?"":df.format(executeQuery.getBigDecimal(columuName.toUpperCase())));
		    			}else if(Types.DATE==metaData.getColumnType(index+1)){
		    				cellMap.put(""+index+"", Empty.isEmpty(executeQuery.getDate(columuName.toUpperCase()))?"":dateformatter.format(executeQuery.getDate(columuName.toUpperCase())));
		    			}else if(Types.TIMESTAMP==metaData.getColumnType(index+1)){
		    				cellMap.put(""+index+"", Empty.isEmpty(executeQuery.getTimestamp(columuName.toUpperCase()))?"":timeformatter.format(executeQuery.getTimestamp(columuName.toUpperCase())));
		    			}else if(Types.CLOB==metaData.getColumnType(index+1)){
		    				Clob clob = executeQuery.getClob(columuName.toUpperCase());
		    				String subString = clob.getSubString(1, (int) clob.length());
		    				subString=subString.replace("\'", "");
		    				subString=subString.replace("\"", "");
		    				cellMap.put(""+index+"", Empty.isEmpty(clob)?"":subString);
		    			}else{
		    				String subString = Empty.isEmpty(executeQuery.getString(columuName.toUpperCase()))?"":executeQuery.getString(columuName.toUpperCase()).trim();
		    				subString=subString.replace("\'", "");
		    				subString=subString.replace("\"", "");
		    				cellMap.put(""+index+"", subString);
		    			}
		    		}
		    	}
		    	bodyContent.add(cellMap);
		    }
		    queryMap.put("rows", rowCount);
		    queryMap.put("tableTitle", tableTitle);
		    queryMap.put("tableBody", bodyContent);
		    String jsonString = JSONObject.toJSONString(queryMap);
		    
		    String datafileurl="excel_temp_"+System.currentTimeMillis()+".json";
			String dir = request.getSession().getServletContext().getRealPath("/")+"/data/temp/"+datafileurl;
			JsonUtils.WriteConfigJson(dir, jsonString);
			resultMap.put("pageSize", ExcelUtils.pageNumbers);
			resultMap.put("tempfilename", datafileurl);
			resultMap.put("success", true);
		    //System.out.println(jsonString);
		    
		    resultMap.put("jsondata",jsonString);
		    resultMap.put("message", "查询成功");
		  }catch (ClassNotFoundException e) {
			    resultMap.put("message", "数据库驱动程序问题");
				e.printStackTrace();
			}// 加载Oracle驱动程序
			catch (SQLException e) {
				String message = e.getMessage();
				if(message.indexOf("无法打开登录所请求的数据库")>-1){
					resultMap.put("message", "连接失败，该数据库名字可能不正确,请检查！"); 
				}else if(message.indexOf("用户 ")>-1 && message.indexOf("登录失败")>-1){
					resultMap.put("message", "连接失败，用户名或密码不正确,请检查！"); 
				}else if(message.indexOf("通过端口")>-1  && message.indexOf("TCP/IP 连接失败")>-1){
					resultMap.put("message", "连接失败，端口或者IP可能不正确，请检查！"); 
				}else if(message.indexOf("附近有语法错误")>-1){
					resultMap.put("message", "SQL语句有错误，请查看！"); 
				}else if(message.indexOf("列名")>-1  && message.indexOf("无效")>-1){
					resultMap.put("message", message); 
				}else if(message.indexOf("对象名")>-1  && message.indexOf("无效")>-1){
					resultMap.put("message", message); 
				}else{
					resultMap.put("message", "连接异常，请检查各参数!"); 
				}
				e.printStackTrace();
			} catch (FileUploadException e) {
				e.printStackTrace();
			}finally {
				if(createStatement!=null){
					try {
						createStatement.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if(dbConn!=null){
					try {
						dbConn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		return resultMap;
	}
	
	
	
	@RequestMapping(value="parserOracle",produces="application/json;charset=UTF-8")
	public @ResponseBody Map<String, Object> parserOracle(HttpServletRequest request) throws IOException{
		 Map<String, Object> resultMap=new HashMap<String, Object>();  
		 Map<String, Object> queryMap=new HashMap<String, Object>(); 
		 Connection dbConn=null;
		 Statement createStatement=null;
		try
		  {
		   Map<String, String> parseRequest = HttpRequestUtils.parseRequest(request);
		   
		   String oracle_ip = parseRequest.get("oracle_ip");
		   String oracle_port = parseRequest.get("oracle_port");
		   String oracle_user = parseRequest.get("oracle_user");
		   String oracle_pwd = parseRequest.get("oracle_pwd");
		   String oracle_dbname = parseRequest.get("oracle_dbname");
		   String sql_query = parseRequest.get("sql_query");
		   Class.forName(ORACLE_DRIVER_NAME);
		   String dbURL="jdbc:oracle:" + "thin:@"+oracle_ip+":"+oracle_port+":"+oracle_dbname;
		   String userName=oracle_user;
		   String userPwd=oracle_pwd;
		   
		   //System.out.println(dbURL);
		   
		    dbConn=DriverManager.getConnection(dbURL,userName,userPwd);
		    createStatement = dbConn.createStatement();
		   ResultSet executeQuery = createStatement.executeQuery(sql_query);
		   // 表格的头信息
		   List<String> tableTitle = new ArrayList<String>();
		   //内容
		   List<Map<String,String>> bodyContent = new ArrayList<Map<String,String>>();
		   
		   //获取字段名信息
		   ResultSetMetaData metaData = executeQuery.getMetaData();
		   if(!Empty.isEmpty(metaData)){
			   queryMap.put("cells", metaData.getColumnCount());
			   for(int i=1;i<=metaData.getColumnCount();i++){
				   String columnName = metaData.getColumnName(i);
				   int columnType = metaData.getColumnType(i);
				   //System.out.println(columnName+":"+columnType);
				   tableTitle.add(columnName);
			   }
		   }
		    int rowCount =0;
		    DecimalFormat  df= new DecimalFormat("#.######");  
		    SimpleDateFormat timeformatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    SimpleDateFormat dateformatter = new SimpleDateFormat("yyyy/MM/dd");
		    while(executeQuery.next()){
		    	//保存每一列数据
		    	Map<String,String> cellMap=new HashMap<String,String>();
		    	rowCount++;
		    	if(!Empty.isEmpty(tableTitle)&&tableTitle.size()>0){
		    		//表格的表头信息
		    		for(int index=0;index<tableTitle.size();index++){
		    			String columuName=tableTitle.get(index);
		    			//如果是字符串
		    			if(Types.NVARCHAR==metaData.getColumnType(index+1)){
		    				cellMap.put(""+index+"", Empty.isEmpty(executeQuery.getString(columuName.toUpperCase()))?"":executeQuery.getString(columuName.toUpperCase()).trim());
		    			}else if(Types.DOUBLE==metaData.getColumnType(index+1)){
		    				cellMap.put(""+index+"", Empty.isEmpty(executeQuery.getDouble(columuName.toUpperCase()))?"":df.format(executeQuery.getDouble(columuName.toUpperCase())));
		    			}else if(Types.FLOAT==metaData.getColumnType(index+1)){
		    				cellMap.put(""+index+"", Empty.isEmpty(executeQuery.getFloat(columuName.toUpperCase()))?"":df.format(executeQuery.getFloat(columuName.toUpperCase())));
		    			}else if(Types.INTEGER==metaData.getColumnType(index+1)){
		    				cellMap.put(""+index+"", Empty.isEmpty(executeQuery.getInt(columuName.toUpperCase()))?"":String.valueOf(executeQuery.getInt(columuName.toUpperCase())));
		    			}else if(Types.DECIMAL==metaData.getColumnType(index+1)){
		    				cellMap.put(""+index+"", Empty.isEmpty(executeQuery.getBigDecimal(columuName.toUpperCase()))?"":df.format(executeQuery.getBigDecimal(columuName.toUpperCase())));
		    			}else if(Types.DATE==metaData.getColumnType(index+1)){
		    				cellMap.put(""+index+"", Empty.isEmpty(executeQuery.getDate(columuName.toUpperCase()))?"":dateformatter.format(executeQuery.getDate(columuName.toUpperCase())));
		    			}else if(Types.TIMESTAMP==metaData.getColumnType(index+1)){
		    				cellMap.put(""+index+"", Empty.isEmpty(executeQuery.getTimestamp(columuName.toUpperCase()))?"":timeformatter.format(executeQuery.getTimestamp(columuName.toUpperCase())));
		    			}else if(Types.CLOB==metaData.getColumnType(index+1)){
		    				Clob clob = executeQuery.getClob(columuName.toUpperCase());
		    				String subString = clob.getSubString(1, (int) clob.length());
		    				subString=subString.replace("\'", "");
		    				subString=subString.replace("\"", "");
		    				cellMap.put(""+index+"",subString);
		    			}else{
		    				//System.out.println(metaData.getColumnType(index+1));
		    				String subString = Empty.isEmpty(executeQuery.getString(columuName.toUpperCase()))?"":executeQuery.getString(columuName.toUpperCase()).trim();
		    				subString=subString.replace("\'", "");
		    				subString=subString.replace("\"", "");
		    				cellMap.put(""+index+"",subString);
		    			}
		    		}
		    	}
		    	bodyContent.add(cellMap);
		    }
		    queryMap.put("rows", rowCount);
		    queryMap.put("tableTitle", tableTitle);
		    queryMap.put("tableBody", bodyContent);
		    String jsonString = JSONObject.toJSONString(queryMap);
		    //System.out.println(jsonString);
		    String datafileurl="excel_temp_"+System.currentTimeMillis()+".json";
			String dir = request.getSession().getServletContext().getRealPath("/")+"/data/temp/"+datafileurl;
			JsonUtils.WriteConfigJson(dir, jsonString);
			resultMap.put("pageSize", ExcelUtils.pageNumbers);
			resultMap.put("tempfilename", datafileurl);
			resultMap.put("success", true);
		    
		    resultMap.put("jsondata",jsonString);
		    resultMap.put("message", "连接成功，正在查询...");
		  }catch (ClassNotFoundException e) {
			    resultMap.put("message", "数据库驱动程序问题");
				e.printStackTrace();
			}// 加载Oracle驱动程序
			catch (SQLException e) {
				String message = e.getMessage();
				if(message.indexOf("TNS:listener does not currently know of SID given in connect descriptor")>-1||message.indexOf("Unknown error 1049")>-1){//Unknown error 1049
					resultMap.put("message", "连接失败，该数据库实例不存在,请检查！"); 
				}else if(message.indexOf("invalid username/password; logon denied")>-1||message.indexOf("Unknown error 1045")>-1){
					resultMap.put("message", "连接失败，用户名或密码不正确,请检查！"); 
				}else if(message.indexOf("The Network Adapter could not establish the connection")>-1||message.indexOf("Communications link failure")>-1){
					resultMap.put("message", "连接失败，网络连接，端口或者IP不正确，请检查！"); 
				}else if(message.indexOf("No suitable driver found")>-1){
					resultMap.put("message", "数据库类型异常！"); 
				}else{
					resultMap.put("message", "连接异常，请检查各参数!"); 
				}
				e.printStackTrace();
			} catch (FileUploadException e) {
				e.printStackTrace();
			}finally {
				if(createStatement!=null){
					try {
						createStatement.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if(dbConn!=null){
					try {
						dbConn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		return resultMap;
	}
	
	
	@RequestMapping(value="excelmanagerSave")
	public @ResponseBody Map<String, Object> saveExcelManagerInfoSave(HttpServletRequest request) throws IOException{
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Map<String, String> parseRequest = HttpRequestUtils.parseRequest(request);
			
			String allowmaxrows=parseRequest.get("allowmaxrows");
			String pageNumbers=parseRequest.get("pageNumbers");
			if(!Empty.isEmpty(allowmaxrows) && !Empty.isEmpty(pageNumbers)){
				
				String menulist = FilePathUtils.getDataFilesRealPath(request)+"/data/excelmanager.json";
				String readJsonString = JsonUtils.readFileString(menulist);
				JSONObject oldParams=JSONObject.parseObject(readJsonString);
				if(oldParams.containsKey("allowmaxrows")){
					oldParams.fluentPut("allowmaxrows", allowmaxrows);
					ExcelUtils.allowmaxrows=Integer.parseInt(allowmaxrows);
				}
				if(oldParams.containsKey("pageNumbers")){
					oldParams.fluentPut("pageNumbers", pageNumbers);
					ExcelUtils.pageNumbers=Integer.parseInt(pageNumbers);
				}
				JsonUtils.WriteConfigJson(menulist, oldParams.toJSONString());
				map.put("message", "保存成功");
			}else{
				map.put("message", "保存失败");
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	
}
