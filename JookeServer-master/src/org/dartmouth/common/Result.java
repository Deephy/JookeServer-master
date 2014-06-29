package org.dartmouth.common;

import java.io.Serializable;

/**
 * @author Yaozhong Kang
 * @date May 20, 2014
 */
public class Result implements Serializable {
	private static final long serialVersionUID = 2211247238347525637L;
	
	
	Object resultObj;
	Boolean flag;
	String msg;

	public Object getResultObj() {
		return resultObj;
	}

	public void setResultObj(Object resultObj) {
		this.resultObj = resultObj;
	}

	public Boolean isSuccess() {
		return flag;
	}

	public void setSuccess(Boolean flag) {
		this.flag = flag;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
