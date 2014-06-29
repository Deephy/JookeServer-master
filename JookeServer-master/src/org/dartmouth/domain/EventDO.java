package org.dartmouth.domain;

import java.io.Serializable;

import org.dartmouth.common.Settings;

/**
 * @author Yaozhong Kang
 * @date May 21, 2014
 */
public class EventDO extends BaseDO implements Serializable {
	private static final long serialVersionUID = -946154441322621928L;

	private static long count = 0;

	private Long event_id;
	private String event_name;
	private Boolean event_mode;
	private String event_zip_code;
	private Long event_time;
	private Boolean allow_add;

	private Float lat;
	private Float lon;
	private String pc_ip;

	private ParticipantDO host;

	public static synchronized long incrementCount() {
		count++;
		if (count == Settings.MAX_EVENT) {
			count = 0;
		}
		return count;
	}

	private EventDO(long id) {
		this.event_id = id;
	}
	
	public EventDO(){
		this.event_id = -1L;
	}

	public static EventDO getNewInstance() {
		EventDO event = new EventDO(0);
		return event;
	}

	public Long getEvent_id() {
		return event_id;
	}

	public void setEvent_id(Long event_id) {
		this.event_id = event_id;
	}

	public String getPc_ip() {
		return pc_ip;
	}

	public void setPc_ip(String pc_ip) {
		this.pc_ip = pc_ip;
	}

	public String getEvent_name() {
		return event_name;
	}

	public void setEvent_name(String event_name) {
		this.event_name = event_name;
	}

	public Boolean getEvent_mode() {
		return event_mode;
	}

	public void setEvent_mode(Boolean event_mode) {
		this.event_mode = event_mode;
	}

	public String getEvent_zip_code() {
		return event_zip_code;
	}

	public void setEvent_zip_code(String event_zip_code) {
		this.event_zip_code = event_zip_code;
	}

	public Long getEvent_time() {
		return event_time;
	}

	public void setEvent_time(Long event_time) {
		this.event_time = event_time;
	}

	public ParticipantDO getHost() {
		return host;
	}

	public void setHost(ParticipantDO host) {
		this.host = host;
	}

	// public Map<Long, ParticipantDO> getParticipant() {
	// return participant;
	// }
	//
	// public List<SongDO> getPlayList() {
	// return playList;
	// }
	//
	// public void setPlayList(List<SongDO> playList) {
	// this.playList = playList;
	// }

	public Float getLat() {
		return lat;
	}

	public void setLat(Float lat) {
		this.lat = lat;
	}

	public Float getLon() {
		return lon;
	}

	public void setLon(Float lon) {
		this.lon = lon;
	}

	public Boolean getAllow_add() {
		return allow_add;
	}

	public void setAllow_add(Boolean allow_add) {
		this.allow_add = allow_add;
	}

}
