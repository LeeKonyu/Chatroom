package com.lky.action;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.lky.dao.UserDao;
import com.lky.dao.UserDaoImple;
import com.lky.vo.User;
import org.apache.commons.beanutils.BeanUtils;

import com.lky.service.UserService;
import com.lky.utils.BaseServlet;

/**
 * @author : 猕猴桃
 * 前端控制器：通过请求指派来处理用户提交的信息
 */

public class UserServlet extends BaseServlet {

    /**
     * 检查session是否过期
     */
    // 调用Service层处理数据
    UserService us = new UserService();

    public String check(HttpServletRequest req,HttpServletResponse resp) throws IOException{
        // 从session中获得用户的信息
        User existUser = (User) req.getSession().getAttribute("existUser");
        // 判断session中的用户是否过期
        if(existUser == null){
            // 登录的信息已经过期了!
            resp.getWriter().println("1");
        }else{
            // 登录的信息没有过期
            resp.getWriter().println("2");
        }
        return null;
    }

    /**
     *  退出聊天室
     */
    public String exit(HttpServletRequest request,HttpServletResponse response) throws IOException{
        // 获得session对象
        HttpSession session = request.getSession();
        // 将session销毁.
        session.invalidate();
        // 跳转登录页面
        response.sendRedirect(request.getContextPath()+"/index.jsp");
        return null;
    }

    /**
     * 测试单人Servlet+JSP聊天，尝试在本页面获取和发送消息并更新消息，仅作测试（能够发送和接收消息，但不能选择聊天对象）
     *
     * ！！！！！！仅做测试！！！！！
     *
     *假设可以通过请求获得main.jsp聊天框中输入信息，并跳转到main.jsp页面，
     * 并在聊天记录显示框中显示聊天信息
     * */
    public String sendGetMessage(HttpServletRequest req,HttpServletResponse resp) throws ServletException,IOException{

        String chat_record = "";  //定义聊天记录变量，此处为全局变量

        String input_textarea = req.getParameter("input_textarea");
        Date now = new Date();    //创建日期对象，及系统当前时间
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String time = dateFormat.format( now ); //按照给定的日期格式获取系统当前时间
        String t = (String)req.getSession().getAttribute("nameSession");  //获取登陆页面用户名
        chat_record += t+"  "+input_textarea+"  "+time+"\n";   //聊天记录存储
        req.setAttribute("input_textarea",chat_record); //将当前聊天输入内容存储
        req.getRequestDispatcher("main.jsp").forward(req,resp);  //跳转到当前聊天输入界面，即界面布局不变

        return null;
    }

    /**
     * 发送聊天内容
     */
    public static int msgCount = 0;

    public String sendMessage(HttpServletRequest req,HttpServletResponse resp) throws IOException{
        // 1.接收数据 。
        System.out.println("sendMessage invoke....");
        String from = req.getParameter("from"); // 发言人
        String to = req.getParameter("to"); // 接收者
        String content = req.getParameter("content"); // 发言内容
        // 发言时间 正常情况下使用SimpleDateFormat
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sendTime = dateFormat.format(now); // 发言时间
        // 获得ServletContext对象.
        ServletContext application = getServletContext();
        //  从ServletContext中获取消息
        String sourceMessage = (String) application.getAttribute("message");
        // 在发送消息量小于100条时
        // 拼接发言的内容:xx 对 yy 说 xxx
        if(msgCount<100) {
            sourceMessage += "<font color='blue'><strong>" + from
                    + "</strong></font>对<font color='green'>[" + to + "]</font>说："
                    + content + "（"
                    + sendTime + "）<br>";
        }
        else {
            sourceMessage += "禁止刷屏！<br>";
        }
        msgCount++;

        // 将消息存入到application的范围
        application.setAttribute("message", sourceMessage);

        return getMessage(req, resp);
    }

