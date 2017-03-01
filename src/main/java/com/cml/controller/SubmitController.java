package com.cml.controller;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cml.bean.Person;
import com.cml.common.CSVFileUtil;
import com.cml.common.Common;
import com.cml.db.DBManager;

@Controller
public class SubmitController implements ControllerIntf{
	private Logger logger = Logger.getLogger(SubmitController.class);
	
	@RequestMapping("/hello")
	public String hello(Model model) throws Exception {
		// TODO Auto-generated method stub
		model.addAttribute("name", "liumeng");
		Person p1 = new Person("liumeng", "0","1", new Date());
		Person p2 = new Person("chenmingli", "1","1", new Date());
		DBManager db = DBManager.getInstance();
		db.executeUpdateOrInsert(DBManager.INSERT_TABLE_SQL, p1);
		db.executeUpdateOrInsert(DBManager.INSERT_TABLE_SQL, p2);
		String dStr = Common.dateToString(null, new Date());
		String select = "select * from dining_table where p_date = '"+ dStr +"'";
		List<Person> list = db.executeQuery(select);
		for(Person p : list){
			logger.info(p.toString());
		}
		logger.info("controller hello.....");
		return "hello";
	}
	
	
	@RequestMapping("/table")
	public ModelAndView table(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		String dStr = req.getParameter("currentDate");
		if(StringUtils.isEmpty(dStr)){
			dStr = Common.dateToString(null, new Date());
		}
		logger.info("query date is : " + dStr);
		DBManager db = DBManager.getInstance();	
		String select = "select id,p_name,p_dining_m,p_dining_a,p_date from dining_table where p_date = '"+ dStr +"'";
		List<Person> list = new ArrayList<Person>();
		try {
			list = db.executeQuery(select);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ModelAndView mView = new ModelAndView();
		mView.addObject("personList", list);
		dStr = dStr.substring(0,10);
		mView.addObject("currentDate", dStr);
		mView.setViewName("table");
		return mView;
	}
	
	@SuppressWarnings("deprecation")
	@ResponseBody 
	@RequestMapping("/submit")
	public Map<String, Object>  submit(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		Date chineseDateTime = new Date();
		logger.info(chineseDateTime);
		Common comm = Common.getInstance();
		if( !comm.isInZone(comm.getStartTimeMorning(), comm.getEndTimeMorning(), comm.getCurrentTime(chineseDateTime)) && !comm.isInZone(comm.getStartTimeAfternoon(), comm.getEndTimeAfternoon(), comm.getCurrentTime(chineseDateTime)) ){
			map.put("result", RESP_CODE_TIMEOUT);
			StringBuilder builder = new StringBuilder();
			builder.append(comm.getStartTimeStrMorning()).append("-").append(comm.getEndTimeStrMorning())
			.append(", ").append(comm.getStartTimeStrAfternoon()).append("-").append(comm.getEndTimeStrAfternoon());
			map.put("valid_time", builder.toString());
			return map;
		}
		boolean flagMorning = true;
		if( chineseDateTime.getHours() > 12 ){
			flagMorning = false;
		}
		String p_name = req.getParameter("name");
		String p_dining_m = req.getParameter("morning");
		String p_dining_a = req.getParameter("afternoon");
		DBManager db = DBManager.getInstance();
		String p_date = Common.dateToString(null, chineseDateTime);
		List<Person> personList = db.executeQuery("select id,p_name,p_dining_m,p_dining_a,p_date from dining_table where p_name='"+ p_name +"' and p_date='"+p_date+"'");
		StringBuilder builder = new StringBuilder();
		if( personList.size() == 0 ){
			builder.append("创建就餐信息 （ ");
			Person person = null;
			if( flagMorning ){
				person = new Person(p_name, p_dining_m, p_dining_a, chineseDateTime);
			}else{
				person = new Person(p_name, "0", p_dining_a, chineseDateTime);
			}
			db.executeUpdateOrInsert(DBManager.INSERT_TABLE_SQL, person);
		}else{
			builder.append("更新就餐信息 （ ");
			Person person = personList.get(0);
			String sql = "";
			if( flagMorning ){
				sql = "update dining_table set p_dining_m = '"+ p_dining_m +"',p_dining_a = '"+ p_dining_a +"' where p_date = '"+ p_date +"' and p_name = '"+ p_name +"' and id = " +person.getP_id();
			}else{
				sql = "update dining_table set p_dining_a = '"+ p_dining_a +"' where p_date = '"+ p_date +"' and p_name = '"+ p_name +"' and id = " +person.getP_id();
			}
			db.executeSQL(sql);
		}
		String userIp = Common.getIpAddress(req);
		builder.append("姓名：").append(p_name).append("; 是否在食堂吃饭：");
		if(flagMorning){
			builder.append("上午：").append(p_dining_m.equals("1") ? "是":"否").append(", ");
		};
		builder.append("下午:").append(p_dining_a.equals("1") ? "是":"否").append("; 提交请求用户IP：").append(userIp);
		logger.info(builder.toString());
		map.put("result", RESP_CODE_SUCCESS);
		return map;
	}
	
	public String getRequestParameter(HttpServletRequest req, String key){
		String str = null;
		str = req.getParameter(key);
		if(!StringUtils.isEmpty(str)){
			try {
				str = URLDecoder.decode(str, "utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return str;
	}
	
	@RequestMapping("/exportFile")
	public void exportFile( HttpServletRequest req, HttpServletResponse resp){
		String date = req.getParameter("date");
		if( StringUtils.isEmpty(date) ){
			return;
		}
		logger.info("down file start... date : " + date);
		DBManager db = DBManager.getInstance();	
		String fileName = "all_info_";
		String select = "select id,p_name,p_dining_m,p_dining_a,p_date from dining_table";
		if( !date.equals("all") ){
			select += " where p_date = '"+ date +"'";
			fileName = date + "_info_";
		}
		List<Person> list = new ArrayList<Person>();
		try {
			list = db.executeQuery(select);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		String[] colNames = new String[]{"序号","姓名","上午","下午","日期"};
		try{
			OutputStream os = resp.getOutputStream();
			CSVFileUtil.responseSetProperties(fileName, resp);
			CSVFileUtil.doExport(list, colNames, os);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@ResponseBody 
	@RequestMapping("/figureData")
	public Map<String, Object>  figureData(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		DBManager db = DBManager.getInstance();	
		List<String> dateList = db.executeQueryCol("select distinct p_date from dining_table order by p_date");
		Map<String, Integer> mapAll = db.executeQueryNumber("select p_date,count(*) from dining_table group by p_date");
		Map<String, Integer> mapM = db.executeQueryNumber("select p_date,count(*) from dining_table where p_dining_m = '1' and p_dining_a = '0' group by p_date order by p_date");
		Map<String, Integer> mapA = db.executeQueryNumber("select p_date,count(*) from dining_table where p_dining_m = '0' and p_dining_a = '1' group by p_date order by p_date");
		Map<String, Integer> mapMA = db.executeQueryNumber("select p_date,count(*) from dining_table where p_dining_m = '1' and p_dining_a = '1' group by p_date order by p_date");
		Map<String, Integer> mapNMA = db.executeQueryNumber("select p_date,count(*) from dining_table where p_dining_m = '0' and p_dining_a = '0' group by p_date order by p_date");
		resultMap.put("columns", dateList);
		resultMap.put("count", mapAll);
		resultMap.put("morning", mapM);
		resultMap.put("afternoon", mapA);
		resultMap.put("allDay", mapMA);
		resultMap.put("allDayNot", mapNMA);
		resultMap.put("result", RESP_CODE_SUCCESS);
		return resultMap;
	}
	
	@RequestMapping("/figure")
	public String figure() {
		// TODO Auto-generated method stub
		return "figure";
	}
	
	@RequestMapping("/index")
	public ModelAndView index() {
		// TODO Auto-generated method stub
		Calendar cal = Calendar.getInstance();
		int currentHour = cal.get(Calendar.HOUR_OF_DAY);
    	logger.info("current time is : " + currentHour);
    	ModelAndView modelV = new ModelAndView();
    	modelV.setViewName("index");
    	modelV.addObject("currentHour", currentHour);
		return modelV;
	}
}
