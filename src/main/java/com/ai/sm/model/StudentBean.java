package com.ai.sm.model;

import javax.validation.constraints.NotEmpty;

public class StudentBean {
	@NotEmpty(message = "Student Id must not be empty")
	private String id;
	@NotEmpty(message = "Student name must not be empty")
	private String name;
	@NotEmpty(message = "Class name must not be empty")
	private String className;
	@NotEmpty(message = "Registered year must not be empty")
	private String year;
	@NotEmpty(message = "Registered month must not be empty")
	private String month;
	@NotEmpty(message = "Registered day must not be empty")
	private String day;
	@NotEmpty(message = "Status must not be empty")
	private String status;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
