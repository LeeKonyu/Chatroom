package com.lky.listener;

import com.lky.vo.User;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : 猕猴桃
 * 监听ServletContext对象创建和销毁
 * 还可以统计当前在线（登录）人数，并将其显示在聊天界面上。
 * 此处还要给MyServletContextListener监听器类在web.xml文件中配置路径！！！
 */


/**关于在线人数问题：
 * 在调用监听器类和online.jsp实现监听和显示系统当前在线人数时，
 * 本机上同一个浏览器同时登陆多个用户，
 * 只能算作一次，本机上不同浏览器登陆则分别算作一次。
 * */

public class MyServletContextListener implements ServletContextListener{
	/**
	 * ServletContext对象创建 下面这个方法就会执行
	 * ServletContextEvent事件对象. 监听器对象---》ServletContext对象.(事件源)
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		Map<User,HttpSession> userMap = new HashMap<User,HttpSession>();
		sce.getServletContext().setAttribute("userMap", userMap);
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
	}



}
