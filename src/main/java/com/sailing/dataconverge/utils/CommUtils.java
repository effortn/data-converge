package com.sailing.dataconverge.utils;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CommUtils {

	/**
	 * 判断字符串是否为空
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.trim().isEmpty();
	}
	
	/**
	 * 判断集合是否为空
	 * @param dataList
	 * @return
	 */
	public static boolean isEmpty(Collection<?> dataList) {
		return dataList == null || dataList.isEmpty();
	}
	
	/**
	 * 判断map是否为空
	 * @param map
	 * @return
	 */
	public static boolean isEmpty(Map<?, ?> map) {
		return map == null || map.isEmpty();
	}
	
	/**
	 * 判断数组是否为空
	 * @param array 数组
	 * @return
	 */
	public static boolean isEmpty(final Object[] array) {
		if (array == null) {
            return true;
        }
        return Array.getLength(array) == 0;
    }
	
	/**
	 * 判断数组是否为空
	 * @param array 数组
	 * @return
	 */
	public static boolean isEmpty(final byte[] array) {
		if (array == null) {
            return true;
        }
        return Array.getLength(array) == 0;
    }
	 
	/**
	 * 判断一个double值是不是非数字
	 * @param n double值
	 * @return 如果是非数字，返回true；否则返回false。
	 */
	public static boolean isNaN(double n) {
		return Double.isNaN(n);
	}
	
	/**
	 * 判断boolean字符串是否为真
	 * @param booleanStr boolean字符串
	 * @return 如果是[1|true|on]，返回true；否则返回false。
	 */
	public static boolean isEnable(String booleanStr) {
		if("1".equals(booleanStr)) {
			return true;
		}
		
		if("true".equalsIgnoreCase(booleanStr)) {
			return true;
		}
		
		if("on".equalsIgnoreCase(booleanStr)) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 字符串比较：忽略大小写
	 * @param str1 字符串1
	 * @param str2 字符串2
	 * @return 是否相等：true-相等；false-不相等
	 */
	public static boolean equalsIgnoreCase(final String str1, final String str2) {
        if (str1 == null || str2 == null) {
            return str1 == str2;
        } else if (str1 == str2) {
            return true;
        } else if (str1.length() != str2.length()) {
            return false;
        } else {
        		return str1.equalsIgnoreCase(str2);
        }
    }
	
	/**
	 * 集合中的元素，用指定的分隔符连起来
	 * @param iterable  集合
	 * @param separator 分隔符号
	 * @return 字符串
	 */
	public static String join(Iterable<?> iterable, String separator) {
		if(iterable == null) return null;
		
		StringBuffer sb = new StringBuffer();
		
		Iterator<?> it = iterable.iterator();
		while(it.hasNext()) {
			Object obj = it.next();
			if(obj == null) continue;
			
			sb.append(obj);
			if(it.hasNext()) {
				sb.append(separator);
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * 去掉前后空格
	 * @param str
	 * @return
	 */
	public static String trim(final String str) {
	    return str == null ? null : str.trim();
	}
	
	/**
	 * 当前线程休眠
	 * @param millis 毫秒数
	 */
	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 集合预览
	 * @param list
	 */
	public static void preview(List<?> list) {
		if(CommUtils.isEmpty(list)) {
			System.out.println("EMPTY LIST");
			return;
		}
		
		System.out.println("----List Elements----");
		Iterator<?> it = list.iterator();
		while(it.hasNext()) {
			System.out.println(it.next());
		}
	}
}

