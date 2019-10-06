package com.lky.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import com.lky.utils.JDBCUtils;
import com.lky.vo.User;

/**
 * @author : 猕猴桃
 * 实现数据库操作方法，进行数据的添加与查询
 * 1.前台校验
 * 2.后台校验（prepareStatement）
 * UserDaolmple.java: 这是UserDao的实现类
 */

public class UserDaoImple implements UserDao {
	// 先获取连接物件
	QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
	@Override
	public User login(User user) {
		String sql = "select * from user where username = ? and password = ?";
		User existUser;
		try {
			existUser = queryRunner.query(sql, new BeanHandler<User>(User.class), user.getUsername(),user.getPassword());
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("用户登录失败!");
		}
		return existUser;
	}

	@Override
	public int userRegDao(User user) {
		int index=0;
		// 定义sql
		String sql = "insert into user(username,password) values(?,?)";
		try {
			index = queryRunner.update(sql,user.getUsername(),user.getPassword());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return index;
	}

	/**
	 * 判断用户名在数据库中是否存在
	 */
	@Override
	public boolean userIsExist(String username) {
		String sql = "select * from user where username = ?";
		// 先获取连接物件
		Connection conn = JDBCUtils.getConnection();
		try {
			// 获取PreparedStatement对象，进行预编译
			PreparedStatement ps = conn.prepareStatement(sql);
			// 对用户对象属性赋值
			ps.setString(1, username);
			// 执行查询获取结果集
			ResultSet rs = ps.executeQuery();
			// 判断结果集是否有效
			if(!rs.next()){
				// 如果无效则证明此用户名可用
				return true;
			}
			// 释放此 ResultSet 对象的数据库和 JDBC 资源
			rs.close();
			// 释放此 PreparedStatement 对象的数据库和 JDBC 资源
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			// 关闭数据库连接
			JDBCUtils.closeConn(conn);
		}
		return false;
	}
}
