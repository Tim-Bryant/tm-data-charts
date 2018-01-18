package com.app.timer.business.util;

import javax.servlet.http.HttpServletRequest;

import com.app.timer.business.pojo.User;

/**
 * 用户的Session数据
 * 
 * @author liuxf
 * 
 */
public class SessionData {
	private static final String emdp_sessiondata = "sessiondata";

	private User user=null;
	/**
	 * 
	 * 从session中获取SessionData, 如果不存在，则新建一个，放在session中
	 * 
	 * @param request
	 * @return
	 */
	public synchronized static SessionData getSessionData(HttpServletRequest request) {
		SessionData data = (SessionData) request.getSession().getAttribute(emdp_sessiondata);
		if (data == null) {
			data = new SessionData();
			request.getSession().setAttribute(emdp_sessiondata, data);
		}
		return data;
	}

	/**
	 * 清除session数据
	 */
	public static void clear(HttpServletRequest req) {
		req.getSession().removeAttribute(emdp_sessiondata);
	}

	/**
	 * 判断session中是否已经有session数据，并且用户对象不为空
	 * @param request
	 * @return
	 */
	public static boolean isEmpty(HttpServletRequest request){
		SessionData data = (SessionData) request.getSession().getAttribute(emdp_sessiondata);
		return data == null || data.user == null;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
