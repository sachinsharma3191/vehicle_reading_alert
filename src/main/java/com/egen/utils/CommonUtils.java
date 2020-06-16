package com.egen.utils;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import org.apache.commons.lang3.StringUtils;
import java.util.logging.Logger;

/**
 * Utiliy Class for various operations on handling null pointer,empty response
 * 
 * @author sachin
 *
 */

public class CommonUtils {

	static Logger log = Logger.getLogger("CommonUtil");

	public static boolean isObjectEmptyOrNull(Object obj) {
		return obj == null;
	}

	public static boolean isEmptyOrNull(String str) {
		return StringUtils.isEmpty(str) && StringUtils.isBlank(str);
	}

	public static boolean isEmptyOrNull(Integer number) {
		return number == null || number == 0;
	}

	public static boolean isEmptyOrNull(Integer[] array) {
		return array == null || array.length == 0;
	}

	public static boolean isEmptyOrNull(String[] array) {
		return array == null || array.length == 0;
	}

	public static boolean isNotEmptyOrNull(String[] str) {
		return !isEmptyOrNull(str);
	}

	public static <T> boolean isListEmptyOrNull(List<T> list) {
		return list == null || list.size() == 0 || list.isEmpty() || list.contains("") || list.contains(null);
	}

	public static boolean isEmptyOrNull(ArrayList<String> list) {
		return list == null || list.isEmpty() || list.contains(null) || list.contains("");
	}

	public static boolean contains(String source, String pattern) {
		return StringUtils.contains(source, pattern);
	}

	public static boolean isNullOrEmpty(final Collection<?> c) {
		return c == null || c.isEmpty();
	}

	public static boolean contains(String source, Character pattern) {
		return StringUtils.contains(source, pattern);
	}


	public static boolean isEmptyOrNull(StringBuilder content) {
		return content == null || content.length() == 0 || StringUtils.isBlank(content) && StringUtils.isEmpty(content);
	}

	public static boolean isEmptyOrNull(List<String> list) {
		return list == null || list.isEmpty() || list.contains(null) || list.contains("");
	}

	private CommonUtils() {

	}

	public static boolean isValidEmail(String email) {
		if (!(CommonUtils.contains(email, "@") && CommonUtils.contains(email, ".com"))) {
			return false;
		}
		return true;
	}

	public static List<String> convertArrayToList(String array[]) {
		List<String> list = new ArrayList<>();

		for (String ele : array) {
			list.add(ele);
		}
		return list;
	}

	public static boolean isStringUpperCase(String str) {
		return StringUtils.isAllUpperCase(str);
	}

	public static String formatDateToString(Date date, String format, String timeZone) {
		// null check
		if (date == null)
			return null;
		// create SimpleDateFormat object with input format
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		// default system timezone if passed null or empty
		if (timeZone == null || "".equalsIgnoreCase(timeZone.trim())) {
			timeZone = Calendar.getInstance().getTimeZone().getID();
		}
		// set timezone to SimpleDateFormat
		sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
		// return Date in required format with timezone as String
		return sdf.format(date);
	}

}
