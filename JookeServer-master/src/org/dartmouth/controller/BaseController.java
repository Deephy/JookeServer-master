package org.dartmouth.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Yaozhong Kang
 * @date May 19, 2014
 */
@Controller
@RequestMapping("/")
public class BaseController {
	@RequestMapping(method = RequestMethod.GET)
	public void sayHello(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String user = request.getParameter("user");
		user = user == null ? "Kitty" : user;
		response.getWriter().write("Hello " + user);
	}
}
