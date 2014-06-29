package org.dartmouth.service;

import java.util.List;
import java.util.Map;

import org.dartmouth.common.Result;
import org.dartmouth.domain.UserDO;

/**
 * @author Yaozhong Kang
 * @date May 20, 2014
 */
public interface UserService {
	Result addUser(UserDO user);

	UserDO login(String usr, String pwd);
	
	Result update(UserDO user);

	List<UserDO> findUser(Map<String, Object> query);
	
	Result sendMail(String email, Long time);
	
	Result addPasswordCase(UserDO user, Long time);
	
	Result checkKey(String caseKey);
	
	Long getUserId(String caseKey);

	Result deleteCaseById(Long id);
	
	Result setPassword(Long id, String password);
}
