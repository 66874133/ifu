package com.demo.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.demo.test.controller.IController;

@SuppressWarnings("serial")
@Controller
public class DispatchServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("Get");

		String path = getServletContext().getContextPath();
		String spath = request.getServletPath();
		String url = request.getRequestURL().toString();
		String cp = request.getContextPath();

		System.out.println("path=" + path);
		System.out.println("getServletPath=" + spath);
		System.out.println("url=" + url);
		System.out.println("cp=" + cp);

		String bean = getBeanId(url);
		IController controller = (IController) WebApplicationContextUtils
				.getRequiredWebApplicationContext(getServletContext()).getBean(
						bean);

		try {
			controller.handle(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("Post");

		doGet(request, response);
	}

	private String getBeanId(String url) {
		int i = url.lastIndexOf('/');

		return url.substring(i + 1);
	}
}
