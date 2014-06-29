package org.dartmouth.domain;

import java.io.Serializable;

public class CachePasswordDO implements Serializable{
	private static final long serialVersionUID = -936151223452621928L;
	
	private Long userId;
	private String email;
	private Long createTime;
	private String key;
	
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
}
