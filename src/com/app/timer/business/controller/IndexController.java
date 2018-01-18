package com.app.timer.business.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileUploadException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.app.timer.business.pojo.User;
import com.app.timer.business.util.Empty;
import com.app.timer.business.util.HttpRequestUtils;
import com.app.timer.business.util.SessionData;
import com.app.timer.business.util.UserUtils;
import com.app.timer.utils.FilePathUtils;
import com.app.timer.utils.JsonUtils;
import com.app.timer.utils.WriteJsonFile;
import com.google.gson.JsonArray;

/**
 * 定时任务对象的控制层
 * @author liuxf
 *
 */
@Controller
public class IndexController {

	@RequestMapping("/login")
	public ModelAndView goLogin(HttpServletRequest request){
		ModelAndView mv=new ModelAndView("login");
		 //跟路径
        String ctxPath = request.getContextPath();
        mv.addObject("ctx", ctxPath);
		return mv;
	}
	
	/**
	 * 验证登录
	 * @return
	 */
	@RequestMapping("/validateLogin")
	public ModelAndView goValidate(HttpServletRequest request){
		ModelAndView mv=new ModelAndView();
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if(!Empty.isEmpty(username)&&!Empty.isEmpty(password)){
			String dir = FilePathUtils.getDataFilesRealPath(request)+"/data/user.json";
			String readFileString = JsonUtils.readFileString(dir);
			JSONArray array=JSONArray.parseArray(readFileString);
			for (int i = 0; i < array.size(); i++) {
	            JSONObject user = (JSONObject) array.get(i);
	            if(user.getString("username").equals(username)&&user.getString("pwd").equals(password)){
	            	mv.setViewName("redirect:/index");
	            	SessionData sessionData = SessionData.getSessionData(request);
	            	User currentUser = UserUtils.getUser(dir, username);
	            	sessionData.setUser(currentUser);
	            	return mv;
	            }
	        }
		}
		mv.setViewName("login");
		mv.addObject("message", "用户名或密码不正确");
		return mv;
	}
	
	
	/**
	 * 退出登录
	 * @return
	 */
	@RequestMapping("/loginout")
	public ModelAndView goLoginOut(HttpServletRequest request){
		ModelAndView mv=new ModelAndView();
    	SessionData sessionData = SessionData.getSessionData(request);
    	sessionData.clear(request);
    	mv.setViewName("redirect:/login");
		return mv;
	}
	
	
	@RequestMapping("/index")
	public ModelAndView goIndex(HttpServletRequest request){
		ModelAndView mv=new ModelAndView("index");
		//String dir = request.getSession().getServletContext().getRealPath("/data/datalist.json");
		//WriteJsonFile.WriteConfigJson(dir, "[{id:1,name:test},{id:2,name:tttt}]");
		String userdata = FilePathUtils.getDataFilesRealPath(request)+"/data/user.json";
		SessionData sessionData = SessionData.getSessionData(request);
		User user = sessionData.getUser();
    	mv.addObject("currentUser", user);
    	//System.out.println(user.getUsername()+":已经登陆了系统");
    	String dir = FilePathUtils.getDataFilesRealPath(request)+"/data/menu";
    	//System.out.println("dir:"+dir);
    	File file = new File(dir);//File类型可以是文件也可以是文件夹
        File[] fileList = file.listFiles();//将该目录下的所有文件放置在一个File类型的数组中
        List<File> wjList = new ArrayList<File>();//新建一个文件集合
        List<String> fileNameList = new ArrayList<String>();//文件列表集合
        if(!Empty.isEmpty(fileList)&&fileList.length>0){
        	for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isFile()) {//判断是否为文件
                     wjList.add(fileList[i]);
                }
             }
             for (int i = 0; i < wjList.size(); i++) {
             	File tempfile = wjList.get(i);
             	//System.out.println(tempfile.getName());
             	fileNameList.add(tempfile.getName());
             }
        }
        mv.addObject("filelist", fileNameList);
        //已经导入数据菜单的查询
        List menuList = new ArrayList();//菜单列表集合
        String menulist = FilePathUtils.getDataFilesRealPath(request)+"/data/menulist.json";
        String readFileString = JsonUtils.readFileString(menulist);
        JSONArray array=JSONArray.parseArray(readFileString);
        for(int i=0;i<array.size();i++){
        	JSONObject jsonObject = array.getJSONObject(i);
        	menuList.add(jsonObject);
        }
        mv.addObject("menulist", menuList);
        //报表菜单的查询
        String dirChartMenu = FilePathUtils.getDataFilesRealPath(request)+"/data/chartslist.json";
		String readFileChart= JsonUtils.readFileString(dirChartMenu);
		List menuChartList = new ArrayList();//已生成的报表集合
        JSONArray arrayChartMenu=JSONArray.parseArray(readFileChart);
        for(int i=0;i<arrayChartMenu.size();i++){
        	JSONObject jsonObject = arrayChartMenu.getJSONObject(i);
        	menuChartList.add(jsonObject);
        }
        mv.addObject("menuChartList", menuChartList);
		return mv;
	}
	
	@RequestMapping(value="/upload/file",produces="application/json;charset=UTF-8")
	public @ResponseBody Map<String, Object> uploadFile(HttpServletRequest request){
		Map<String, Object> map=new HashMap<String, Object>();
		//需要用到GSOn你转换器否则前台无法解析
		//JsonObject jsonObject=new JsonObject();
			try {
				Map<String, String> params =HttpRequestUtils.parseRequestByMultipartResolver(request);
				map.put("data", params);
			} catch (FileUploadException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		map.put("code", "0");
		map.put("msg", "上传成功了");
		return map;
	}
	
}
