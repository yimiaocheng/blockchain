package com.cwt.app.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;


public class Public {

	private static final Logger logger = LoggerFactory.getLogger(Public.class);

	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd");

	public static String getEndTime() throws Exception {
		String rentTime = sdfs.format(new Date());
		String start = rentTime + " 00:00:00";
		long date1 = sdf.parse(start).getTime();// - 3600*1000*24;
		String str = sdf.format(new Date(date1));	//	结束时间
		return str;
	}

	// 增加k天后的日期时间
	public static String addDate(String dateStr, int k) throws Exception {
		SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd");
		String tempStr = dateStr.substring(0, 10);
		return sdfs.format(sdfs.parse(tempStr).getTime()+k*24*3600000L).substring(0, 10)+dateStr.substring(10, 19);
	}
	
	// 两个日期间隔的天数，返回0则表示这两个日期均为当天，返回其它则表示这两个日期不在同一天
	public static int getDays(String rentTime, String returnTime){
		rentTime = rentTime.substring(0, 10) + " 00:00:00";
		returnTime = returnTime.substring(0, 10) + " 00:00:00";
		return (int)(Public.StringToDate1(returnTime).getTime()- Public.StringToDate1(rentTime).getTime())/(24*3600*1000);
	}
	
	
	public static String GetIpAdd() {
		String ipAddress = "";
		try {
			ipAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			logger.error(ExceptionUtil.readStackTrace(e));
		}
		return ipAddress;
	}

	public static Date GetNewDate() {
		String fromFormat = "yyyy-MM-dd HH:mm:ss:ms";
		SimpleDateFormat format = new SimpleDateFormat(fromFormat);
		Date myDate = new Date();
		TimeZone zone = TimeZone.getTimeZone("GMT+8");
		format.setTimeZone(zone);
		return StringToDate1(format.format(myDate));
	}

	public static Date StringToDate(String str) {
		SimpleDateFormat sdfIn;
		sdfIn = new SimpleDateFormat("yyyy-MM-dd");
		Date dataTmp = null;
		try {
			dataTmp = sdfIn.parse(str);
		} catch (ParseException e) {
			logger.error(e.getMessage());
		}
		return dataTmp;
	}
	//时间差
	public static String getDatePoor(Date endDate, Date nowDate) {
	    long nd = 1000 * 24 * 60 * 60;
	    long nh = 1000 * 60 * 60;
	    //long nm = 1000 * 60;
	    float hireDate = 0;	//	租期
	    // 获得两个时间的毫秒时间差异
	    long diff = endDate.getTime() - nowDate.getTime();
	    // 计算差多少天
	    float day = diff / nd;
	    hireDate += day;
	    // 计算差多少小时
	    long hour = diff % nd / nh;
	    if(hour >= 7){//7小时起（含7小时）租期计1天
			hireDate += 1;
		}
	    if(hour >= 1 && hour < 7){
			//1->7小时以内（不含7小时），租期计0.5天
			hireDate += 0.5f;
		}else{
			//1小时以内（不含1小时），不计入租期
		}
	    // 计算差多少分钟
	   // long min = diff % nd % nh / nm;
	    // 计算差多少秒
	    // long sec = diff % nd % nh % nm / ns;
	    //输出结果
	    return hireDate+"";
	}
	
	public static Date StringToDate1(String str) {
		SimpleDateFormat sdfIn;
		sdfIn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dataTmp = null;
		try {
			dataTmp = sdfIn.parse(str);
		} catch (ParseException e) {
			logger.error(ExceptionUtil.readStackTrace(e));
		}
		return dataTmp;
	}

