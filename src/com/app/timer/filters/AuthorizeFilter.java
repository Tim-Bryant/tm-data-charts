package com.app.timer.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.app.timer.business.util.SessionData;
import com.app.timer.utils.FilePathUtils;
/**
 * 用户权限登录过滤器,主要是检查用户是否登录
 * @author liuxf
 *
 */
public class AuthorizeFilter implements Filter {

	private static final Logger log = LoggerFactory.getLogger(AuthorizeFilter.class);
	private boolean isFilter;
	//重定向登录页面地址
	private String login;
	
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public boolean getIsFilter() {
		return isFilter;
	}

	public void setIsFilter(boolean isFilter) {
		this.isFilter = isFilter;
	}

	@Override
	public void destroy() {
		log.info("服务器停止，用户权限过滤器销毁...");
	}

	/**
	 * 用户登录权限过滤
	 */
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
		  HttpServletRequest request=(HttpServletRequest) req;
          HttpServletResponse response=(HttpServletResponse) res;
          //跟路径
          String ctxPath = request.getContextPath();
          StringBuffer requestURL = request.getRequestURL();
		  if(isFilter){
			  //登录界面和验证登陆请求不需要过滤
			  if(requestURL.toString().indexOf("login")>-1 || requestURL.toString().indexOf("validateLogin")>-1){
				  //过滤完成放行
				  filterChain.doFilter(request, response);
			  }else if(requestURL.toString().contains(".css") || requestURL.toString().contains(".js")|| requestURL.toString().contains(".png")|| requestURL.toString().contains(".jpg")|| requestURL.toString().contains(".ico")){
	        		//如果发现是css或者js文件，直接放行
				  filterChain.doFilter(request, response);
        	  }else{
        		  //System.out.println("过滤的路径："+requestURL.toString());
				  if (SessionData.isEmpty(request)) {
					  //如果是登录界面就不需要操作权限判断来重定向
					  //如果session为空，用户也为空
					  //如果没有登录，重定向
					  response.sendRedirect(ctxPath+"/"+login);
	        	  }else{
	        		  SessionData sessionData = SessionData.getSessionData(request);
					  //System.out.println(sessionData.getUser().getUsername());
					  //System.out.println("不需要过滤的路径："+requestURL.toString());
	        		  //过滤完成放行
	        		  filterChain.doFilter(request, response);
	        	  }
			  }
          }else{
        	  //System.out.println("不需要过滤的路径："+requestURL.toString());
        	  filterChain.doFilter(request, response);
          }
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		  //得到过滤器的名字
	      String filterName = filterConfig.getFilterName();
	      //System.out.println("服务器启动，用户权限过滤器"+filterName+"初始化...");
	      //得到在web.xml文件中配置的初始化参数
		  String initParam1 = filterConfig.getInitParameter("isFilter");
	      String initParam2 = filterConfig.getInitParameter("login");
	      isFilter=Boolean.parseBoolean(initParam1);
	      login=initParam2;
	}
	
}
