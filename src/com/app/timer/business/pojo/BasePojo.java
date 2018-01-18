package com.app.timer.business.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;

import org.beetl.sql.core.annotatoin.AssignID;
import org.beetl.sql.core.annotatoin.AutoID;
import org.beetl.sql.core.annotatoin.TableTemplate;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 实体对象的基类
 * 
 * @author liuxf
 * 
 */
public class BasePojo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;// 主键ID
	private String state;// 状态为
	//@JsonFormat，默认情况下timeZone为GMT（即标准时区），所以会造成输出少8小时。
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date createTime;// 创建时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date modifyTime;// 修改时间
	
	/**
	 * 主键需要通过注解来说明，如@AutoID(自动增长)，或者@AssingID等，但如果是自增主键，且属性是名字是id，则不需要注解，自动认为是自增主键
	 * 作用于属性字段或者getter方法，告诉beetlsql，这是程序设定
	 */
	@AssignID()
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Column(name="CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name="MODIFY_TIME")
	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

}
