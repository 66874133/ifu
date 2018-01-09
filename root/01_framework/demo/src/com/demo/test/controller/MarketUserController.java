package com.demo.test.controller;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.demo.test.model.User;
import com.demo.test.service.UserService;

@Controller
@Scope("prototype")
public class MarketUserController implements IController {

	@Autowired
	private UserService userService;

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		System.out.println("Get");
		PrintWriter out = response.getWriter();

		User user3 = new User();
		// 添加两条数据
		user3.setId(3);
		user3.setUsername("abc");
		user3.setPassword("132323");
		userService.addUser(user3);

		out.println("addUser");

	}
}
