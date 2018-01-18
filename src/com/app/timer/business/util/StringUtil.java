package com.app.timer.business.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文件名操作类
 * @author liuxf
 *
 */
public abstract class StringUtil {
	private static String IMG_EXT = ".jpg,.gif,.bmp,.jpeg,.png,";

	/**
	 * 得到文件扩展名
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getFileExt(String fileName) {
		if (Empty.isEmpty(fileName)) {
			return "";
		}
		return fileName.substring(fileName.lastIndexOf('.'), fileName.length());
	}

	/**
	 * 判断是否为图片类型文件
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean isImgFile(String fileName) {
		return IMG_EXT.indexOf(getFileExt(fileName) + ",") >= 0;
	}

	/**
	 * 从文件全路径中得到文件名
	 * 
	 * @param args
	 */
	public static String getFileName(String path) {
		// /abc/bcd/abc.txt
		if (Empty.isEmpty(path)) {
			return null;
		}
		String s = path.substring(path.lastIndexOf("/") + 1);
        s = path.substring(path.lastIndexOf("\\") + 1);
		return s;
	}

	/**
	 * 把文本转换成html 显示,静态化生成用到
	 * 
	 * @param text
	 * @return
	 */
	public static String toHtmlString(String text) {
		if (Empty.isEmpty(text)) {
			return "";
		}
		String tmp = text;

		String tmpS = null;
		try {
			String regex1 = "\n"; // 回车
			String regex2 = "^\\s"; // 首行为 tab 操作符
			tmpS = tmp.replaceAll(regex1, "<br/>&nbsp;&nbsp;&nbsp;&nbsp;").replaceAll(regex2, "&nbsp;&nbsp;");
		} catch (Exception e) {
			e.printStackTrace();
			tmpS = text;
		}
		return tmpS;
	}

	public static String toHexString(String text) {
		if (text == null) {
			return "";
		}
		StringBuffer buf = new StringBuffer();
		final String start = "\\u";
		for (int i = 0; i < text.length(); i++) {
			int c = text.charAt(i);
			buf.append(start);
			String s = Integer.toHexString(c);

			if (s.length() == 1) {
				s = "000" + s;
			} else if (s.length() == 2) {
				s = "00" + s;
			} else if (s.length() == 3) {
				s = "0" + s;
			}
			buf.append(s);
		}
		return buf.toString();
	}

	/**
	 * 向文件名中追加字符，以修改文件名称
	 * 
	 * @param oldName
	 *            文件原有名称
	 * @param appendString
	 *            追加名称
	 * @return 处理后的文件名
	 */
	public static String appendFileName(String oldName, String appendString) {
		// /abc/bcd/abc.txt
		if (Empty.isEmpty(oldName)) {
			return null;
		}
		String name = oldName.substring(oldName.lastIndexOf("\\") + 1);
		name = name.substring(name.lastIndexOf("/") + 1);
		StringBuffer sb = new StringBuffer();
		sb.append(oldName.substring(0, oldName.lastIndexOf(name)));
		sb.append(name.substring(0, name.lastIndexOf(".")));
		sb.append(appendString);
		sb.append(name.substring(name.lastIndexOf(".")));

		return sb.toString();
	}

