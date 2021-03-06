package org.dartmouth.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dartmouth.common.CommonUtils;
import org.dartmouth.common.GlobalVariables;
import org.dartmouth.domain.EventDO;
import org.dartmouth.domain.UserDO;
import org.dartmouth.service.EventService;
import org.dartmouth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Yaozhong Kang
 * @date May 19, 2014
 */
@Controller
public class TestController {
	@Autowired
	private EventService eventService;

	@Autowired
	private UserService userService;

	static Logger logger = Logger.getLogger(TestController.class.getName());

	@RequestMapping(value = "/test/listevents")
	public void lisete(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		List<EventDO> list = eventService.getAll();
		StringBuffer buffer = new StringBuffer();
		for (EventDO e : list) {
			buffer.append(CommonUtils.testObj2String(e));
			buffer.append("\n");
			buffer.append(CommonUtils.testObj2String(e.getHost()));
			buffer.append("\n");
			buffer.append("\n");
		}
		response.getWriter().append(buffer.toString());
	}

	@RequestMapping(value = "/test/listusers")
	public void list(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			List<UserDO> list = this.userService
					.findUser(new HashMap<String, Object>());
			StringBuffer buffer = new StringBuffer();
			for (UserDO u : list) {
				buffer.append(CommonUtils.testObj2String(u));
				buffer.append("\n");
			}
			response.getWriter().append(buffer.toString());
		} catch (Exception e) {
			
			response.getWriter().append("System Error");
		}
	}

	@RequestMapping(value = "/test/clearevents")
	public void clear(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			this.eventService.deleteAll();
			response.getWriter().append("done");
		} catch (Exception e) {
			response.getWriter().append("System Error");
		}
	}
}
