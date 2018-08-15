package util;

import java.util.ArrayList;
import java.util.List;

public class CompareFileName {

	/**
	 * 将传入的字符串拆分为 由比较单元组成的数组
	 * 
	 * @param s
	 * @return
	 */
	public static List<String> split(String s) {

		List<String> list = new ArrayList<String>();

		char[] cs = s.toCharArray();

		// 记录数字单元开头的索引位置
		int tmp = -1;

		for (int i = 0; i < cs.length; i++) {
			char c = cs[i];
			if (Character.isDigit(c)) {
				if (tmp < 0) {
					tmp = i;
				}
			} else {
				if (tmp >= 0) {
					// 将该字符之前的数字部分加入比较单元
					list.add(s.substring(tmp, i));
					tmp = -1;
				}
				list.add(String.valueOf(c));
			}
		}

		// 如果最后一个是数字,将最后的数字加入list中
		if (Character.isDigit(cs[cs.length - 1])) {
			tmp = tmp < 0 ? cs.length - 1 : tmp;
			list.add(s.substring(tmp, cs.length));
			tmp = -1;
		}

		return list;

	}

	/**
	 * 比较规则：
	 * 
	 * @1、单个非数字字符算作一个比较单元，比较时忽略大小写
	 * @2、出现连续数字，将连续数字看成一个整数，算作一个比较单元，数字 小于 非数字字符
	 * @3、如果相等，则单独比较其中的数字单元，规则：数字位数较多的小于位数较少的
	 * 
	 */
	public static int compareFileName(String s1, String s2) {

		// 省略提前判断和大小写转换部分的代码 详见附件

		List<String> ss1 = split(s1);
		List<String> ss2 = split(s2);

		// 取两个比较单元的最小长度
		int len = ss1.size() < ss2.size() ? ss1.size() : ss2.size();

		// 比较结果
		int r = 0;

		// t1、t2 对应比较单元
		String t1 = null;
		String t2 = null;

		// b1 b2 标识比较单元是否为数字
		boolean b1 = false;
		boolean b2 = false;

		for (int i = 0; i < len; i++) {
			t1 = ss1.get(i);
			t2 = ss2.get(i);

			b1 = Character.isDigit(t1.charAt(0));
			b2 = Character.isDigit(t2.charAt(0));

			// t1是数字 t2非数字
			if (b1 && !b2) {
				return -1;
			}

			// t2是数字 t1非数字
			if (!b1 && b2) {
				return 1;
			}

			// t1、t2 非数字
			if (!b1 && !b2) {
				r = t1.compareTo(t2);
				if (r != 0) {
					return r;
				}
			}

			// t1 t2都是数字
			if (b1 && b2) {

				// r = compareNumber(t1, t2);
				// r = Integer.parseInt(t1) - Integer.parseInt(t2);
				long l = Long.parseLong(t1) - Long.parseLong(t2);
				if (l > 0) {
					r = 1;
				} else if (l < 0) {
					r = -1;
				} else {
					r = 0;
				}

				if (r != 0) {
					return r;
				}
			}

		}
		// 如果两个集合的 0-(len-1)部分相等
		if (r == 0) {
			if (ss1.size() > ss2.size()) {
				r = 1;
			} else if (ss1.size() < ss2.size()) {
				r = -1;
			} else {
				// r = compareNumberPart(s1, s2);
				r = 1;
			}
		}

		return r;
	}

}
