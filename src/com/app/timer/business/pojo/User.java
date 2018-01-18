package com.app.timer.business.pojo;

/**
 * @author 作者 E-mail:xiaofeng09happy@sina.com
 * @version 创建时间：2017年11月12日 下午7:50:49 类说明:系统使用人员，轻量级建模
 */
public class User {

	/** 用户角色－－－管理员 */
	public static final String ADMIN = "admin";
	/** 用户角色－－－数据采编员 */
	public static final String DATA_IMPORT = "data_import";
	/** 用户角色－－－报表采编员 */
	public static final String CHARTS_GEN = "charts_gen";
	/** 用户角色－－－报表查看人员 */
	public static final String CHARTS_REVIEW = "charts_review";

	/** 用户名---唯一性 */
	private String username;
	/** 密码 */
	private String pwd;
	/** 角色名 */
	private String role;
	/** 描述 */
	private String description;

	public User() {
		super();
	}
	
	public User(String username, String pwd, String role, String description) {
		this.username = username;
		this.pwd = pwd;
		this.role = role;
		this.description = description;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
