package com.lky.utils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author : 猕猴桃
 * 反射工具类:通过反射调用方法
 */

public class BaseServlet extends HttpServlet {
	/*
	 * 它会根据请求中的名称，来决定调用本类的哪个方法
	 */
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		//System.out.println("baseServlet的service方法被执行了...");
		req.setCharacterEncoding("UTF-8");
		res.setContentType("text/html;charset=utf-8");
	/*	//1.获取请求路径
		String uri = req.getRequestURI();
		System.out.println("请求uri:"+uri);
		//2.获取方法名称
		String method_Name = uri.substring(uri.lastIndexOf('/') + 1);
		System.out.println("方法名称："+method_Name);
		//谁调用我？我代表谁
		System.out.println(this);	//com.lky.action.UserServlet???
	*/
		//Message
		String methodName = req.getParameter("method");// 它是一个方法名称
		// 当没用指定要调用的方法时，那么默认请求的是execute()方法。
		if(methodName == null || methodName.isEmpty()) {
			methodName = "execute";
		}
		//3.获取方法对象Method
		try {
			// 通过方法名称获取方法的反射对象
			Method m = this.getClass().getMethod(methodName, HttpServletRequest.class,
					HttpServletResponse.class);
			//暴力反射
			//m.setAccessible(true);
			// 反射方法目标方法，实现动态调用，也就是说，如果methodName为add，那么就调用add方法。
			String result = (String) m.invoke(this, req, res);
			// 通过返回值完成请求转发
			if(result != null && !result.isEmpty()) {
				req.getRequestDispatcher(result).forward(req, res);
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

}
