package com.cml.common;

import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class Common {
	
	private static Logger logger = Logger.getLogger(Common.class);
	
	private static Common common = null;
	public static final String YYYYMMDD = "yyyy-MM-dd"; 
	public static final String DATE_FORMMAT = "yyyy-MM-dd HH:mm:ss";  
    private static final String HOUR_FORMMAT = "HH:mm";
    private static SimpleDateFormat sdf=new SimpleDateFormat(HOUR_FORMMAT);
    
	private long startTimeMorning = 0;
	private long endTimeMorning = 0;
	private String startTimeStrMorning = "";
	private String endTimeStrMorning = "";
	
	private long startTimeAfternoon = 0;
	private long endTimeAfternoon = 0;
	private String startTimeStrAfternoon = "";
	private String endTimeStrAfternoon = "";
	
	private Common(){
		 initCommonProperties();
	}
	
	public static Common getInstance(){
		if( null == common ){
			common = new Common();
		}
		return common;
	}

	
	public void initCommonProperties(){
		// TODO Auto-generated method stub
		logger.info("initialize common properties...");
		String path = getResourcePath("common.properties");
		try {
			FileInputStream in = new FileInputStream(path);
			Properties props = new Properties();
			props.load(in);
			this.startTimeStrMorning = props.getProperty("submit_start_time_morning", "00:00");
			this.endTimeStrMorning = props.getProperty("submit_end_time_morning", "23:59");
			this.startTimeMorning = getLong(startTimeStrMorning);
			this.endTimeMorning = getLong(endTimeStrMorning);
			
			this.startTimeStrAfternoon = props.getProperty("submit_start_time_afternoon", "00:00");
			this.endTimeStrAfternoon = props.getProperty("submit_end_time_afternoon", "23:59");
			this.startTimeAfternoon = getLong(startTimeStrAfternoon);
			this.endTimeAfternoon = getLong(endTimeStrAfternoon);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getResourcePath(String name){
		StringBuilder rootPath = new StringBuilder(System.getProperty("sign.web.root"));
		rootPath.append("/WEB-INF/classes/").append(name);
		return rootPath.toString();
	}
	
	
	public static String dateToString(String format, Date date){
		if( null == format || format.isEmpty() ){
			format = YYYYMMDD;
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		return simpleDateFormat.format(date);
	}
	
    /** 
     * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址, 
     * 参考文章： http://developer.51cto.com/art/201111/305181.htm 
     *  
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？ 
     * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。 
     *  
     * 如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130, 
     * 192.168.1.100 
     *  
     * 用户真实IP为： 192.168.1.110 
     *  
     * @param request 
     * @return 
     */  
    public static String getIpAddress(HttpServletRequest request) {  
        String ip = request.getHeader("x-forwarded-for");  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_CLIENT_IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getRemoteAddr();  
        }  
        return ip;  
    }  

    public boolean isInZone(long tStart,long tEnd,long t) throws ParseException {
    	logger.info("start:" + tStart);
    	logger.info("end:" + tEnd);
    	logger.info("current:" + t);
        return tStart <= t && t <= tEnd;
    }

    public long getLong(String timeStr) throws ParseException {
        return sdf.parse(timeStr).getTime();
    }

    public long getCurrentTime(Date date) throws ParseException {
        return getLong(sdf.format(date));
    }

	public long getStartTimeMorning() {
		return startTimeMorning;
	}

	public long getEndTimeMorning() {
		return endTimeMorning;
	}

	public String getStartTimeStrMorning() {
		return startTimeStrMorning;
	}

	public String getEndTimeStrMorning() {
		return endTimeStrMorning;
	}

	public long getStartTimeAfternoon() {
		return startTimeAfternoon;
	}

	public long getEndTimeAfternoon() {
		return endTimeAfternoon;
	}

	public String getStartTimeStrAfternoon() {
		return startTimeStrAfternoon;
	}

	public String getEndTimeStrAfternoon() {
		return endTimeStrAfternoon;
	}
  
}	
