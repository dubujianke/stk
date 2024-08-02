package com.alading.model;

import java.io.Serializable;

public class Stock implements Serializable {
	private long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	private String code;
	private String date;
	private String time;
	private String price;
	public String name;
	public double zf;

	public String getInfo() {
		return name+" "+code+" "+zf;
	}

	public String toString() {
		return getInfo();
	}

}
