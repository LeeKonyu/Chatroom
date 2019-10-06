# 聊天室的简要编写过程

最开始的时候，打算用JFrame和Socket以及多线程进行编写聊天室，可是在后来遇到了Servlet+JSP的方法后觉得用JFrame和Socket以及多线程太麻烦，于是便转换了思路

在查找资料以后，找到了黑马的关于聊天室的视频，便观看视频，查找资料，观看完视频以它的思路便试着敲代码了

## 1.问题描述

**利用Servlet+JSP+JDBC+JavaBean（观看黑马视频、看网盘中的视频、网上的博客与书上的案例）**

（1）编写一个登陆界面index.jsp，如果数据库存在该用户，输入的用户名和密码都正确，则登陆成功，跳入main.jsp聊天界面

（2）编写一个注册页面reg.jsp，注册标准是对照数据库中的用户名，如果用户名存在则注册失败，用户名不存在则注册成功，并跳入登陆页面index.jsp

（3）编写一个Servlet的程序UserServlet.java通过请求指派来处理用户提交的登陆信息，如果用户名和密码正确则跳入聊天界面main.jsp，如失败则在登陆页面提示错误

（4）在UserServlet.java中编写能够发送信息和接收方法sendGetMessage9（可以理解为该方法只能进行私聊，无法公聊）进行测试，尝试在消息输入的界面main.jsp（测试jsp）的信息输入框输入想要发送的信息，再点击发送按钮，能够在聊天记录显示框显示发送的消息。

（5）使用Cookie来记住用户名和其对应的密码

（6）使用监听器MyServletContextListener.java和online.jsp统计在线的人数，并显示在main.jsp中



## 2.解决方案

### 2.1预期效果

具有的功能应该有session是否过期、退出聊天室、测试聊天、发送与接收内容、登陆、注册、统计在线人数，这些方法都可以通过BaseServlet反射类进行调用并且可以和数据库进行一定的逻辑处理。

### 2.2系统结构示意图

![](C:\Users\Envy\Desktop\聊天室\Servlet.jpg)

详细流程：

（1）打开浏览器，进入登陆界面index.jsp

（2）若想要登陆，则在登陆界面输入用户名和密码，若用户名和密码正确跳入聊天界面main.jsp，若用户名和密码存在错误则弹出错误提示框

（3）若想要注册，则点击注册按钮跳入注册界面reg.jsp进行注册，注册成功或者失败都会跳入登陆界面index.jsp并提示注册成功与否

（4）在main.jsp聊天界面，其中左侧页面显示聊天好友列表和当前在线总人数（统计当前在线人数通过调用监听类和online.jsp来实现），中间主体页面显示用户在聊天输入框中输入消息，发送并显示在聊天消息显示框中功能。（实现聊天功能通过调用UserServlet.java类中的getMessage方法来实现），下面部分就是进行进行聊天发送与退出聊天室（实现发送和退出聊天室的功能分别是是UserServlet.java类中的sendMessage方法和exit方法）



### 2.3遇到的一些问题

（1）JQuery的语法与使用如：

```
// JQuery
// $("#online") == document.getElementById("online");
```

（2）前后端数据的连接

使用request对象来接收前端发送过来的数据，使用response对象将Java后端的数据返回给前端，使用Filter拦截器来处理中文乱码问题(Tomcat默认的编码是ISO-88591)（在刚开始的时候遇到的问题）。

（3）JDBC连接最新版数据库关于时区引发的问题的问题

要在url后加?serverTimezone

（4）关于cookie的问题（来源于网上博客）

```
main.jsp页面代码：Cookie[] cookies = request.getCookies();
开启服务第一次登录时是没有cookie的，所以执行 for (int i = 0; i < cookies.length; i++) 时会出现空指针异常。在for循环前加个if判断：if(cookies!=null)才能成功运行
```

（5）session.setAttribute()和session.getAttribute()配对使用，作用域是整个会话期间，在所有的页面都使用这些数据的时候使用。

（6）request.setAttribute()和request.getAttribute()配对使用，作用域是请求和被请求页面之间。request.setAttribute()是只在此action的下一个forward需要使用的时候使用；request.getAttribute()表示从request范围取得设置的属性，必须要先setAttribute设置属性，才能通过getAttribute来取得，设置与取得的为Object对象类型。其实表单控件中的Object的name与value是存放在一个哈希表中的，所以在这里给出Object的name会到哈希表中找出对应它value。setAttribute()的参数是String和Object。

（7）request.getParameter()表示接收参数，参数为页面提交的参数。包括：表单提交的参数、URL重写(就是xxx?id=1中的id)传的参数等，因此这个并没有设置参数的方法(没有setParameter())，而且接收参数返回的不是Object，而是String类型。

（8）request.getParameter() 和request.getAttribute() 区别

- （1）request.getParameter()取得是通过容器的实现来取得通过类似post，get等式传入的数据，request.setAttribute()和getAttribute()只是在web容器内部流转，仅仅是请求处理阶段。
- （2）request.getParameter()方法传递的数据，会从Web客户端传到Web服务器端，代表HTTP请求数据。request.getParameter()方法返回String类型的数据。request.setAttribute()和getAttribute()方法传递的数据只会存在于Web容器内部还有一点就是，HttpServletRequest类有setAttribute()方法，而没有setParameter()方法。

（9）request.getAttribute()与request.setAttribute()

（10）request.getAttribute("nameOfObj")可得到JSP页面一表单中控件的Value。其实表单控件中的Object的name与value是存放在一个哈希表中的，所以在这里给出Object的name会到哈希表中找出对应它的value。而不同页面间传值使用request.setAttribute(position, nameOfObj)时，只会从a.jsp到b.jsp一次传递，之后这个request就会失去它的作用范围，再传就要再设一个request.setAttribute()。而使用session.setAttribute()会在一个过程中始终保有这个值。

（11）response.sendRedirect(url)与request.getRequestDispatcher(url).forward(request,response)的区别

- response.sendRedirect(url)跳转到指定的URL地址，产生一个新的request，所以要传递参数只有在url后加参数，如：url?id=1.
- request.getRequestDispatcher(url).forward(request,response)是直接将请求转发到指定URL，所以该请求能够直接获得上一个请求的数据，也就是说采用请求转发，request对象始终存在，不会重新创建。而sendRedirect()会新建request对象，所以上一个request中的数据会丢失。





参考资料：

[怎样学习JSP？](https://www.zhihu.com/question/23984162/answer/689106407)

[javaweb：JDBC连线MYSQL资料库详解，使用JDBC对资料库进行CRUD操作以及DAO模式的介绍](<https://www.itread01.com/content/1543086560.html>)

[cookie实现记住密码](<https://www.cnblogs.com/Asdin/p/5867127.html>)

[监听器（实现在线统计人数，踢人）](http://blog.csdn.net/mm2223/article/details/7009560)

[Java Filter过滤机制详解](http://blog.csdn.net/ethanq/article/details/7336938)

[servlet请求转发与重定向的区别及页面跳转与传值](<https://blog.csdn.net/fox_bert/article/details/80634468>)

[JSP与Servlet的参数传递接收](<https://blog.csdn.net/rui15111/article/details/56686452>)

[分IP统计访问次数（监听器负责创建map,过滤器负责统计,JSP负责显示结果）](<https://blog.csdn.net/u012110719/article/details/45101615>)

[监听器和过滤器写的分类统计IP访问次数（map，jsp）](<https://blog.csdn.net/My_blankness/article/details/78429519>)

