package org.dartmouth.service.impl;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dartmouth.cache.UserCache;
import org.dartmouth.common.CommonUtils;
import org.dartmouth.common.GlobalVariables;
import org.dartmouth.common.Result;
import org.dartmouth.dao.UserDAO;
import org.dartmouth.domain.CachePasswordDO;
import org.dartmouth.domain.UserDO;
import org.dartmouth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Yaozhong Kang
 * @date May 20, 2014
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

	private UserCache userCache = new UserCache();

	@Autowired
	private UserDAO userDAO;

	static Logger logger = Logger.getLogger(UserServiceImpl.class.getName());

	@Override
	public Result addUser(UserDO user) {
		Result result = this.userDAO.insertUser(user);
		return result;
	}

	@Override
	public UserDO login(String email, String pwd) {
		Result result = new Result();
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put(GlobalVariables.USER_DATABASE_COLUMN.USER_COLUMN_EMAIL, email);
		queryMap.put(GlobalVariables.USER_DATABASE_COLUMN.USER_COLUMN_PASSWORD, pwd);
		List<UserDO> users = findUser(queryMap);
		if (users.size() == 0){
			return null;
		}
		return users.get(0);
	}

	@Override
	public List<UserDO> findUser(Map<String, Object> query) {
		return userDAO.query(query);
	}

	@Override
	public Result update(UserDO user) {
		Result result = new Result();
		try {
			int lines = this.userDAO.updateUser(user);
			if (lines == 0) {
				result.setSuccess(false);
				result.setMsg(GlobalVariables.RESPONSE_MESSAGES.USER_NOT_EXIST);
			} else {
				result.setSuccess(true);
			}
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMsg(GlobalVariables.RESPONSE_MESSAGES.DB_ERROR);
		}
		return result;
	}

	@Override
	public Result sendMail(String email, Long time) {
		// TODO Auto-generated method stub

		return null;
	}

	@Override
	public Result addPasswordCase(UserDO user, Long time) {
		// TODO Auto-generated method stub

		String caseKey = null;
		try {
			caseKey = CommonUtils.generateMD5(user.getEmail() + time);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		CachePasswordDO cPwd = new CachePasswordDO();
		cPwd.setUserId(user.getUserid());
		cPwd.setEmail(user.getEmail());
		cPwd.setCreateTime(System.currentTimeMillis());
		cPwd.setKey(caseKey);

		userCache.addCase(cPwd);
		return null;
	}

	@Override
	public Result checkKey(String caseKey) {
		// TODO Auto-generated method stub
		Result result = new Result();
		List<CachePasswordDO> caseList = userCache.getCaseByKey(caseKey);
		
		// check existence
		if (caseList.size() == 0) {
			result.setSuccess(false);
			result.setMsg(GlobalVariables.RESPONSE_MESSAGES.KEY_NOT_EXIST);
			return result;
		}

		// could check matches

		// check the time
		if (System.currentTimeMillis() - caseList.get(0).getCreateTime() > GlobalVariables.EMAIL_SERVICE.EMAIL_EXPIRED_TIME_TOL) {
			result.setSuccess(false);
			result.setMsg(GlobalVariables.RESPONSE_MESSAGES.KEY_EXPIRED);
			deleteCaseById(caseList.get(0).getUserId());
			return result;
		}

		result.setSuccess(true);
		result.setMsg(GlobalVariables.RESPONSE_MESSAGES.KEY_MATCH);

		return result;
	}

	@Override
	public Long getUserId(String caseKey) {
		// TODO Auto-generated method stub
		List<CachePasswordDO> caseList = userCache.getCaseByKey(caseKey);
		if (caseList.size() == 0) {
			return -1L;
		}
		return caseList.get(0).getUserId();
	}

	@Override
	public Result deleteCaseById(Long id) {
		// TODO Auto-generated method stub
		Result result = new Result();
		result.setSuccess(userCache.deleteCase(id) == 0);
		return result;
	}

	@Override
	public Result setPassword(Long id, String password) {
		// TODO Auto-generated method stub
		Result result = new Result();
		int res = userDAO.setPassword(id, password);
		
		// What's the return value of jdbc ?
		result.setSuccess(true);
		return result;
	}
}
