package org.dartmouth.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dartmouth.common.GlobalVariables;
import org.dartmouth.common.Result;
import org.dartmouth.domain.CachePasswordDO;
import org.dartmouth.domain.UserDO;
import org.dartmouth.mail.MailManager;
import org.dartmouth.service.UserService;
import org.json.JSONStringer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Yaozhong Kang
 * @date May 20, 2014
 */
@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private JavaMailSender mailSender;

	static Logger logger = Logger.getLogger(UserController.class.getName());

	// TODO @RequestMapping(method = RequestMethod.GET)
	@RequestMapping(value = "/login")
	public void login(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		JSONStringer stringer = new JSONStringer();
		try {
			int loginType = Integer.valueOf(request
					.getParameter(GlobalVariables.REQUEST_KEYS.KEY_LOGIN_TYPE));

			// if is login using third party account
			if (loginType != 0) {
				Long third_party_id = Long
						.valueOf(request
								.getParameter(GlobalVariables.REQUEST_KEYS.KEY_THIRD_PARTY_ID));
				Map<String, Object> query = new HashMap<String, Object>();
				query.put(
						GlobalVariables.USER_DATABASE_COLUMN.USER_COLUMN_THIRD_PARTY_ID,
						third_party_id);
				query.put(
						GlobalVariables.USER_DATABASE_COLUMN.USER_COLUMN_SIGNUP_TYPE,
						loginType);

				List<UserDO> users = this.userService.findUser(query);
				// if it is the first time the user login, then create a new
				// profile
				if (users.size() == 0) {
					UserDO user = new UserDO();
					user.fillByRequest(request);
					user.setSignup_type(loginType);

					if (user.getThirdparty_id() == null
							|| user.getFullname() == null
							|| user.getProfile_img() == null) {
						stringer.object()
								.key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
								.value(false)
								.key(GlobalVariables.RESPONSE_KEYS.MSG)
								.value("Fail to add user").endObject();
						response.getWriter().append(stringer.toString());
						return;
					}
					this.userService.addUser(user);
				}
				stringer.object().key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
						.value(true).endObject();
				response.getWriter().append(stringer.toString());
				return;
			}
			String email = request
					.getParameter(GlobalVariables.REQUEST_KEYS.KEY_EMAIL);
			String pwd = request
					.getParameter(GlobalVariables.REQUEST_KEYS.KEY_PASSWORD);

			// Invalid parameters
			if (email == null || pwd == null || email.length() == 0
					|| pwd.length() == 0) {
				stringer.object()
						.key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
						.value(false)
						.key(GlobalVariables.RESPONSE_KEYS.MSG)
						.value(GlobalVariables.RESPONSE_MESSAGES.INVALID_PARAMETERS)
						.endObject();
				response.getWriter().append(stringer.toString());
				return;
			}

			UserDO user = userService.login(email, pwd);

			if (user == null) {
				stringer.object().key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
						.value(false).key(GlobalVariables.RESPONSE_KEYS.MSG)
						.value(GlobalVariables.RESPONSE_MESSAGES.USER_NOT_EXIST)
						.endObject();
				response.getWriter().append(stringer.toString());
				return;
			}

			stringer.object().key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
					.value(true)
					.key(GlobalVariables.RESPONSE_KEYS.USERID)
					.value(user.getUserid())
					.key(GlobalVariables.RESPONSE_KEYS.MSG)
					.value(null)
					.key(GlobalVariables.RESPONSE_KEYS.FULLNAME)
					.value(user.getFullname())
					.key(GlobalVariables.RESPONSE_KEYS.PROFILE_IMG)
					.value(user.getProfile_img())
					.endObject();
			response.getWriter().append(stringer.toString());

		} catch (Exception e) {
			stringer.object().key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
					.value(false).key(GlobalVariables.RESPONSE_KEYS.MSG)
					.value(GlobalVariables.RESPONSE_MESSAGES.SYSTEM_ERROR)
					.endObject();
			response.getWriter().append(stringer.toString());
		}
	}

	@RequestMapping(value = "/signup")
	public void signup(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		JSONStringer stringer = new JSONStringer();
		try {
			UserDO user = new UserDO();
			user.fillByRequest(request);

			// The parameters are not valid for the request
			if (user.getEmail() == null || user.getFullname() == null
					|| user.getPassword() == null
					|| user.getSignup_type() == null
					|| user.getEmail().length() == 0
					|| user.getFullname().length() == 0
					|| user.getPassword().length() == 0) {
				stringer.object()
						.key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
						.value(false)
						.key(GlobalVariables.RESPONSE_KEYS.MSG)
						.value(GlobalVariables.RESPONSE_MESSAGES.INVALID_PARAMETERS)
						.endObject();
				response.getWriter().append(stringer.toString());

			} else {

				Result result = userService.addUser(user);
				stringer.object().key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
						.value(result.isSuccess())
						.key(GlobalVariables.RESPONSE_KEYS.USERID)
						.value((Long) result.getResultObj())
						.key(GlobalVariables.RESPONSE_KEYS.MSG)
						.value(result.getMsg()).endObject();
				response.getWriter().append(stringer.toString());

			}
		} catch (Exception e) {
			stringer.object().key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
					.value(false).key(GlobalVariables.RESPONSE_KEYS.MSG)
					.value(GlobalVariables.RESPONSE_MESSAGES.SYSTEM_ERROR)
					.endObject();
			response.getWriter().append(stringer.toString());
		}
	}

	@RequestMapping(value = "/get_profile")
	public void findPeople(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JSONStringer stringer = new JSONStringer();
		try {
			String userid = request
					.getParameter(GlobalVariables.REQUEST_KEYS.KEY_USER_ID);
			// Invalid parameters
			if (userid == null || userid.length() == 0) {
				stringer.object()
						.key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
						.value(false)
						.key(GlobalVariables.RESPONSE_KEYS.MSG)
						.value(GlobalVariables.RESPONSE_MESSAGES.INVALID_PARAMETERS)
						.endObject();
				response.getWriter().append(stringer.toString());
				return;

			}

			Map<String, Object> query = new HashMap<String, Object>();
			query.put(GlobalVariables.USER_DATABASE_COLUMN.USER_COLUMN_ID,
					Long.valueOf(userid));
			List<UserDO> result = userService.findUser(query);

			if (result.size() > 0) {
				UserDO user = result.get(0);
				stringer.object().key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
						.value(true)
						.key(GlobalVariables.REQUEST_KEYS.KEY_FULLNAME)
						.value(user.getFullname())
						.key(GlobalVariables.REQUEST_KEYS.KEY_INSTAGRAM_LINK)
						.value(user.getInstagram_link())
						.key(GlobalVariables.REQUEST_KEYS.KEY_FACEBOOK_LINK)
						.value(user.getFacebook_link())
						.key(GlobalVariables.REQUEST_KEYS.KEY_TWITTER_LINK)
						.value(user.getTwitter_link())
						.key(GlobalVariables.REQUEST_KEYS.KEY_PROFILE_IMG)
						.value(user.getProfile_img()).endObject();

			} else {
				stringer.object()
						.key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
						.value(false)
						.key(GlobalVariables.RESPONSE_KEYS.MSG)
						.value(GlobalVariables.RESPONSE_MESSAGES.USER_NOT_EXIST)
						.endObject();
			}
			response.getWriter().append(stringer.toString());
		} catch (Exception e) {
			stringer.object().key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
					.value(false).key(GlobalVariables.RESPONSE_KEYS.MSG)
					.value(GlobalVariables.RESPONSE_MESSAGES.SYSTEM_ERROR)
					.endObject();
			response.getWriter().append(stringer.toString());
		}
	}

	@RequestMapping(value = "/edit_profile")
	public void editProfile(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JSONStringer stringer = new JSONStringer();
		try {
			UserDO user = new UserDO();
			user.fillByRequest(request);

			// User not exist
			if (user.getUserid() == null) {
				stringer.object()
						.key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
						.value(false)
						.key(GlobalVariables.RESPONSE_KEYS.MSG)
						.value(GlobalVariables.RESPONSE_MESSAGES.INVALID_PARAMETERS)
						.endObject();
				response.getWriter().append(stringer.toString());
				return;
			}

			Result result = this.userService.update(user);

			stringer.object().key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
					.value(result.isSuccess())
					.key(GlobalVariables.RESPONSE_KEYS.MSG)
					.value(result.getMsg()).endObject();

			response.getWriter().append(stringer.toString());

		} catch (Exception e) {
			stringer.object().key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
					.value(false).key(GlobalVariables.RESPONSE_KEYS.MSG)
					.value(GlobalVariables.RESPONSE_MESSAGES.SYSTEM_ERROR)
					.endObject();
			response.getWriter().append(stringer.toString());
		}
	}

	@RequestMapping(value = "/forget_password")
	public void forgetPassword(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		// TODO

		/*
		 * 1. Check the email if existed No: return not assigned
		 * 
		 * 2. Yes: a. send email b. store info in the cache user_id email
		 * create_time
		 */

		String email = request
				.getParameter(GlobalVariables.REQUEST_KEYS.KEY_EMAIL);

		JSONStringer stringer = new JSONStringer();
		try {
			Map<String, Object> query = new HashMap<String, Object>();
			query.put(GlobalVariables.USER_DATABASE_COLUMN.USER_COLUMN_EMAIL,
					email);
			List<UserDO> result = userService.findUser(query);

			// User not existed
			if (result.size() == 0) {
				stringer.object()
						.key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
						.value(false)
						.key(GlobalVariables.RESPONSE_KEYS.MSG)
						.value(GlobalVariables.RESPONSE_MESSAGES.USER_NOT_EXIST)
						.endObject();
				response.getWriter().append(stringer.toString());
			} else {
				Long createTime = System.currentTimeMillis();

				// send email
				this.userService.sendMail(email, createTime);

				// put this in the sendMail function later when injection works
				MailManager mg = new MailManager(mailSender, createTime);
				mg.send(email);

				// save in the cache
				this.userService.addPasswordCase(result.get(0), createTime);

				stringer.object().key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
						.value(true).key(GlobalVariables.RESPONSE_KEYS.MSG)
						.value(GlobalVariables.RESPONSE_MESSAGES.SEND_EMAIL)
						.endObject();
				response.getWriter().append(stringer.toString());
			}

		} catch (Exception e) {

			stringer.object().key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
					.value(false).key(GlobalVariables.RESPONSE_KEYS.MSG)
					.value(GlobalVariables.RESPONSE_MESSAGES.SYSTEM_ERROR)
					.endObject();
			response.getWriter().append(stringer.toString());

		}

	}

	/* When the user click the link in the email, jump to this servlet */
	@RequestMapping(value = "/reset_password")
	public ModelAndView resetPassword(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			String caseKey = request
					.getParameter(GlobalVariables.REQUEST_KEYS.KEY_FORGET_PWD_CASE);
			Result result = this.userService.checkKey(caseKey);

			if (result.isSuccess()) {
				// load the webpage, and pass the key as the parameter
				ModelAndView modelAndView = new ModelAndView("set_password");
				modelAndView.addObject(
						GlobalVariables.REQUEST_KEYS.KEY_CASEKEY, caseKey);
				return modelAndView;
			} else {
				ModelAndView modelAndView = new ModelAndView("sorry");
				modelAndView.addObject("sorry", result.getMsg());
				return modelAndView;
			}

		} catch (Exception e) {
			ModelAndView modelAndView = new ModelAndView("sorry");
			modelAndView.addObject("sorry",
					GlobalVariables.RESPONSE_MESSAGES.SYSTEM_ERROR);
			return modelAndView;
		}

	}

	/* When user set password from the web page, jump to this servlet */
	@RequestMapping(value = "/set_password")
	public void setPassword(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		JSONStringer stringer = new JSONStringer();

		try {
			String caseKey = request
					.getParameter(GlobalVariables.REQUEST_KEYS.KEY_FORGET_PWD_CASE);
			String password = request
					.getParameter(GlobalVariables.REQUEST_KEYS.KEY_PASSWORD);
			String re_password = request
					.getParameter(GlobalVariables.REQUEST_KEYS.KEY_RE_PASSWORD);

			// The passwords not match
			if (!password.equals(re_password)) {

			}

			// Get the user id of this case number
			Long userId = this.userService.getUserId(caseKey);

			// If jump to this url, the key should has already been checked
			if (userId == -1L) {
				stringer.object().key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
						.value(false).key(GlobalVariables.RESPONSE_KEYS.MSG)
						.value(GlobalVariables.RESPONSE_MESSAGES.SYSTEM_ERROR)
						.endObject();
				response.getWriter().append(stringer.toString());

			} else {

				// Reset the password in the database
				this.userService.setPassword(userId, password);

				// Delete the case from the cache
				this.userService.deleteCaseById(userId);

				stringer.object()
						.key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
						.value(true)
						.key(GlobalVariables.RESPONSE_KEYS.MSG)
						.value(GlobalVariables.RESPONSE_MESSAGES.RESET_PWD_SUCCESS)
						.endObject();
				response.getWriter().append(stringer.toString());
			}

		} catch (Exception e) {

			stringer.object().key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
					.value(false).key(GlobalVariables.RESPONSE_KEYS.MSG)
					.value(GlobalVariables.RESPONSE_MESSAGES.SYSTEM_ERROR)
					.endObject();
			response.getWriter().append(stringer.toString());

		}

	}

	/* When user set password from the web page, jump to this servlet */
	@RequestMapping(value = "/test")
	public ModelAndView handleRequest(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception {

		ModelAndView modelAndView = new ModelAndView("set_password");
		modelAndView.addObject("caseKey", "hello kitty");

		return modelAndView;
	}
}
