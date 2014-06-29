package org.dartmouth.domain;

import java.io.Serializable;

public class CacheEventDO implements Serializable{
	private static final long serialVersionUID = -94615121322621928L;
	
	private Long event_id;
	private Long host_id;
	private String host_ip;
	
	private Long event_time;
	private Long last_heartbeat_time;
	
	public Long getEvent_id() {
		return event_id;
	}
	public void setEvent_id(Long event_id) {
		this.event_id = event_id;
	}
	public String getHost_ip() {
		return host_ip;
	}
	public void setHost_ip(String host_ip) {
		this.host_ip = host_ip;
	}
	public Long getEvent_time() {
		return event_time;
	}
	public void setEvent_time(Long event_time) {
		this.event_time = event_time;
	}
	public Long getLast_heartbeat_time() {
		return last_heartbeat_time;
	}
	public void setLast_heartbeat_time(Long last_heartbeat_time) {
		this.last_heartbeat_time = last_heartbeat_time;
	}
	public Long getHost_id() {
		return host_id;
	}
	public void setHost_id(Long host_id) {
		this.host_id = host_id;
	}

}
