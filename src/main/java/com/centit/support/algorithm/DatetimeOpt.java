package com.centit.support.algorithm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DatetimeOpt {
	private static Log log = LogFactory.getLog(DatetimeOpt.class);


	private static String defaultDatePattern = "yyyy-MM-dd";
	private static String timePattern = "HH:mm";
	private static String datetimePattern = "yyyy-MM-dd HH:mm:ss";
	private static String timestampPattern = "yyyy-MM-dd HH:mm:ss.SSS";
	
	/**
	 * 获得当前日期的字符串 ，格式为 "yyyy-MM-dd" 示例 2015-08-24
	 * @return
	 */
	public static String currentDate() { // 取系统当前日期
		SimpleDateFormat formatter = new SimpleDateFormat(defaultDatePattern);
		Date dt = new Date();
		dt.setTime(System.currentTimeMillis());
		String sCurDate = formatter.format(dt);
		return sCurDate;
	}
	
	/**
	 * 获得当前日期的字符串 ，格式为 "yyyy-MM-dd HH:mm:ss" 
	 * @return
	 */
	public static String currentDatetime() { // 取系统当前日期
		SimpleDateFormat formatter = new SimpleDateFormat(datetimePattern);
		Date dt = new Date();
		dt.setTime(System.currentTimeMillis());
		String sCurDate = formatter.format(dt);
		return sCurDate;
	}
	
	/**
	 * 根据 年、月、日、时、分、秒  创建一个日期 类型为 java.util.Date
	 * @param year 年 
	 * @param month 月
	 * @param date 日 
	 * @param hourOfDay 时 （24小时制）
	 * @param minute 分
	 * @param second 秒
	 * @return
	 */
	public static java.util.Date createUtilDate(int year, int month, int date,
			int hourOfDay, int minute,int second)
	{
		Calendar cal = new GregorianCalendar();
		cal.set( year,  month-1,  date,
				 hourOfDay,  minute, second);
		return cal.getTime();
	}
	
	/**
	 * 根据 年、月、日、时、分  创建一个日期 类型为 java.util.Date
	 * @param year 年 
	 * @param month 月
	 * @param date 日 
	 * @param hourOfDay 时 （24小时制）
	 * @param minute 分
	 * @return
	 */
	public static java.util.Date createUtilDate(int year, int month, int date,
			int hourOfDay, int minute)
	{
		return createUtilDate(year,  month,  date,
				 hourOfDay,  minute,0);
	}
	
	/**
	 * 根据 年、月、日  创建一个日期 类型为 java.util.Date
	 * @param year 年 
	 * @param month 月
	 * @param date 日 
	 * @return
	 */
	public static java.util.Date createUtilDate(int year, int month, int date)
	{
		return createUtilDate(year,  month,  date, 0, 0,0);
	}

	/**
	 * 日期类型转换 从 java.sql.Date 转换为 java.util.Date 这个会截断时间
	 * @param date
	 * @return
	 */
	public static java.util.Date convertUtilDate(java.sql.Date date){
		return date;
	}
	/**
	 *  日期类型转换 从 java.util.Date 转换为 java.sql.Date 这个会截断时间
	 * @param date
	 * @return
	 */
	public static java.sql.Date convertSqlDate(java.util.Date date){
		if(date==null)
			return null;
		else
			return new java.sql.Date(date.getTime());
	}
	
	/**
	 *  日期类型转换 从 java.util.Date 转换为 java.sql.Timestamp 
	 * @param date
	 * @return
	 */
	public static java.sql.Timestamp convertSqlTimestamp(java.util.Date date){
        if(date==null)
            return null;
        else
            return new java.sql.Timestamp(date.getTime());
    }
	
	/**
	 * 取系统当前日期和时间 ，返回 类型 java.sql.Date
	 */
	public static java.sql.Date currentSqlDate() { 
		return new java.sql.Date(System.currentTimeMillis());
	}	
	/**
	 * 取系统当前日期和时间，返回类型 java.util.Date
	 */
	public static java.util.Date currentUtilDate() { 
		return new java.util.Date(System.currentTimeMillis());
	}	

	/**
	 * 取系统当前日期和时间，返回类型 java.util.Calendar
	 */
	public static Calendar currentCalendarDate() {
		java.util.Date today = currentUtilDate();;
		Calendar cal = new GregorianCalendar();
		cal.setTime(today);
		return cal;
	}
	/**
	 *  返回常量字符串  "yyyy-MM-dd"
	 * 
	 * @return a string representing the date pattern on the UI
	 */
	public static String getDatePattern() {
		return defaultDatePattern;
	}

	/**
	 *  返回常量字符串  "yyyy-MM-dd HH:mm:ss"
	 * @return
	 */
	public static String getDateTimePattern() {
		return datetimePattern;
	}

	/**
	 * This method generates a string representation of a date/time in the
	 * format you specify on input
	 * 
	 * @param aMask
	 *            the date pattern the string is in
	 * @param strDate
	 *            a string representation of a date
	 * @return a converted Date object
	 * @see java.text.SimpleDateFormat
	 * @throws ParseException
	 */
	public static final Date convertStringToDate(String strDate,String aMask) {
		SimpleDateFormat df = null;
		Date date = null;
		df = new SimpleDateFormat(aMask);

		try {
			if(strDate == null || strDate.equals(""))
				return null;
			date = df.parse(strDate);
		} catch (ParseException pe) {
			log.error("converting '" + strDate + "' to date with mask '"
					+ aMask + "'");
			return null;
			//throw new ParseException(pe.getMessage(), pe.getErrorOffset());
		}
		return (date);
	}

	/**
	 * This method generates a string representation of a date's date/time in
	 * the format you specify on input
	 * 
	 * @param aMask
	 *            the date pattern the string is in
	 * @param aDate
	 *            a date object
	 * @return a formatted string representation of the date
	 * 
	 * @see java.text.SimpleDateFormat
	 */
	public static final String convertDateToString( Date aDate,String aMask) {
		String returnValue = "";

		if (aDate == null) {
			log.error("aDate is null!");
		} else {
			String sMask = (aMask ==null || "".equals(aMask))?"yyyy-MM-dd":aMask;
			SimpleDateFormat df = new SimpleDateFormat(sMask);
			returnValue = df.format(aDate);
		}
		return returnValue;
	}

	/**
	 * This method generates a string representation of a date based on the
	 * System Property 'dateFormat' in the format you specify on input
	 * 
	 * @param aDate
	 *            A date to convert
	 * @return a string representation of the date
	 */
	public static final String convertTimeToString(Date aDate) {
		return convertDateToString( aDate,timePattern);
	}
	/**
	 * 返回时间 字符串
	 * @param aDate
	 * @return "HH:mm:ss"
	 */
	public static final String convertTimeWithSecondToString(Date aDate) {
		return convertDateToString( aDate,"HH:mm:ss");
	}	
	/**
	 * 返回日期字符串  "yyyy-MM-dd"
	 * @param aDate
	 * @return
	 */
	public static final String convertDateToString(Date aDate) {
		return convertDateToString( aDate,defaultDatePattern);
	}

	/**
	 * 返回日期字符串  "yyyy-MM-dd HH:mm:ss"
	 * @param aDate
	 * @return
	 */
	public static final String convertDatetimeToString(Date aDate) {
		return convertDateToString( aDate,datetimePattern);
	}
	
	public static final String convertTimestampToString(Date aDate) {
		return convertDateToString( aDate,timestampPattern);
	}
	
	/**
	 * 获得当前时间字符串，格式为   "yyyy-MM-dd HH:mm:ss"
	 * @return string
	 */
	public static final String getNowDateTime4String() {
		return convertDateToString( currentUtilDate(),getDateTimePattern());
	}

	/**
	 * This method converts a String to a date using the datePattern
	 * 
	 * @param strDate
	 *            the date to convert (in format yyyy-mm-dd)
	 * @return a date object
	 * 
	 * @throws ParseException
	 */
	public static Date convertStringToDate(String strDate)
			throws ParseException {

		return convertStringToDate(strDate,getDatePattern());
	}

	/**
	 * 获得 年月日 对应的日期 是星期几  星期日 到星期六 为 0-6
	 * @param y
	 * @param m
	 * @param d
	 * @return
	 */
	public static int getDayOfWeek(int y,int m,int d)
    {
        int y0 = y - (14 - m) / 12;
        int x = y0 + y0/4 - y0/100 + y0/400;
        int m0 = m + 12 * ((14 - m) / 12) - 2;
        return (d + x + (31*m0)/12) % 7;
    }
	
	/**
	 * 获得 指定日期 是星期几  星期日 到星期六 为 0-6
	 * @param date
	 * @return
	 */
	public static int getDayOfWeek(java.util.Date date) {
		
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_WEEK) - 1;
	}
	
	/**
	 * 获得 指定日期 是星期几  星期日 到星期六 为 "星期日","星期一","星期二","星期三","星期四","星期五","星期六"
	 * @param date
	 * @return
	 */
	public static String getDayOfWeekCN(java.util.Date date) {
		String [] weeklist = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六","",};
		return weeklist[getDayOfWeek(date)];
	}
	
	public static int getSecond(java.util.Date date) {
			
			Calendar cal = new GregorianCalendar();
			cal.setTime(date);
			return cal.get(Calendar.SECOND);
		} 
		
	public static int getMinute(java.util.Date date) {
		
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		return cal.get(Calendar.MINUTE);
	} 

	public static int getHour(java.util.Date date) {
		
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		return cal.get(Calendar.HOUR_OF_DAY);
	} 
	

	public static int getWeekOfYear(java.util.Date date) {
		
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		return cal.get(Calendar.WEEK_OF_YEAR);
	} 
	
	public static int getWeekOfMonth(java.util.Date date) {
		
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		return cal.get(Calendar.WEEK_OF_MONTH);
	} 
	
	public static int getDay(Date date) {
		
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_MONTH);
	}
	

	public static int getDayOfYear(java.util.Date date) {
		
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_YEAR);
	}
	
	public static int getMonth(java.util.Date date) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		return cal.get(Calendar.MONTH)+1;
	}
	
	public static int getYear(java.util.Date date) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		return cal.get(Calendar.YEAR);
	}	
	
	public static java.util.Date truncateToDay(java.util.Date date){
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		return createUtilDate(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,cal.get(Calendar.DAY_OF_MONTH));
	}
	public static java.util.Date truncateToMonth(java.util.Date date){
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		return createUtilDate(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,1);
	}	
	public static java.util.Date truncateToYear(java.util.Date date){
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		return createUtilDate(cal.get(Calendar.YEAR),1,1);
	}	
	//跳转到年的最后一天
	public static java.util.Date seekEndOfYear(java.util.Date date){
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		return createUtilDate(cal.get(Calendar.YEAR),12,31);
	}
	//跳转到月的最后一天
	public static java.util.Date seekEndOfMonth(java.util.Date date){
		return addDays(truncateToMonth(addMonths(date,1)),-1);		
	}
	
	public static java.util.Date addSeconds(java.util.Date date,int nSeconds) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.SECOND, nSeconds);
		return cal.getTime();
	}
	
	public static java.util.Date addMinutes(java.util.Date date,int nMinutes) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, nMinutes);
		return cal.getTime();
	}
	
	public static java.util.Date addHours(java.util.Date date,int nHours) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.HOUR, nHours);
		return cal.getTime();
	}
	
	public static java.util.Date addDays(java.util.Date date,int nDays) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.DATE, nDays);
		return cal.getTime();
	}
	public static java.util.Date addMonths(java.util.Date date,int nMonths) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.MONTH, nMonths);
		return cal.getTime();
	}
	public static java.util.Date addYears(java.util.Date date,int nYears) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.YEAR, nYears);
		return cal.getTime();
	}
	

	/**
	 * 
	 * @param beginTime
	 * @param endTime
	 * @return 计算这个周期中的天数, 包括 beginTime，endTime
	 */
    public static int calcSpanDays(java.util.Date beginDate, java.util.Date endDate) {
    	java.util.Date bD = (beginDate.getTime() > endDate.getTime()) ? truncateToDay(endDate) : truncateToDay(beginDate);
    	java.util.Date eD = (beginDate.getTime() > endDate.getTime()) ? beginDate : endDate;
        return  (int) ( (eD.getTime() - bD.getTime())  / 1000 / 60 / 60 / 24 + 1 );

    }	
   
    /**
     * 计算周的第一天始日期
     * @param nYear
     * @param nWeekNo
     * @return
     */
    public static java.util.Date calcWeek1stDay(int nYear, int nWeekNo){
    	 Calendar  cal = Calendar.getInstance();
         cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);      
         cal.set(Calendar.YEAR, nYear);
         cal.set(Calendar.WEEK_OF_YEAR, nWeekNo);
         
         return cal.getTime();
    }
    
    /**
     * 计算周的最后一天日期
     * @param nYear
     * @param nWeekNo
     * @return
     */
    public static java.util.Date calcWeekLastDay(int nYear, int nWeekNo){
   	 Calendar  cal = Calendar.getInstance();
     cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);      
     cal.set(Calendar.YEAR, nYear);
     cal.set(Calendar.WEEK_OF_YEAR, nWeekNo);
     
     return cal.getTime();

    }
    
    /**
   	 * 计算这个周期中的周六和周日的天数，周末的天数
   	 * @param beginTime
   	 * @param endTime
   	 * @return 计算这个周期中的周六和周日的天数, 包括 beginTime，endTime
   	 */
       public static int calcWeekendDays(java.util.Date beginDate, java.util.Date endDate) {
           int nWeekDay= getDayOfWeek(beginDate);
           int m = calcSpanDays( beginDate,endDate);
           int weekEnds = (m+nWeekDay) / 7;
           int nWeekDay2 = (m+nWeekDay) % 7;
           
           int days = weekEnds * 2 - (nWeekDay==0?0:1) + (nWeekDay2>0?1:0) ;
           return days;
       }	
       
    
    /**
	 * 计算这个周期中 工作日的天数,不包括 周六和周日，但是因为不知道其他的假期，所以只是去掉周末
	 * @param beginTime
	 * @param endTime
	 * @param weekDay 0~6 "星期日","星期一","星期二","星期三","星期四","星期五","星期六"
	 * @return 计算这个周期中 某个工作日的天数, 包括 beginTime，endTime
	 */
    public static int calcWeekDays(java.util.Date beginDate, java.util.Date endDate,int weekDay) {
        int nWeekDay= getDayOfWeek(beginDate);
        int m = calcSpanDays( beginDate,endDate);
        return m/7 + ( ( (weekDay >= nWeekDay && nWeekDay + m%7 > weekDay )|| (weekDay+7 >= nWeekDay && nWeekDay + m%7 > weekDay+7 ))?1:0 );
    }	
    /**
     * 判断两个时间是否相等，精确到秒
     * @param oneDate
     * @param otherDate
     * @return
     */
	public static boolean equalOnSecond(java.util.Date oneDate, java.util.Date otherDate) {
		if(oneDate==null || otherDate==null)
			return false;
		return oneDate.getTime() / 1000 == otherDate.getTime() / 1000;
	}
	/**
     * 判断两个时间是否相等，精确到分
     * @param oneDate
     * @param otherDate
     * @return
     */
	public static boolean equalOnMinute(java.util.Date oneDate, java.util.Date otherDate) {
		if(oneDate==null || otherDate==null)
			return false;
		return oneDate.getTime() / 60000 == otherDate.getTime() / 60000;
	}
	/**
     * 判断两个时间是否相等，精确到时
     * @param oneDate
     * @param otherDate
     * @return
     */
	public static boolean equalOnHour(java.util.Date oneDate, java.util.Date otherDate) {
		if(oneDate==null || otherDate==null)
			return false;
		return oneDate.getTime() / 3600000 == otherDate.getTime() / 3600000;
	}
	/**
     * 判断两个时间是否相等，精确到天
     * @param oneDate
     * @param otherDate
     * @return
     */
	public static boolean equalOnDay(java.util.Date oneDate, java.util.Date otherDate) {
		if(oneDate==null || otherDate==null)
			return false;
		return oneDate.getTime() / 86400000 == otherDate.getTime() / 86400000;
	}
	
	public static java.util.Date smartPraseDate(String sDate){
		if(sDate==null || "".equals(sDate))
			return null;
		String sTD = StringRegularOpt.trimDateString(sDate);
		int sl = sTD.length();
		switch(sl){
		case 5:
			return convertStringToDate(sTD,"yy-MM");
		case 7:
			return convertStringToDate(sTD,"yyyy-MM");
		case 8:
			return convertStringToDate(sTD,"yy-MM-dd");
		case 10:
			return convertStringToDate(sTD,"yyyy-MM-dd");
		case 11:
			return convertStringToDate(sTD,"yy-MM-dd HH");
		case 13:
			return convertStringToDate(sTD,"yyyy-MM-dd HH");
		case 14:
			return convertStringToDate(sTD,"yy-MM-dd HH:mm");
		case 16:
			return convertStringToDate(sTD,"yyyy-MM-dd HH:mm");
		case 17:
			return convertStringToDate(sTD,"yy-MM-dd HH:mm:ss");
		case 19:
			return convertStringToDate(sTD,"yyyy-MM-dd HH:mm:ss");
		case 20:
		case 21:
		case 22:
		case 23:
			return convertStringToDate(sTD,"yyyy-MM-dd HH:mm:ss.SSS");
		default:
			return null;
		}		
	}

}
