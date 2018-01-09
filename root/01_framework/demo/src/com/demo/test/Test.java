package com.demo.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.demo.test.dao.UserDao;
import com.demo.test.model.User;
import com.demo.test.service.UserService;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String[] path = { "WebContent/WEB-INF/applicationContext.xml" };
		ApplicationContext ctx = new FileSystemXmlApplicationContext(path);
		//
		// ApplicationContext ctx = null;
		//
		// ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		UserService userService = ctx.getBean(UserService.class);
	
		User user3 = new User();
		// 添加两条数据
		user3.setId(3);
		user3.setUsername("abc");
		user3.setPassword("132323");
		userService.addUser(user3);
		
		UserDao userDao = (UserDao) ctx.getBean("userDao");
		User user = new User();
		// 添加两条数据
		user.setId(1);
		user.setUsername("Jessica");
		user.setPassword("123");
		userDao.addUser(user);
		user.setId(2);
		user.setUsername("Jessica2");
		user.setPassword("123");
		userDao.addUser(user);
		System.out.println("添加成功");
		// 查询数据
		user.setUsername("Jessica");
		user.setPassword("123");
		System.out.println(userDao.getUser(user).toString());
		user.setUsername("Jessica2");
		user.setPassword("123");
		System.out.println(userDao.getUser(user).toString());
		// 修改数据
		user.setId(2);
		user.setPassword("802");
		userDao.updateUser(user);
		System.out.println("修改成功");
		// 删除数据
		userDao.deleteUser(1);
		System.out.println("删除成功");

	}

}
