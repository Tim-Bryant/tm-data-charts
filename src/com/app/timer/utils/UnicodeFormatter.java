package com.app.timer.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *数据编码转换工具类
 * 编辑器修改赋值使用
 * @author liuxf
 *
 */
public class UnicodeFormatter {
	private static Logger log = LoggerFactory.getLogger(UnicodeFormatter.class);

	/** Unicode编码前缀 */
	private static final String _UNICODE_PREFIX = "\\u";

	/** 十六进制字符 */
	private static final char[] _HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
			'e', 'f' };

	/**
	 * 将一个byte转成十六进制格式的字符串
	 * 
	 * @param b
	 * @return
	 */
	public static String byteToHex(byte b) {
		char[] array = { _HEX_DIGITS[(b >> 4) & 0x0f], _HEX_DIGITS[b & 0x0f] };

		return new String(array);
	}

	/**
	 * 将一个普通字符转为十六进制格式的字符串
	 * 
	 * @param c
	 * @return
	 */
	public static String charToHex(char c) {
		byte hi = (byte) (c >>> 8);
		byte lo = (byte) (c & 0xff);

		char[] array = { _HEX_DIGITS[(hi >> 4) & 0x0f], _HEX_DIGITS[hi & 0x0f], _HEX_DIGITS[(lo >> 4) & 0x0f],
				_HEX_DIGITS[lo & 0x0f] };

		return new String(array);
	}

	/**
	 * 将Unicode编码的字符串解析成普通格式字符串
	 * 
	 * @param hexString
	 * @return
	 */
	public static String parseString(String hexString) {
		StringBuilder sb = new StringBuilder();

		char[] array = hexString.toCharArray();

		if ((array.length % 6) != 0) {
			log.error("String is not in hex format");

			return hexString;
		}
		for (int i = 2; i < hexString.length(); i = i + 6) {
			String s = hexString.substring(i, i + 4);
			try {
				char c = (char) Integer.parseInt(s, 16);
				sb.append(c);
			} catch (Exception e) {
				log.error("Parse error, " + e);
				return hexString;
			}
		}

		return sb.toString();
	}

	/**
	 * 字符数组转为转成Unicode编码的字符串
	 * 
	 * @param array
	 * @return
	 */
	public static String toString(char[] array) {
		StringBuilder sb = new StringBuilder(array.length * 6);

		char[] hexes = new char[4];

		for (int i = 0; i < array.length; i++) {
			sb.append(_UNICODE_PREFIX);
			sb.append(_charToHex(array[i], hexes));
		}

		return sb.toString();
	}

	/**
	 * 将字符串转成Unicode编码的字符串
	 * 
	 * @param s
	 * @return
	 */
	public static String toString(String s) {
		if (s == null) {
			return null;
		}

		StringBuilder sb = new StringBuilder(s.length() * 6);

		char[] hexes = new char[4];

		for (int i = 0; i < s.length(); i++) {
			sb.append(_UNICODE_PREFIX);
			sb.append(_charToHex(s.charAt(i), hexes));
		}

		return sb.toString();
	}

	// char在java中是2个字节。java采用unicode，2个字节（16位）来表示一个字符。
	private static char[] _charToHex(char c, char[] hexes) {
		byte hi = (byte) (c >>> 8);
		byte lo = (byte) (c & 0xff);

		hexes[0] = _HEX_DIGITS[(hi >> 4) & 0x0f];
		hexes[1] = _HEX_DIGITS[hi & 0x0f];
		hexes[2] = _HEX_DIGITS[(lo >> 4) & 0x0f];
		hexes[3] = _HEX_DIGITS[lo & 0x0f];

		return hexes;
	}

	public static void main(String[] args) {
		String test = "abc123@汉字αβあにキКЖ÷⑥";
		String test1 = "<p style='width:800'>哈哈哈哈哈</p>";

		String u = UnicodeFormatter.toString(test1);
		System.out.println(u);
		System.out.println(UnicodeFormatter.parseString(u));
		System.out.println(UnicodeFormatter.toString("\""));
	}
}
