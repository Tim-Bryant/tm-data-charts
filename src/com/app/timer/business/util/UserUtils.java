package com.app.timer.business.util;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.app.timer.business.pojo.User;
import com.app.timer.utils.JsonUtils;

/**
 * @author 作者 E-mail:xiaofeng09happy@sina.com
 * @version 创建时间：2017年11月12日 下午8:01:22 类说明:从JSON文件获取USER对象
 */
public class UserUtils {

	/**
	 * 根据用户名---获取用户
	 * 
	 * @param dir
	 * @return
	 */
	public static User getUser(String dir, String username) {
		User user = null;
		String readFileString = JsonUtils.readFileString(dir);
		if (!Empty.isEmpty(readFileString) && readFileString.trim().startsWith("[")
				&& readFileString.trim().endsWith("]")) {
			JSONArray userArray = JSONArray.parseArray(readFileString);
			for (int i = 0; i < userArray.size(); i++) {
				JSONObject userJson = userArray.getJSONObject(i);
				if (!Empty.isEmpty(userJson)) {
					if (userJson.containsKey("username") && userJson.containsKey("pwd")
							&& userJson.containsKey("role")) {
						if (username.equals(userJson.getString("username"))) {
							String uname = userJson.getString("username");
							String pwd = userJson.getString("pwd");
							String role = userJson.getString("role");
							String des = userJson.getString("description");
							user = new User(uname, pwd, role, des);
						}
					}
				}
			}
		}
		return user;
	}

	/**
	 * 添加新用户
	 * 
	 * @param dir
	 * @return
	 */
	public static boolean addUser(String dir, User user) {

		String readFileString = JsonUtils.readFileString(dir);
		if (!Empty.isEmpty(readFileString) && readFileString.trim().startsWith("[")
				&& readFileString.trim().endsWith("]")) {
			JSONArray userArray = JSONArray.parseArray(readFileString);
			String jsonString = JSONObject.toJSONString(user);
			JSONObject userObj = JSONObject.parseObject(jsonString);
			userArray.add(userObj);
			JsonUtils.WriteConfigJson(dir, userArray.toJSONString());
			return true;
		}
		return false;
	}

	/**
	 * 删除用户
	 * 
	 * @param dir
	 * @return
	 */
	public static boolean deleteUser(String dir, String username) {
		String readFileString = JsonUtils.readFileString(dir);
		if (!Empty.isEmpty(readFileString) && readFileString.trim().startsWith("[")
				&& readFileString.trim().endsWith("]")) {
			JSONArray userArray = JSONArray.parseArray(readFileString);
			for (int i = 0; i < userArray.size(); i++) {
				JSONObject object = (JSONObject) userArray.get(i);
				if (object.containsKey("username") && username.equals(object.getString("username"))) {
					userArray.remove(i);
					break;
				}

			}
			JsonUtils.WriteConfigJson(dir, userArray.toJSONString());
			return true;
		}
		return false;
	}

	/**
	 * 用户是否存在
	 * 
	 * @param dir
	 * @return
	 */
	public static boolean userIsExist(String dir, String username) {
		String readFileString = JsonUtils.readFileString(dir);
		if (!Empty.isEmpty(readFileString) && readFileString.trim().startsWith("[")
				&& readFileString.trim().endsWith("]")) {
			JSONArray userArray = JSONArray.parseArray(readFileString);
			for (int i = 0; i < userArray.size(); i++) {
				JSONObject object = (JSONObject) userArray.get(i);
				if (object.containsKey("username") && username.equals(object.getString("username"))) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 获取用户列表
	 * 
	 * @param dir
	 * @return
	 */
	public static List<User> listAllUsers(String dir) {
		List<User> list = new ArrayList<User>();
		String readFileString = JsonUtils.readFileString(dir);
		if (!Empty.isEmpty(readFileString) && readFileString.trim().startsWith("[")
				&& readFileString.trim().endsWith("]")) {
			JSONArray userArray = JSONArray.parseArray(readFileString);
			for (int i = 0; i < userArray.size(); i++) {
				JSONObject userJson = userArray.getJSONObject(i);
				if (!Empty.isEmpty(userJson)) {
					if (userJson.containsKey("username") && userJson.containsKey("pwd")
							&& userJson.containsKey("role")) {
						String uname = userJson.getString("username");
						String pwd = userJson.getString("pwd");
						String role = userJson.getString("role");
						String des = userJson.getString("description");
						User user = new User(uname, pwd, role, des);
						list.add(user);
					}
				}
			}
		}
		return list;
	}

	/**
	 * 获取用户列表---json格式
	 * 
	 * @param dir
	 * @return
	 */
	public static String listAllUsersJsonDatas(String dir) {
		String readFileString = JsonUtils.readFileString(dir);
		return readFileString;
	}
	
	
}
