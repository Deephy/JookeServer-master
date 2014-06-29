package org.dartmouth.dao;

import java.util.List;
import java.util.Map;

import org.dartmouth.common.Result;
import org.dartmouth.domain.UserDO;
/**
 * @author Yaozhong Kang
 * @date   May 20, 2014
 */
public interface UserDAO {
	Result insertUser(UserDO user);

	int updateUser(UserDO user);

	int deleteUserByQuery(Map<String, Object> params);

	List<UserDO> query(Map<String, Object> params);
	
	int setPassword(Long id, String password);
}
