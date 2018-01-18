package com.app.timer.business.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 作者 E-mail:xiaofeng09happy@sina.com
 * @version 创建时间：2017年11月6日 下午10:55:39 类说明:查询结果的Table格式显示
 */
public class TableShow {

	// 表格的头信息列表
	private List<String> tableTitles = new ArrayList<String>();
	// 表格的内容列表
	private List<List<String>> tableContent = new ArrayList<List<String>>();

	public List<String> getTableTitles() {
		return tableTitles;
	}

	public void setTableTitles(List<String> tableTitles) {
		this.tableTitles = tableTitles;
	}

	public List<List<String>> getTableContent() {
		return tableContent;
	}

	public void setTableContent(List<List<String>> tableContent) {
		this.tableContent = tableContent;
	}

}
