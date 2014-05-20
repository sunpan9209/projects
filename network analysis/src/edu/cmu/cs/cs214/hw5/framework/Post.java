package edu.cmu.cs.cs214.hw5.framework;

import java.util.Date;

public class Post {
	private String id;
	private String message;
	private int sharedTimes;
	private Date time;
	
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getSharedTimes() {
		return sharedTimes;
	}
	public void setSharedTimes(int sharedTimes) {
		this.sharedTimes = sharedTimes;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getTime() {
		return time;
	}
	public void setCreatedAt(Date time) {
		this.time = time;
	}
}
