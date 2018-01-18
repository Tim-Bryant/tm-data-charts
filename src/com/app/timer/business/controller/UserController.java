package com.app.timer.business.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.lang3.StringUtils;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.app.timer.business.pojo.User;
import com.app.timer.business.util.Empty;
import com.app.timer.business.util.HttpRequestUtils;
import com.app.timer.business.util.UserUtils;
import com.app.timer.utils.ExcelUtils;
import com.app.timer.utils.FilePathUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author 作者 E-mail:xiaofeng09happy@sina.com
 * @version 创建时间：2017年11月12日 下午9:08:40 类说明:人员信息控制类
 */
@Controller
@RequestMapping("/user")
public class UserController {

	@RequestMapping("/userlist")
	public ModelAndView goList(HttpServletRequest request) {
		ModelAndView mView = new ModelAndView("user/userlist");
		return mView;
	}

	@RequestMapping("/register")
	public ModelAndView goRegister(HttpServletRequest request) {
		ModelAndView mView = new ModelAndView("user/register");
		return mView;
	}

	@RequestMapping(value = "/save", produces = "application/json;charset=UTF-8")
	public @ResponseBody Map<String, Object> userSave(HttpServletRequest request) {
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			Map<String, String> parseRequest = HttpRequestUtils.parseRequest(request);
			String username = parseRequest.get("username");
			//用于判断是否新增还是修改
			String isnewUser = parseRequest.get("isnewUser");
			String pwd = parseRequest.get("pwd");
			String role = parseRequest.get("role");
			String description = parseRequest.get("description");
			//为空表示新增用户
			if(Empty.isEmpty(isnewUser)){
				// 必要参数不可以为空
				if (!Empty.isEmpty(username) && !Empty.isEmpty(pwd) && !Empty.isEmpty(role)) {
					String userpath = FilePathUtils.getDataFilesRealPath(request)+"/data/user.json";
					User user=new User(username, pwd, role, description);
					//用户不存在才添加
					if(!UserUtils.userIsExist(userpath, username)){
						UserUtils.addUser(userpath, user);
						map.put("message", "用户【"+username+"】添加成功！");
						map.put("isadd",true);
					}else{
						map.put("message", "用户【"+username+"】已经存在，不可以重复添加！");
						map.put("isadd",false);
					}
				}
			}else{//表示修改用户
				// 必要参数不可以为空
				if (!Empty.isEmpty(username) && !Empty.isEmpty(pwd) && !Empty.isEmpty(role)) {
					String userpath = FilePathUtils.getDataFilesRealPath(request)+"/data/user.json";
					//对于文件来说 修改就是先删除在新增
					boolean deleteUser = UserUtils.deleteUser(userpath, isnewUser);
					//删除成功
					if(deleteUser){
						User user=new User(username, pwd, role, description);
						if(!UserUtils.userIsExist(userpath, username)){
							boolean addUser = UserUtils.addUser(userpath, user);
							if(addUser){
								map.put("message", "用户【"+username+"】修改成功！");
								map.put("isadd",true);
							}else{
								map.put("message", "用户【"+username+"】修改失败，请联系管理员！");
								map.put("isadd",false);
							}
						}else{
							map.put("message", "用户【"+username+"】修改失败，请联系管理员！");
							map.put("isadd",false);
						}
					}
				}
			}
		} catch (FileUploadException | IOException e) {
			e.printStackTrace();
		}

		return map;
	}
	
	@RequestMapping(value="/listusers")
	public @ResponseBody Map<String, Object> goListData(HttpServletRequest request){
		Map<String, Object> resultmap=new HashMap<String, Object>();
		try {
			String userpath = FilePathUtils.getDataFilesRealPath(request)+"/data/user.json";
			List<User> listAllUsers = UserUtils.listAllUsers(userpath);
	        resultmap.put("list", listAllUsers);
		} catch (Exception e) {
			e.printStackTrace();
		}
		resultmap.put("pageSize", 10);
		return resultmap;
	}
	
	
	@RequestMapping(value="/detail/{username}",method=RequestMethod.GET)
	public ModelAndView detail(@PathVariable(value = "username") String username,HttpServletRequest request) throws Exception{
		ModelAndView mView=new ModelAndView("user/register");
		if(!StringUtils.isBlank(username)){
			String userpath = FilePathUtils.getDataFilesRealPath(request)+"/data/user.json";
			User user = UserUtils.getUser(userpath, username);
			mView.addObject("user", user);
		}
		return mView;
	}
	
	
	@RequestMapping("/delete")
	public @ResponseBody Map<String, Object> delete(HttpServletRequest request) throws Exception{
		Map<String, Object> map=new HashMap<String, Object>();
		String users = request.getParameter("users");
		//记录删除的条数
		int delcounts=0;
		String userpath = FilePathUtils.getDataFilesRealPath(request)+"/data/user.json";
		if(!StringUtils.isBlank(users)){
			String[] unameArray = users.split(",");
			for(String uname:unameArray){
				boolean deleteUser = UserUtils.deleteUser(userpath, uname);
				if(deleteUser){
					delcounts++;
				}
			}
		}
		map.put("message", "成功删除【"+delcounts+"】条记录");
		return map;
	}
	
	
}
