package com.app.timer.business.controller;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.app.timer.business.util.AppConstant;
import com.app.timer.business.util.Empty;
import com.app.timer.business.util.FtpFileUpload;
import com.app.timer.business.util.HttpRequestUtils;
import com.app.timer.business.util.RandomGUID;
import com.app.timer.business.util.StringUtil;
import com.app.timer.utils.ExcelUtils;
import com.app.timer.utils.FilePathUtils;
import com.app.timer.utils.JsonUtils;
import com.app.timer.utils.PicUtils;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * 图表操作类
 * @author liuxf
 *
 */
@Controller
@RequestMapping("/charts")
public class ChartsController {

	/**
	 * 柱图
	 */
	public static final String CHART_TYPE_ZT="zt";
	/**
	 * 饼图
	 */
	public static final String CHART_TYPE_BT="bt";
	/**
	 * 折线图
	 */
	public static final String CHART_TYPE_ZXT="zxt";
	/**
	 * 柱状K线组合图
	 */
	public static final String CHART_TYPE_ZKT="zkt";
	/**
	 * 仪表盘
	 */
	public static final String CHART_TYPE_YBP="ybp";
	
	
	@RequestMapping("/show")
	public ModelAndView goList(HttpServletRequest request){
		ModelAndView mView=new ModelAndView("charts/charts");
		String filename = request.getParameter("filename");
		//System.out.println("filename:"+filename);
		if(!Empty.isEmpty(filename)){
			mView.addObject("filename", filename);
			String dir = FilePathUtils.getDataFilesRealPath(request)+"/data/menu/"+filename;
			String readFileString = JsonUtils.readFileString(dir);
			if(!Empty.isEmpty(readFileString)){
				JSONObject json=JSONObject.parseObject(readFileString);
				if(!Empty.isEmpty(json)){
					//从json文件里查找列数组
					JSONArray object = (JSONArray) json.get("tableTitle");
					mView.addObject("xycells", object);
				}
			}
		}
		return mView;
	}
	
	
	@RequestMapping("/chartslist")
	public ModelAndView gochartslist(HttpServletRequest request){
		ModelAndView mView=new ModelAndView("charts/chartslist");
		//所有报表图片的URL集合
		String chartImagesList = FilePathUtils.getDataFilesRealPath(request)+"/data/imageslist.json";
		String readFileString = JsonUtils.readFileString(chartImagesList);
		JSONArray array=JSONArray.parseArray(readFileString);
		mView.addObject("imageArray", JsonUtils.sortJsonArrayRelect(array));
		mView.addObject("imageSize", array.size());
		if(AppConstant.CHART_PIC_IS_PUT_OUT){//保存的图表放外部的HTTP服务器
			mView.addObject("HTTP_VIEW_WORKPATH", AppConstant.getValue("FILE_VIEW_PATH"));
		}else{
			mView.addObject("HTTP_VIEW_WORKPATH", "./images/charts/");
		}
		return mView;
	}
	
	
	@RequestMapping("/chartsReview")
	public ModelAndView chartsReview(HttpServletRequest request){
		ModelAndView mView=new ModelAndView("charts/charts_review");
		try {
			Map<String, String> parseRequest = HttpRequestUtils.parseRequest(request);
			String filename = parseRequest.get("filename");
			String dir = FilePathUtils.getDataFilesRealPath(request)+"/data/menu/" + filename;
			String readFileString = JsonUtils.readFileString(dir);
			String readFileStrin2=readFileString.replace("\"", "\'");
			//System.out.println(readFileStrin2);
			JSONArray array=JSONArray.parseArray(readFileStrin2);
			mView.addObject("picDatas", array);
			mView.addObject("filename", filename);
			mView.addObject("HTTP_VIEW_WORKPATH", AppConstant.getValue("FILE_VIEW_PATH"));
		} catch (FileUploadException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mView;
	}
	
	@RequestMapping(value="review",produces="application/json;charset=UTF-8")
	public @ResponseBody Map<String, Object> parserExcel(HttpServletRequest request) throws IOException{
		Map<String, Object> map=new HashMap<String, Object>();
	    try {
			Map<String, String> parseRequest = HttpRequestUtils.parseRequest(request);
			//数据文件
			String filename = parseRequest.get("filename");
			//图表类型
			String chartsType = parseRequest.get("chart_type");
			//如果是柱图
			if (ChartsController.CHART_TYPE_ZT.equals(chartsType) && !Empty.isEmpty(filename)) {
				map.put("chartType", ChartsController.CHART_TYPE_ZT);
				String xcell = parseRequest.get("x_data");
				String ycell = parseRequest.get("y_data");
				//是否开启两个Y开关参数
				String zt_mutiYswitch = parseRequest.get("zt_mutiYswitch");
				//第二个Y轴字段
				String zt_y2_data = parseRequest.get("zt_y2_data");
				
				int xcellIndex = Integer.valueOf(xcell);
				int ycellIndex = Integer.valueOf(ycell);
				String dir = FilePathUtils.getDataFilesRealPath(request)+"/data/menu/" + filename;
				String readFileString = JsonUtils.readFileString(dir);
				// X轴的数据
				List<String> xlist = new ArrayList<String>();
				// Y轴的数据
				List<String> ylist = new ArrayList<String>();
				// Y2轴的数据
				List<String> y2list = new ArrayList<String>();
				// 图例的数据
				List<String> legendlist = new ArrayList<String>();
				if (!Empty.isEmpty(readFileString)) {
					JSONObject json = JSONObject.parseObject(readFileString);
					if (!Empty.isEmpty(json)) {
						// 从json文件里查找列数组
						//System.out.println("x:" + xcellIndex);
						//System.out.println("y:" + ycellIndex);
						JSONArray bodyArray = (JSONArray) json.get("tableBody");
						JSONArray titleArray = (JSONArray) json.get("tableTitle");
						if(!Empty.isEmpty(titleArray)){
							String legend1 = (String) titleArray.get(ycellIndex);
							legendlist.add(legend1);
							//判断是否开启多Y
							if(!Empty.isEmpty(zt_mutiYswitch) && "on".equals(zt_mutiYswitch)){
								int ycellIndex2 = Integer.valueOf(zt_y2_data);
								//System.out.println(zt_y2_data);
								String legend2 = (String) titleArray.get(ycellIndex2);
								legendlist.add(legend2);
							}
						}
						for (int i = 0; i < bodyArray.size(); i++) {
							JSONObject jsonObj = (JSONObject) bodyArray.get(i);
							//System.out.println(jsonObj.toJSONString());
							String xcl = (String) jsonObj.get("" + xcellIndex + "");
							xlist.add(xcl);
							String ycl = (String) jsonObj.get("" + ycellIndex + "");
							ylist.add(ycl);
							if(!Empty.isEmpty(zt_mutiYswitch) && "on".equals(zt_mutiYswitch)){
								int ycellIndex2 = Integer.valueOf(zt_y2_data);
								String ycl2 = (String) jsonObj.get("" + ycellIndex2 + "");
								y2list.add(ycl2);
							}
						}
						map.put("xdata", xlist);
						map.put("ydata", ylist);
						map.put("y2data", y2list);
						map.put("legend", legendlist);
					}
				}
			}else if(ChartsController.CHART_TYPE_BT.equals(chartsType) && !Empty.isEmpty(filename)){//饼图
				map.put("chartType", ChartsController.CHART_TYPE_BT);
				String xcell = parseRequest.get("x_data");
				String ycell = parseRequest.get("y_data");
				int xcellIndex = Integer.valueOf(xcell);
				int ycellIndex = Integer.valueOf(ycell);
				String dir = FilePathUtils.getDataFilesRealPath(request)+"/data/menu/" + filename;
				String readFileString = JsonUtils.readFileString(dir);
				
				// 图例的数据
				List<String> legendlist = new ArrayList<String>();
				if (!Empty.isEmpty(readFileString)) {
					JSONObject json = JSONObject.parseObject(readFileString);
					if (!Empty.isEmpty(json)) {
						// 从json文件里查找列数组
						JSONArray bodyArray = (JSONArray) json.get("tableBody");
						JSONArray titleArray = (JSONArray) json.get("tableTitle");
						//饼图series数据区域
						JSONArray dataArray = new JSONArray();
						
						for (int i = 0; i < bodyArray.size(); i++) {
							JSONObject jsonObj = (JSONObject) bodyArray.get(i);
							String xcl = (String) jsonObj.get("" + xcellIndex + "");
							String ycl = (String) jsonObj.get("" + ycellIndex + "");
							//饼图series数据区域数据项
							JSONObject dataObject = new JSONObject();
							dataObject.put("value", ycl);
							dataObject.put("name", xcl);
							dataArray.add(dataObject);
							legendlist.add(xcl);
						}
						map.put("xdata", legendlist);
						map.put("ydata", dataArray);
						map.put("legend", legendlist);
					}
				}
			}else if(ChartsController.CHART_TYPE_ZXT.equals(chartsType) && !Empty.isEmpty(filename)){//折线图
				map.put("chartType", ChartsController.CHART_TYPE_ZXT);
				String xcell = parseRequest.get("x_data");
				String ycell = parseRequest.get("y_data");
				int xcellIndex = Integer.valueOf(xcell);
				int ycellIndex = Integer.valueOf(ycell);
				String dir = FilePathUtils.getDataFilesRealPath(request)+"/data/menu/" + filename;
				String readFileString = JsonUtils.readFileString(dir);
				// X轴的数据
				List<String> xlist = new ArrayList<String>();
				// Y轴的数据
				List<String> ylist = new ArrayList<String>();
				// 图例的数据
				List<String> legendlist = new ArrayList<String>();
				if (!Empty.isEmpty(readFileString)) {
					JSONObject json = JSONObject.parseObject(readFileString);
					if (!Empty.isEmpty(json)) {
						// 从json文件里查找列数组
						JSONArray bodyArray = (JSONArray) json.get("tableBody");
						JSONArray titleArray = (JSONArray) json.get("tableTitle");
						
						//饼图series数据区域数据项
						JSONObject dataObject = new JSONObject();
						dataObject.put("type", "line");
						dataObject.put("stack", "总量");
						if(!Empty.isEmpty(titleArray)){
							String legend = (String) titleArray.get(ycellIndex);
							dataObject.put("name", legend);
							legendlist.add(legend);
						}
						
						for (int i = 0; i < bodyArray.size(); i++) {
							JSONObject jsonObj = (JSONObject) bodyArray.get(i);
							//System.out.println(jsonObj.toJSONString());
							String xcl = (String) jsonObj.get("" + xcellIndex + "");
							xlist.add(xcl);
							String ycl = (String) jsonObj.get("" + ycellIndex + "");
							ylist.add(ycl);
						}
						dataObject.put("data", ylist);
						map.put("xdata", xlist);
						map.put("ydata", dataObject);
						map.put("legend", legendlist);
					}
				}
			}//如果是柱K图
			else if (ChartsController.CHART_TYPE_ZKT.equals(chartsType) && !Empty.isEmpty(filename)) {
				map.put("chartType", ChartsController.CHART_TYPE_ZKT);
				String xcell = parseRequest.get("x_data");
				String ycell = parseRequest.get("y_data");
				//是否开启两个Y开关参数
				String zt_mutiYswitch = parseRequest.get("zkt_mutiYswitch");
				//第二个Y轴字段
				String zt_y2_data = parseRequest.get("zkt_y2_data");
				
				int xcellIndex = Integer.valueOf(xcell);
				int ycellIndex = Integer.valueOf(ycell);
				String dir = FilePathUtils.getDataFilesRealPath(request)+"/data/menu/" + filename;
				String readFileString = JsonUtils.readFileString(dir);
				// X轴的数据
				List<String> xlist = new ArrayList<String>();
				// Y轴的数据
				List<String> ylist = new ArrayList<String>();
				// Y2轴的数据
				List<String> y2list = new ArrayList<String>();
				// 图例的数据
				List<String> legendlist = new ArrayList<String>();
				if (!Empty.isEmpty(readFileString)) {
					JSONObject json = JSONObject.parseObject(readFileString);
					if (!Empty.isEmpty(json)) {
						// 从json文件里查找列数组
						//System.out.println("x:" + xcellIndex);
						//System.out.println("y:" + ycellIndex);
						JSONArray bodyArray = (JSONArray) json.get("tableBody");
						JSONArray titleArray = (JSONArray) json.get("tableTitle");
						if(!Empty.isEmpty(titleArray)){
							String legend1 = (String) titleArray.get(ycellIndex);
							legendlist.add(legend1);
							//判断是否开启多Y
							if(!Empty.isEmpty(zt_mutiYswitch) && "on".equals(zt_mutiYswitch)){
								int ycellIndex2 = Integer.valueOf(zt_y2_data);
								//System.out.println(zt_y2_data);
								String legend2 = (String) titleArray.get(ycellIndex2);
								legendlist.add(legend2);
							}
						}
						for (int i = 0; i < bodyArray.size(); i++) {
							JSONObject jsonObj = (JSONObject) bodyArray.get(i);
							//System.out.println(jsonObj.toJSONString());
							String xcl = (String) jsonObj.get("" + xcellIndex + "");
							xlist.add(xcl);
							String ycl = (String) jsonObj.get("" + ycellIndex + "");
							ylist.add(ycl);
							if(!Empty.isEmpty(zt_mutiYswitch) && "on".equals(zt_mutiYswitch)){
								int ycellIndex2 = Integer.valueOf(zt_y2_data);
								String ycl2 = (String) jsonObj.get("" + ycellIndex2 + "");
								y2list.add(ycl2);
							}
						}
						map.put("xdata", xlist);
						map.put("ydata", ylist);
						map.put("y2data", y2list);
						map.put("legend", legendlist);
					}
				}
				
				
			}else if (ChartsController.CHART_TYPE_YBP.equals(chartsType) && !Empty.isEmpty(filename)) {//如果是仪表盘
				map.put("chartType", ChartsController.CHART_TYPE_YBP);
				String xcell = parseRequest.get("x_data");
				String ycell = parseRequest.get("y_data");
				//X除以Y的含义  即仪表盘指针位置含义
				String ybp_xdividey = parseRequest.get("ybp_xdividey");
				int xcellIndex = Integer.valueOf(xcell);
				int ycellIndex = Integer.valueOf(ycell);
				String dir = FilePathUtils.getDataFilesRealPath(request)+"/data/menu/" + filename;
				String readFileString = JsonUtils.readFileString(dir);
				// 仪表盘的数据
				List<Map<String,Object>> ybpdata = new ArrayList<Map<String,Object>>();
				// 图例的数据
				List<String> legendlist = new ArrayList<String>();
				if (!Empty.isEmpty(readFileString)) {
					JSONObject json = JSONObject.parseObject(readFileString);
					if (!Empty.isEmpty(json)) {
						// 从json文件里查找列数组
						//System.out.println("x:" + xcellIndex);
						//System.out.println("y:" + ycellIndex);
						JSONArray bodyArray = (JSONArray) json.get("tableBody");
						JSONArray titleArray = (JSONArray) json.get("tableTitle");
						if(!Empty.isEmpty(titleArray)){
							String legend = (String) titleArray.get(ycellIndex);
							legendlist.add(legend);
						}
						for (int i = 0; i < bodyArray.size(); i++) {
							JSONObject jsonObj = (JSONObject) bodyArray.get(i);
							//System.out.println(jsonObj.toJSONString());
							Map<String,Object> valmap=new HashMap<String,Object>();
							String xcl = (String) jsonObj.get("" + xcellIndex + "");
							
							String ycl = (String) jsonObj.get("" + ycellIndex + "");
							
							//x y都为数字才有意义
							if(isNumeric(xcl) && isNumeric(ycl)){
								Double xvalueOf = Double.valueOf(xcl);
								Double yvalueOf = Double.valueOf(ycl);
								Double double1 = xvalueOf/yvalueOf;
								String substring = String.valueOf(double1*100);
								int indexOf = substring.indexOf(".");
								//System.out.println(indexOf);
								valmap.put("value", substring.substring(0, indexOf));
								valmap.put("name", ybp_xdividey);
								ybpdata.add(valmap);
							}
							
						}
						map.put("legend", legendlist);
						map.put("data", ybpdata);
					}
				
				}
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	@RequestMapping(value="genCharts",produces="application/json;charset=UTF-8")
	public @ResponseBody Map<String, Object> genCharts(HttpServletRequest request) throws IOException{
		Map<String, Object> map=new HashMap<String, Object>();
		
	    try {
			Map<String, String> parseRequest = HttpRequestUtils.parseRequest(request);
			String chartsdata = parseRequest.get("chartsdata");
			String charts_menu_name = parseRequest.get("charts_menu_name");
			
			String datafileurl="datalist_"+System.currentTimeMillis()+".json";
			String dataFile = FilePathUtils.getDataFilesRealPath(request)+"/data/menu/"+datafileurl;
			JsonUtils.WriteConfigJson(dataFile, chartsdata);
			
			String chartslist = FilePathUtils.getDataFilesRealPath(request)+"/data/chartslist.json";
			String readFileString = JsonUtils.readFileString(chartslist);
			JSONArray array=JSONArray.parseArray(readFileString);
			JSONObject menuData=JSONObject.parseObject("{url:'charts/chartsReview',filename:'"+datafileurl+"',type:'chartsData',menuChartName:'"+charts_menu_name+"'}");
			array.add(menuData);
			JsonUtils.WriteConfigJson(chartslist, array.toJSONString());
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
		
		map.put("message", "组合报表已经生成");
		return map;
	}
	
	/**
	 * 删除已经生成的图表图片
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="delCharts",produces="application/json;charset=UTF-8")
	public @ResponseBody Map<String, Object> delCharts(HttpServletRequest request) throws IOException{
		Map<String, Object> map=new HashMap<String, Object>();
	    try {
			String[] delPics = request.getParameterValues("delPics");
			if(!Empty.isEmpty(delPics)){
				String chartslist = FilePathUtils.getDataFilesRealPath(request)+"/data/imageslist.json";
				String readFileString = JsonUtils.readFileString(chartslist);
				//获取图片的存储路径
				String chartImages = "";
				if(AppConstant.CHART_PIC_IS_PUT_OUT){//保存的图表放外部的HTTP服务器
					chartImages = AppConstant.EXP_Chart_ImagePath;
				}else{//图表图片放在应用服务器的下面的特殊目录
					chartImages=request.getSession().getServletContext().getRealPath("/images/charts");
				}
				if (!Empty.isEmpty(readFileString) && readFileString.trim().startsWith("[")
						&& readFileString.trim().endsWith("]")) {
					JSONArray userArray = JSONArray.parseArray(readFileString);
					for(String filename:delPics){
						for (int i = 0; i < userArray.size(); i++) {
							JSONObject object = (JSONObject) userArray.get(i);
							if (object.containsKey("filename") && filename.equals(object.getString("filename"))) {
								userArray.remove(i);
								//删除对应的图片文件
								PicUtils.deleteImageFile(chartImages, filename);
								break;
							}
						}
					}
				JsonUtils.WriteConfigJson(chartslist, userArray.toJSONString());
			  }
				map.put("message", "报表图片删除成功!");
			}else{
				map.put("message", "请选择报表图片重试删除一次!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("message", "报表图片删除失败!");
		}
		return map;
	}
	
	/**
	 * 将图表保存为图片
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="genChartPic",produces="application/json;charset=UTF-8")
	public @ResponseBody Map<String, Object> genChartsPic(HttpServletRequest request) throws IOException{
		Map<String, Object> map=new HashMap<String, Object>();
		
	    try {
			Map<String, String> parseRequest = HttpRequestUtils.parseRequest(request);
			String baseimg = parseRequest.get("baseimg");
			String option = parseRequest.get("option");
			
			//System.out.println(option);
			
			//传递过正中  "+" 变为了 " "  
			baseimg=baseimg.replaceAll(" ", "+"); 
			String[] split = baseimg.split("base64,");
			//String chartImages = request.getSession().getServletContext().getRealPath("/images/charts");
			String chartImages = "";
			if(AppConstant.CHART_PIC_IS_PUT_OUT){//保存的图表放外部的HTTP服务器
				chartImages = AppConstant.EXP_Chart_ImagePath;
			}else{//图表图片放在应用服务器的下面的特殊目录
				chartImages=request.getSession().getServletContext().getRealPath("/images/charts");
			}
			String imageName="chart_image_"+System.currentTimeMillis()+".jpg";
			PicUtils.generateImage(split[1], chartImages+"/"+imageName);
			
			//所有报表图片的URL集合
			String chartImagesList = FilePathUtils.getDataFilesRealPath(request)+"/data/imageslist.json";
			String readFileString = JsonUtils.readFileString(chartImagesList);
			JSONArray array=JSONArray.parseArray(readFileString);
			SimpleDateFormat dF=new SimpleDateFormat("yyyy-MM-dd");
			String dnformat = dF.format(new Date());
			JSONObject menuData=JSONObject.parseObject("{url:'"+imageName+"',filename:'"+imageName+"',type:'image',option:'"+option+"',date:'"+dnformat+"'}");
			array.add(menuData);
			JsonUtils.WriteConfigJson(chartImagesList, array.toJSONString());
			map.put("message", "图表的保存成功");
		} catch (FileUploadException e) {
			e.printStackTrace();
			map.put("message", "图表的保存异常，请联系管理员");
		}
		
		return map;
	}
	
	
	/**
	 * 删除已经生成的组合图表页面
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="delChartsShowPage",produces="application/json;charset=UTF-8")
	public @ResponseBody Map<String, Object> delChartsShowPage(HttpServletRequest request) throws IOException{
		Map<String, Object> map=new HashMap<String, Object>();
	    try {
			String filename = request.getParameter("filename");
			if(!Empty.isEmpty(filename)){
				String chartslist = FilePathUtils.getDataFilesRealPath(request)+"/data/chartslist.json";
				String readFileString = JsonUtils.readFileString(chartslist);
				if (!Empty.isEmpty(readFileString) && readFileString.trim().startsWith("[")
						&& readFileString.trim().endsWith("]")) {
					JSONArray chartsShowArray = JSONArray.parseArray(readFileString);
						for (int i = 0; i < chartsShowArray.size(); i++) {
							JSONObject object = (JSONObject) chartsShowArray.get(i);
							if (object.containsKey("filename") && filename.equals(object.getString("filename"))) {
								//删除对应的JSO你对象
								chartsShowArray.remove(i);
								break;
							}
						}
				JsonUtils.WriteConfigJson(chartslist, chartsShowArray.toJSONString());
				String datadir = FilePathUtils.getDataFilesRealPath(request)+"/data/menu";
				JsonUtils.deleteJsonFile(datadir, filename);
			  }
				map.put("message", "组合图表页面已删除!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("message", "组合图表页面删除失败!");
		}
		return map;
	}
	
	public boolean isNumeric(String str){ 
		   Pattern pattern = Pattern.compile("^[-+]?\\d+(\\.\\d+)?$"); 
		   Matcher isNum = pattern.matcher(str);
		   if( !isNum.matches() ){
		       return false; 
		   } 
		   return true; 
	}
	
	
	public static void main(String[] args) {
		
	}
	
}
