package com.funnel.svc.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

public class DateUtil {
	private static final Logger logger = Logger.getLogger(DateUtil.class);

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	private static final SimpleDateFormat dateFormatWithoutTime = new SimpleDateFormat(
			"yyyy-MM-dd");

	public static Date parseStr2Date(String date) {

		try {
			return dateFormat.parse(date);
		} catch (ParseException e) {
			logger.equals("date:" + date + " format:" + dateFormat + " ת��ʧ��");
			return null;
		}
	}
	
	public static Date parseStr2DateWithoutTime(String date) {

		try {
			return dateFormatWithoutTime.parse(date);
		} catch (ParseException e) {
			logger.error("date:" + date + " format:" + dateFormatWithoutTime ,e);
			return null;
		}
	}
	
	public static Date parseStr2Date(String date, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		try {
			return dateFormat.parse(date);
		} catch (ParseException e) {
			logger.equals("date:" + date + " format:" + format + " ת��ʧ��");
			return null;
		}
	}

	public static String parseDate2Str(Date date, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}

	public static String getSimpleDateStr(Date date) {
		return dateFormat.format(date);
	}

	public static String getSimpleDateStrWithoutTime(Date date) {
		return dateFormatWithoutTime.format(date);
	}

	public static Date getDateBefore(Date date, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
		return now.getTime();
	}

	/**
	 * <li>����������ʱ������õ�����
	 * 
	 * @param beginDateStr
	 * @param endDateStr
	 * @return long
	 * @author Administrator
	 */
	public static long getDaySub(String beginDateStr, String endDateStr) {
		long day = 0;
		Date beginDate = parseStr2Date(beginDateStr, "yyyy-MM-dd HH:mm:ss");
		Date endDate = parseStr2Date(endDateStr, "yyyy-MM-dd HH:mm:ss");
		day = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
		return day;
	}
}