	/** 日期增减天数k,正数为增,负数为减 */
	public static Date addDate(int k) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, k);
		Date d = cal.getTime();
		return d;
	}
	
	/** 日期增减 分钟k,正数为增,负数为减 */
	public static Date addMinute(Date date, int k) {
		Calendar cal= Calendar.getInstance();
		cal.setTime(date);
//		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, k);
		Date d = cal.getTime();
		return d;
	}
	
	public static String getWeek(String str) {
		String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
		Date date = StringToDate1(str);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
	}

	public static String DateTOString(Date date) {
		SimpleDateFormat dformat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return dformat.format(date);
	}

	public static String DateTOString1(Date date) {
		SimpleDateFormat dformat = new SimpleDateFormat(
				"yyyyMMdd");
		return dformat.format(date);
	}

	public static String DateTOString2(Date date) {
		SimpleDateFormat dformat = new SimpleDateFormat(
				"yyyyMMddHHmmss");
		return dformat.format(date);
	}

	public static String DateTOString3(Date date) {
		SimpleDateFormat dformat = new SimpleDateFormat(
				"yyMMdd");
		return dformat.format(date);
	}

	public static String DateTOString4(Date date) {
		SimpleDateFormat dformat = new SimpleDateFormat(
				"yyyyMMdd");
		return dformat.format(date);
	}

	public static String DateTOString5(Date date) {
		SimpleDateFormat dformat = new SimpleDateFormat(
				"yyyy-MM-dd");
		return dformat.format(date);
	}	
	
	public static String DateTOString6(String str) {
		SimpleDateFormat sdfIn;
		sdfIn = new SimpleDateFormat("yyyy-MM-dd");
		Date dataTmp = null;
		try {
			dataTmp = sdfIn.parse(str);
		} catch (ParseException e) {
			logger.error(ExceptionUtil.readStackTrace(e));
		}
		SimpleDateFormat dformat = new SimpleDateFormat(
				"yyyy-MM-dd");
		return dformat.format(dataTmp);
	}
	
	
	public static String getRandomNub(int Nub) {
		String num = "";
		for (int i = 0; i < Nub; i++) {
			num += String.valueOf((int) (10 * Math.random()));
		}
		return num;
	}

	public static String getIpAddr(HttpServletRequest request) {
		return request.getRemoteAddr();
	}
	
	public static String dec(String str) {
		String[] strarr = str.split("\\'");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < strarr.length; i++) {
			sb.append((char) Integer.parseInt(strarr[i]));
		}
		return sb + "";
	}

	public static void writeLog(String userid, String logStr) {
		// System.out.println("【"+userid+"】【"+Public.DateTOString(Public.GetNewDate())+"】"+logStr);
	}
	
	/**比较指定时间与当前时间相差多少分钟*/
	public static long compareTime(Date time){
		return (GetNewDate().getTime()-time.getTime())/(1000*60);
	}
	
	/**比较指定时间time2与时间time1相差多少分钟*/
	public static long compareTime2(Date time1, Date time2){
		return (time1.getTime()-time2.getTime())/(1000*60);
	}
	
	/**比较指定时间time2与时间time1相差多少秒*/
	public static long compareTime3(Date time1, Date time2){
		return (time1.getTime()-time2.getTime())/(1000);
	}
	public static int compareDate(Date d1, Date d2){
        if (d1.getTime() > d2.getTime()) {
            return 1;
        } else if (d1.getTime() < d2.getTime()) {
            return -1;
        } else {//相等
            return 0;
        }
}
	/**确定是否今天的日期*/
    public static boolean isToday(Date date){
        if(date == null) return false;
        return DateTOString1(GetNewDate()).equals(DateTOString1(date));
    }
    
    /**
	 * 获取系统日记记录时间
	 * @return 系统日记记录时间
	 */
	public static String GetCurrLogTime(){
		return ("	【" + Public.DateTOString(Public.GetNewDate()) + "】");
	}
	
	/**获取用时记录Str*/
	public static String getLogStr(String startTime, String endTime){
		long min = Public.compareTime3(Public.StringToDate1(endTime), Public.StringToDate1(startTime));
		int hour = (int) (min/3600);
		int minu = (int) (min%3600/60);
		int seco = (int) (min%3600%60);
		if(hour == 0){
			return minu+"分"+seco+"秒";
		}
		return hour+"小时"+minu+"分"+seco+"秒";
	}
    
    /**
	 * 将整数的个位四舍五入，如525要变成530
	 * @param integerstr 参数必须是一个整数
	 * @return
	 */
	public static String numF(String integerstr){
		int lastnum = Integer.parseInt(integerstr.substring(integerstr.length()-1));
		if(lastnum>=5){
			return (Integer.parseInt(integerstr)-lastnum+10)+"";
		}else{
			return (Integer.parseInt(integerstr)-lastnum)+"";
		}
	}

	
	/**
	 * 获取文件的绝对路径
	 * @param fileName  /file.txt  这种格式
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String getSourcePath(String fileName)
	{
		return URLDecoder.decode(Public.class.getResource(fileName).getFile());
	}
	/**
	 * 从properties文件读取数据
	 * @tag properties 
	 * @param key
	 * @param proPath
	 * @return
	 */
	public static String getProVal(String key, String proPath)
	{
//		System.out.println(proPath);
		proPath= Public.getSourcePath(proPath);
		FileInputStream in = null;
		Properties properties = new Properties();
		try{
			in = new FileInputStream(proPath);
			if (in != null){
				properties.load(in);
			}
		} catch (IOException e){
			logger.error(ExceptionUtil.readStackTrace(e));
		} finally{
			if (in != null){
				try{
					in.close();
				} catch (IOException e){
					logger.error(ExceptionUtil.readStackTrace(e));
				}
			}
		}
		return properties.getProperty(key);
	}

}
