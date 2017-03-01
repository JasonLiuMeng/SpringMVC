package com.cml.bean;

public class CountObj {
	
	private String date;
	private int count;
	
	public CountObj(String date, int count) {
		this.date = date;
		this.count = count;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "{" +this.date + "-" + this.count + "}";
	}
	
}
