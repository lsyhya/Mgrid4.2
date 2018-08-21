package com.sg.common.lsyBase;

public class MyDoorTime {

	private String year,month,day,week,hour,min,sec;
	
	public MyDoorTime(String year,String month,String day,String week,String hour,String min,String sec)
	{
		this.year=year;
		this.month=month;
		this.day=day;
		this.week=week;
		this.hour=hour;
		this.min=min;
		this.sec=sec;
	}
	
	public MyDoorTime()
	{
		
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	public String getMin() {
		return min;
	}

	public void setMin(String min) {
		this.min = min;
	}

	public String getSec() {
		return sec;
	}

	public void setSec(String sec) {
		this.sec = sec;
	}
	
}
