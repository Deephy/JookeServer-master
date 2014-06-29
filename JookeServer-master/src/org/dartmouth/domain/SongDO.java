package org.dartmouth.domain;

import java.io.Serializable;
/**
 * @author Yaozhong Kang
 * @date   May 21, 2014
 */
public class SongDO extends BaseDO implements Serializable {

	private static final long serialVersionUID = 7042392106765347538L;
	private String title;
	private String artist;
	private Long duration;
	private String cover_img;
	private Long song_id;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public String getCover_img() {
		return cover_img;
	}

	public void setCover_img(String cover_img) {
		this.cover_img = cover_img;
	}

	public Long getSong_id() {
		return song_id;
	}

	public void setSong_id(Long song_id) {
		this.song_id = song_id;
	}

}
