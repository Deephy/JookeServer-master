package org.dartmouth.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.dartmouth.common.GlobalVariables;
import org.dartmouth.common.Result;
import org.dartmouth.dao.UserDAO;
import org.dartmouth.domain.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author Yaozhong Kang
 * @date May 20, 2014
 */
@Repository
public class UserDAOImpl implements UserDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	static Logger logger = Logger.getLogger(UserDAOImpl.class.getName());

	@Override
	public Result insertUser(UserDO user) {
		Result result = new Result();
		// check for duplicate
		Map<String, Object> map = new HashMap<String, Object>();
		if (user.getThirdparty_id() != null) {
			map.put(GlobalVariables.USER_DATABASE_COLUMN.USER_COLUMN_SIGNUP_TYPE,
					user.getSignup_type());
			map.put(GlobalVariables.USER_DATABASE_COLUMN.USER_COLUMN_THIRD_PARTY_ID,
					user.getThirdparty_id());
		} else {
			map.put(GlobalVariables.USER_DATABASE_COLUMN.USER_COLUMN_EMAIL,
					user.getEmail());
		}
		List<UserDO> list = query(map);

		// Insert fails, user has already existed in the database
		if (list.size() > 0) {
			result.setSuccess(false);
			result.setResultObj(-1L);
			result.setMsg(GlobalVariables.RESPONSE_MESSAGES.SIGN_UP_DUPLICATE);
			return result;
		}

		// Insert the new user into the database
		String inserQuery = "insert into user (name,email,password,profile_img,signup_type,third_party_id,last_modified,created_at) values (?,?, ?, ?, ?, ?,NOW(), NOW()) ";
		Object[] params = new Object[] { user.getFullname(), user.getEmail(),
				user.getPassword(), user.getProfile_img(),
				user.getSignup_type(), user.getThirdparty_id() };
		int[] types = new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
				Types.VARCHAR, Types.INTEGER, Types.INTEGER };

		result.setSuccess(jdbcTemplate.update(inserQuery, params, types) == 1);
		if (result.isSuccess()) {
			list = query(map);
			result.setResultObj(list.get(0).getUserid());
			result.setMsg(null);
		} else {
			result.setResultObj(-1L);
			result.setMsg(GlobalVariables.RESPONSE_MESSAGES.DB_ERROR);
		}
		return result;
	}

	@Override
	public int updateUser(UserDO user) {
		
		boolean updateEvent = false;
		
		StringBuffer sql = new StringBuffer(
				"update user set user.gender=user.gender");
		StringBuffer sqlEvent = new StringBuffer("update event_table set event_table.allow_add = event_table.allow_add");
		
		if (user.getFullname() != null) {
			sql.append(", user.name='" + user.getFullname() + "'");
			
			updateEvent = true;
			sqlEvent.append(", event_table.host_name='" + user.getFullname() + "'");
		}
		if (user.getInstagram_link() != null) {
			sql.append(", user.instagramlink='" + user.getInstagram_link()
					+ "'");
		}
		if (user.getFacebook_link() != null) {
			sql.append(", user.facebooklink='" + user.getFacebook_link() + "'");
		}
		if (user.getTwitter_link() != null) {
			sql.append(", user.twitterlink='" + user.getTwitter_link() + "'");
		}
		if (user.getProfile_img() != null) {
			sql.append(", user.profile_img='" + user.getProfile_img() + "'");
			
			updateEvent = true;
			sqlEvent.append(", event_table.host_profile_img='" + user.getProfile_img() + "'");
		}
		sql.append(", user.last_modified = NOW()");
		sql.append(" where user.id=" + user.getUserid());
		sqlEvent.append(" where event_table.host_id =" + user.getUserid());
		
		
//		if (updateEvent){
//			jdbcTemplate.update(sqlEvent.toString());
//		}
		return jdbcTemplate.update(sql.toString());
	}

	@Override
	public int deleteUserByQuery(Map<String, Object> params) {
		return 0;
	}

	@Override
	public List<UserDO> query(Map<String, Object> params) {
		StringBuffer sql = new StringBuffer("select * from user where 1=1");
		Object[] ps = new Object[params.size()];
		int index = 0;
		for (Entry<String, Object> entry : params.entrySet()) {
			sql.append(" and ");
			sql.append(entry.getKey() + "=?");
			ps[index++] = entry.getValue();
		}
		sql.append(" order by id desc");
		List<UserDO> result = jdbcTemplate.query(sql.toString(), ps,
				new ResultSetExtractor<List<UserDO>>() {
					@Override
					public List<UserDO> extractData(ResultSet rs)
							throws SQLException, DataAccessException {

						List<UserDO> list = new ArrayList<UserDO>();
						while (rs.next()) {
							UserDO u = new UserDO();
							
							u.setCreated_at(rs.getDate(GlobalVariables.USER_DATABASE_COLUMN.USER_COLUMN_CREATED_AT));
							u.setEmail(rs.getString(GlobalVariables.USER_DATABASE_COLUMN.USER_COLUMN_EMAIL));
							u.setFacebook_link(rs.getString(GlobalVariables.USER_DATABASE_COLUMN.USER_COLUMN_FACEBOOK_LINK));
							u.setGender(rs.getBoolean(GlobalVariables.USER_DATABASE_COLUMN.USER_COLUMN_GENDER));
							u.setUserid(rs.getLong(GlobalVariables.USER_DATABASE_COLUMN.USER_COLUMN_ID));
							u.setInstagram_link(rs.getString(GlobalVariables.USER_DATABASE_COLUMN.USER_COLUMN_INSTAGRAM_LINK));
							u.setLast_modified(rs.getDate(GlobalVariables.USER_DATABASE_COLUMN.USER_COLUMN_LAST_MODIFIED));
							u.setFullname(rs.getString(GlobalVariables.USER_DATABASE_COLUMN.USER_COLUMN_NAME));
							u.setProfile_img(rs.getString(GlobalVariables.USER_DATABASE_COLUMN.USER_COLUMN_PROFILE_IMG));
							u.setPassword(rs.getString(GlobalVariables.USER_DATABASE_COLUMN.USER_COLUMN_PASSWORD));
							u.setSignup_type(rs.getInt(GlobalVariables.USER_DATABASE_COLUMN.USER_COLUMN_SIGNUP_TYPE));
							u.setThirdparty_id(rs.getLong(GlobalVariables.USER_DATABASE_COLUMN.USER_COLUMN_THIRD_PARTY_ID));
							u.setTwitter_link(rs.getString(GlobalVariables.USER_DATABASE_COLUMN.USER_COLUMN_TWITTER_LINK));
								
							
//							u.setCreated_at(rs.getDate("created_at"));
//							u.setEmail(rs.getString("email"));
//							u.setFacebook_link(rs.getString("facebooklink"));
//							u.setGender(rs.getBoolean("gender"));
//							u.setUserid(rs.getLong("id"));
//							u.setInstagram_link(rs.getString("instagramlink"));
//							u.setLast_modified(rs.getDate("last_modified"));
//							u.setFullname(rs.getString("name"));
//							u.setProfile_img(rs.getString("profile_img"));
//							u.setPassword(rs.getString("password"));
//							u.setSignup_type(rs.getInt("signup_type"));
//							u.setThirdparty_id(rs.getLong("third_party_id"));
//							u.setTwitter_link(rs.getString("twitterlink"));
							list.add(u);
						}
						return list;
					}
				});
		return result;
	}

	@Override
	public int setPassword(Long id, String password) {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer("update user set user.password='"
				+ password + "'");
		sql.append(" where user.id=" + id);
		return jdbcTemplate.update(sql.toString());
	}

}
