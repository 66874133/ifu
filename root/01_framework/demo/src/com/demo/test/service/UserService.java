package com.demo.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.test.dao.UserDao;
import com.demo.test.model.User;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;

	public void addUser(User user) {
		userDao.addUser(user);
	}

	public User getUser(User user) {
		return userDao.getUser(user);
	}

	public void deleteUser(int id) {
		userDao.deleteUser(id);
	}

	public void updateUser(User user) {
		userDao.updateUser(user);
	}

}
