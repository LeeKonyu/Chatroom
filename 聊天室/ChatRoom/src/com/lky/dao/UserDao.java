package com.lky.dao;

import com.lky.vo.User;

/**
 * @author : 猕猴桃
 * 与用户相关的数据库操作方法被封装到UserDao类中
 */
public interface UserDao {

	public User login(User user);

	public int userRegDao(User user);

	public boolean userIsExist(String username);
}
