package com.cml.common;

import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.cml.db.DBManager;

@Component
public class WebStartInit implements ApplicationListener<ContextRefreshedEvent> {
	
	public static Logger logger = Logger.getLogger(WebStartInit.class);
	
	public static void checkAndCreateDB(){
		DBManager db = DBManager.getInstance();
		try {
			if( !db.checkTable("dining_table") ){
				db.executeSQL( DBManager.CREATE_TABLE_SQL );
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void onApplicationEvent(ContextRefreshedEvent event) {
		// TODO Auto-generated method stub
		if( event.getApplicationContext().getParent() == null ){
//			checkAndCreateDB();
			TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
		}
	}
	
}
