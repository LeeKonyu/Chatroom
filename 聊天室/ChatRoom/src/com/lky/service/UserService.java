package com.lky.service;

import com.lky.dao.UserDao;
import com.lky.dao.UserDaoImple;
import com.lky.vo.User;



/**
 * @author : 猕猴桃
 * 用户登陆服务类
 */

public class UserService {

	public User login(User user) {
		//声明Dao层对象
		UserDao dao = new UserDaoImple();
		//防止SQL注入
		return dao.login(user);
	}


//	//用户注册
//	public int userRegService(User user) {
//		UserDao dao = new UserDaoImple();
//		return dao.userRegDao(user);
//	}

}