	/**
	 * 判断字符串是否在字符数组中
	 * 
	 * @param s
	 *            字符串数组
	 * @param str
	 *            需要比较的字符串
	 * @return 如果存在返回：true,如果不存在返回：false
	 */
	public static boolean contains(String[] s, String str) {
		if (s == null || str == null)
			return false;
		for (int i=0;i<s.length;i++) {
			if (str.equals(s[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 过滤字符中的HTML标签
	 * 
	 * @param element
	 *            含有HTML标签体的字符串
	 * @return
	 */
	public static String getTxtWithoutHTML(String element) {
		// String reg="<[^<|^>]+>";
		// return element.replaceAll(reg,"");

		if (null == element || "".equals(element.trim())) {
			return element;
		}

		Pattern pattern = Pattern.compile("&[a-z]{1,10}+;|<[^<|^>]*>");

		Matcher matcher = pattern.matcher(element);
		StringBuffer txt = new StringBuffer();

		// 替换字符中HTML标签和特殊符号

		while (matcher.find()) {
			String group = matcher.group();

			if (group.matches("<[\\s]*>")) {
				matcher.appendReplacement(txt, group);
			} else if (group.matches("&nbsp;")) {
				matcher.appendReplacement(txt, " ");
			} else if (group.matches("&amp;")) {
				matcher.appendReplacement(txt, "&");
			} else if (group.matches("&lt;")) {
				matcher.appendReplacement(txt, "<");
			} else if (group.matches("&gt;")) {
				matcher.appendReplacement(txt, ">");
			} else if (group.matches("&quot;")) {
				matcher.appendReplacement(txt, "\"");
			} else if (group.matches("&apos;")) {
				matcher.appendReplacement(txt, "\'");
			} else {
				matcher.appendReplacement(txt, "");
			}

		}

		// 加裁最后字符

		matcher.appendTail(txt);
		/*
		 * txt.repaceEntities(txt,"&","&"); repaceEntities(txt,"<","<");
		 * repaceEntities(txt,">",">"); repaceEntities(txt,""","\"");
		 * repaceEntities(txt," ","");
		 */

		return txt.toString();

	}

	/**
	 * 过滤字符中的HTML标签
	 * 
	 * @param element
	 *            含有HTML标签体的字符串
	 * @param length
	 *            返回过滤后字符长度
	 * @return 返回过虑后的字符串
	 */
	public static String getTxtWithoutHTML(String element, int length) {
		/*if (null == element || "".equals(element.trim())) {
			return element;
		}
		String reg="<[^<|^>]+>";
		return element.replaceAll(reg,"").substring(0, element.length() > length ? length : element.length());
		*/
		
		if (null == element || "".equals(element.trim())) {
			return element;
		}

		Pattern pattern = Pattern.compile("&[a-z]{1,10}+;|<[^<|^>|[^\\x00-\\xff]]*>");
		Matcher matcher = pattern.matcher(element);
		StringBuffer txt = new StringBuffer();
		// 替换字符中HTML标签和特殊符号
		while (matcher.find() && length > txt.length()) {
			String group = matcher.group();
			if (group.matches("&nbsp;")) {
				matcher.appendReplacement(txt, " ");
			} else if (group.matches("&amp;")) {
				matcher.appendReplacement(txt, "&");
			} else if (group.matches("&lt;")) {
				matcher.appendReplacement(txt, "<");
			} else if (group.matches("&gt;")) {
				matcher.appendReplacement(txt, ">");
			} else if (group.matches("&quot;")) {
				matcher.appendReplacement(txt, "\"");
			} else if (group.matches("&apos;")) {
				matcher.appendReplacement(txt, "\'");
			} else if (group.matches("<br />")) {
				matcher.appendReplacement(txt, "");
			}  else {
				matcher.appendReplacement(txt, "");
			}
		}
		// 加裁最后字符
		if (length > txt.length())
			matcher.appendTail(txt);
		
		return txt.toString().substring(0, txt.length() > length ? length : txt.length());
	}

	/**
	 * 
	 * @param context
	 * @param path
	 * @param fileMap
	 * @return
	 */
	public static String filterIMG(String context, String path, Map fileMap) {
		Pattern pattern = Pattern.compile("<IMG style[^<|^>|[^\\x00-\\xff]]*>");
		String imgString = "";
		Matcher matcher = pattern.matcher(context);
		StringBuffer txt = new StringBuffer();
		String temp = null;
		String group = null;
		while (matcher.find()) {
			group = matcher.group();
			temp = group;
			Set set = fileMap.keySet();
			for (java.util.Iterator it = set.iterator(); it.hasNext();) {
				String oldName = (String) it.next();
				if (group.indexOf(oldName + "');") != -1) {
					imgString = "src='" + path + fileMap.get(oldName) + "\'";
					temp = group.replaceFirst("FILTER:[^<|^>|[^\\x00-\\xff]]*'\\);", "");
					temp = temp.replaceFirst("src=\"[^<|^>|^']*editor/none.gif\"", imgString);
					// System.out.println(temp);

				}
			}
			matcher.appendReplacement(txt, temp);
		}
		matcher.appendTail(txt);
		return txt.toString();
	}

	public static void main(String[] org) {
		String str2 = "safasdfasdfad你们来测试一下<IMG style=\"中文的"
				+ "FILTER: progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod="
				+ "'scale',src='C:/Documents and Settings/Administrator/My Documents/My Pictures/Mac_OS_"
				+ "X_Leopard.jpg\');WIDTH: 235px; HEIGHT: 145px " + "alt="
				+ " src=\"http://132.159.173.106:9080/" + "images/editor/none.gif\""
				+ "border=0 aid=attach_1>查看 一下替换是否成功了？？？";

		String str = "<SPAN style=\"FONT-FAMILY: 宋体; mso-ascii-font-family: Calibri; mso-hansi-font-family: Calibri\"><IMG style=\"WIDTH: 400px; HEIGHT: 255px\" alt=\"\" src=\"http://130.1.33.31:9080/idealCMS/attachments/p1.JPG\" border=0 aid=\"attach_1\"></SPAN></SPAN></P>";
		str = str + str;
		Map map = new HashMap();
		map.put("Mac_OS_X_Leopard.jpg", "aaa.jpg");
		String ss = StringUtil.filterIMG(str, "http://asdfasdf/", map);
		System.out.println(ss);
		String ss1 = StringUtil.getTxtWithoutHTML(str, 500);
		System.out.println(ss1);
		// 测试过滤字符
		/*
		 * String str = "<a herf='asdfasdfwe'>测试一下吧</a><btml>adsfasdfasdf
		 * &amp;&amp;&amp;>>>&ndfbsp;&nbsp;&nbsp; wq
		 * g</html>" + "<a>再第二次</b>最后的字符";
		 * 
		 * System.out.println(""); System.out.println("转换前: " + str);
		 * System.out.println(""); String results =
		 * FormatUtil.getTxtWithoutHTML(str, 100);
		 * 
		 * System.out.println("转换后: " + results);
		 * 
		 * //测试日期 Calendar c=Calendar.getInstance(); System.out.println("年: " +
		 * c.get(Calendar.YEAR) ); System.out.println("月: " +
		 * c.get(Calendar.MONTH) ); System.out.println("日: " +
		 * c.get(Calendar.DAY_OF_MONTH));
		 */
		// \u4F60\u597D
		String text = "你好,hi";
		System.out.println(toHexString(text));

	}

}
