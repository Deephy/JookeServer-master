package org.dartmouth.domain;

import java.io.Serializable;

/**
 * @author Yaozhong Kang
 * @date May 23, 2014
 */
public class ParticipantDO extends BaseDO implements Serializable {
	private static final long serialVersionUID = -7415415777806857278L;

	private Long id;
	private String host_ip;
	private Long time;
	private EventDO event;

	private String name;
	private String profile_img;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getHost_ip() {
		return host_ip;
	}

	public void setHost_ip(String host_ip) {
		this.host_ip = host_ip;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public void hostEvent(EventDO e) {
		this.event = e;
		e.setHost(this);
		this.time = System.currentTimeMillis();
	}

	public void leaveEvent() {
		if (this.event != null) {
			this.event = null;
			this.time = null;
		}
	}

	public String getProfile_img() {
		return profile_img;
	}

	public void setProfile_img(String profile_img) {
		this.profile_img = profile_img;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		ParticipantDO obj = (ParticipantDO) o;
		return this.id.equals(obj.id);
	}
}
