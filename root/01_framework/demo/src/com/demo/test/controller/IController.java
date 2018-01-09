package com.demo.test.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IController {

	public void handle(HttpServletRequest request, HttpServletResponse response)
			throws Exception;
}
