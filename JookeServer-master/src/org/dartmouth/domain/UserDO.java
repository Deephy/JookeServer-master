package org.dartmouth.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Yaozhong Kang
 * @date May 20, 2014
 */
public class UserDO extends BaseDO implements Serializable {
	private static final long serialVersionUID = 6978089255719569084L;
	private Long userid;
	private Long thirdparty_id;
	private Integer signup_type;
	private String fullname;
	private String email;
	private String password;
	private Boolean gender;
	private String profile_img;
	private String twitter_link;
	private String instagram_link;
	private String facebook_link;
	private Date last_modified;
	private Date created_at;
	


	public Long getThirdparty_id() {
		return thirdparty_id;
	}

	public void setThirdparty_id(Long thirdparty_id) {
		this.thirdparty_id = thirdparty_id;
	}

	public Integer getSignup_type() {
		return signup_type;
	}

	public void setSignup_type(Integer signup_type) {
		this.signup_type = signup_type;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getGender() {
		return gender;
	}

	public void setGender(Boolean gender) {
		this.gender = gender;
	}

	public String getProfile_img() {
		return profile_img;
	}

	public void setProfile_img(String profile_img) {
		this.profile_img = profile_img;
	}

	public String getTwitter_link() {
		return twitter_link;
	}

	public void setTwitter_link(String twitter_link) {
		this.twitter_link = twitter_link;
	}

	public String getInstagram_link() {
		return instagram_link;
	}

	public void setInstagram_link(String instagram_link) {
		this.instagram_link = instagram_link;
	}

	public String getFacebook_link() {
		return facebook_link;
	}

	public void setFacebook_link(String facebook_link) {
		this.facebook_link = facebook_link;
	}

	public Date getLast_modified() {
		return last_modified;
	}

	public void setLast_modified(Date last_modified) {
		this.last_modified = last_modified;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

}