    /**
     * 获取消息的方法
     */
    public String getMessage(HttpServletRequest req,HttpServletResponse resp) throws IOException{
        String message = (String) getServletContext().getAttribute("message");
        if(message != null){
            resp.getWriter().println(message);
        }
        return null;
    }



    /**
     * 登录的功能
     */
    public String login(HttpServletRequest req,HttpServletResponse resp) throws  IOException{
        //编码设置
        req.setCharacterEncoding("UTF-8");
        // 获取用户名和密码数据
        Map<String, String[]> map = req.getParameterMap();
        // 封装对象
        User user = new User();
        // 封装数据
        try {
            // BeanUtils.populate( Object bean, Map properties )，
            // 这个方法会遍历map<key, value>中的key，
            // 如果bean中有这个属性，就把这个key对应的value值映射到bean的属性。
            // 映射的过程就是将页面中的内容先用request获得，然后再将之转换为Map，
            // 就是上面的req.getParameterMap();
            BeanUtils.populate(user, map);

            // 调用Service查询
            User existUser = us.login(user);
            // 判断用户对象是否为null
            if (existUser == null) {
                // 用户名密码或错误
                req.setAttribute("msg", "用户名或密码错误!");
                return "/index.jsp";
            } else {
                // 用户登录成功
                // 第一个BUG的解决:第二个用户登录后将之前的session销毁!
                req.getSession().invalidate();

                // 第二个BUG的解决:判断用户是否已经在Map集合中,存在：已经在列表中.销毁其session.
                // 获得到ServletCOntext中存的Map集合.
                Map<User, HttpSession> userMap = (Map<User, HttpSession>) getServletContext()
                        .getAttribute("userMap");
                // 判断用户是否已经在map集合中'
                if(userMap.containsKey(existUser)){
                    // 说用map中有这个用户.
                    HttpSession session = userMap.get(existUser);
                    // 将这个session销毁.
                    session.invalidate();
                }

                //如果登陆成功则记住密码
                String userName = req.getParameter("username");
                String passWord = req.getParameter("password");
                String checkBox = req.getParameter("save_password");
                if("save".equals(checkBox)){
                    //Cookie存取时用URLEncoder.encode进行编码（PS：读取时URLDecoder.decode进行编码）
                    String name = URLEncoder.encode(userName,"UTF-8");
                    //创建两个Cookie对象
                    Cookie nameCookie = new Cookie("username", name);
                    //设置Cookie的有效期为3天
                    nameCookie.setMaxAge(60 * 60 * 24 * 3);
                    String pwd = URLEncoder.encode(passWord,"UTF-8");
                    Cookie pwdCookie = new Cookie("password", pwd);
                    pwdCookie.setMaxAge(60 * 60 * 24 * 3);
                    resp.addCookie(nameCookie);
                    resp.addCookie(pwdCookie);
                }

                // 使用监听器:HttpSessionBandingListener作用在JavaBean上的监听器.
                req.getSession().setAttribute("existUser", existUser); // 登陆成功标记
                ServletContext application = getServletContext();

                String sourceMessage = "";

                if (null != application.getAttribute("message")) {
                    sourceMessage = application.getAttribute("message")
                            .toString();
                }

                sourceMessage += "系统公告：<font color='gray'>"
                        + existUser.getUsername() + "进入了聊天室！</font><br>";
                application.setAttribute("message", sourceMessage);

                resp.sendRedirect(req.getContextPath() + "/main.jsp");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 注册的功能
     * */
    public String reg(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        //实例化UserDao对象
        UserDao dao = new UserDaoImple();
        if (username!= null && !username.isEmpty() ){
            if (dao.userIsExist(username)){
                User user = new User();
                user.setUsername(username);
                user.setPassword(password);
                //保存用户注册信息
                dao.userRegDao(user);
                req.setAttribute("info","恭喜，注册成功！");
            }else {
                req.setAttribute("info","注册失败：此用户名已经存在");
            }
        }
        req.getRequestDispatcher("/index.jsp").forward(req, resp);

        return null;
    }



}
