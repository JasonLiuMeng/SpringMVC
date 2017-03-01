package com.cml.bean;

import java.io.Serializable;
import java.util.Date;

import com.cml.common.Common;

public class Person implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3607660433591624117L;
	private int p_id;
	private String p_name;
	private String p_dining_m;
	private String p_dining_a;
	private Date p_time;
	
	public static final String YES = "是";
	public static final String NO = "否";
			
	
	public Person(int p_id, String p_name, String p_dining_m, String p_dining_a, Date p_time) {
		this.p_id = p_id;
		this.p_name = p_name;
		this.p_dining_m = p_dining_m;
		this.p_dining_a = p_dining_a;
		this.p_time = p_time;
	}
	
	public Person(String p_name, String p_dining_m, String p_dining_a, Date p_time) {
		this.p_name = p_name;
		this.p_dining_m = p_dining_m;
		this.p_dining_a = p_dining_a;
		this.p_time = p_time;
	}

	public int getP_id() {
		return p_id;
	}

	public void setP_id(int p_id) {
		this.p_id = p_id;
	}

	public String getP_name() {
		return p_name;
	}

	public void setP_name(String p_name) {
		this.p_name = p_name;
	}

	public String getP_dining_m() {
		return p_dining_m;
	}

	public void setP_dining_m(String p_dining_m) {
		this.p_dining_m = p_dining_m;
	}

	public String getP_dining_a() {
		return p_dining_a;
	}

	public void setP_dining_a(String p_dining_a) {
		this.p_dining_a = p_dining_a;
	}

	public Date getP_time() {
		return p_time;
	}

	public void setP_time(Date p_time) {
		this.p_time = p_time;
	}
	
	public String getMorningStr(){
		return ( this.p_dining_m.equals("1") ? YES : NO );
	}
	
	public String getAfternoonStr(){
		return ( this.p_dining_a.equals("1") ? YES : NO );
	}
	
	public String getDateStr(){
		return Common.dateToString(null, this.p_time);
	}
	
	
	public String toString(){
		return this.p_id + " - " + this.p_name + " - " + this.p_dining_m + " - " + this.p_dining_a + " - " + Common.dateToString(null, this.p_time) ;
	}
	
}
