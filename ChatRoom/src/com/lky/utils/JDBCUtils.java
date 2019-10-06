package com.lky.utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author : 猕猴桃
 * JDBC工具类进行加载驱动，获得连接和释放资源
 */

public class JDBCUtils {
	
	private static ComboPooledDataSource dataSource = new ComboPooledDataSource();
	
	// 获得连接池:
	public static DataSource getDataSource(){
		return dataSource;
	}
	
	// 获得连接
	public static Connection getConnection() {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	// 关闭数据库连接
	public static void closeConn(Connection conn){
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}



}
