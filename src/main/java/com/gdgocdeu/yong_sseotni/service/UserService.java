package com.gdgocdeu.yong_sseotni.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdgocdeu.yong_sseotni.dao.UserDao;
import com.gdgocdeu.yong_sseotni.vo.User;

@Service
public class UserService {
	
	@Autowired
	UserDao userDao;
	
	public int updatePw(User u) {
		return userDao.updatePw(u);
	}
	
	public int deleteUser(User u) {
		return userDao.deleteUser(u);
	}
	
	public int updateUser(User u) {
		return userDao.updateUser(u);
	}
	
	public User findByIdx(int user_idx) {
		return userDao.findByIdx(user_idx);
	}
	
	public User login(User u) {
		return userDao.login(u);
	}
	
	public User findByEmail(String user_email) {
		return userDao.findByEmail(user_email);
	}
	
	public int join(User u) {
		return userDao.join(u);
	}
	
}
