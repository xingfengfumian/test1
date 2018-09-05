package com.tour.seckill.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.druid.util.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tour.seckill.pojo.User;
import com.tour.seckill.thread.UserThreadLocal;

import redis.clients.jedis.JedisCluster;

public class UserInterceptor implements HandlerInterceptor{
	/**
	 * 1.判断用户客户端是否有cookie/token数据
	 * 	如果用户没有则重定向到用户登录页面
	 * 2.如果用户token中有数据，则从redis缓存中取数据	
	 * 	如果redis 缓存中数据为null，则重定向到等登录页面
	 */
	ObjectMapper objectMapper=new ObjectMapper();
	@Autowired
	JedisCluster jedisCluster;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		Cookie[] cookies=request.getCookies();
		String token=null;
		for (Cookie c:cookies){
			if("JT_TICKET".equals(c.getName())){
				token=c.getValue();
				break;
			}
		}
		if(!StringUtils.isEmpty(token)){
			String userJson = jedisCluster.get(token);
			if(!StringUtils.isEmpty(userJson)){
				//将用户的信息转化为user对象
				User user=objectMapper.readValue(userJson, User.class);
				//思路：使用session共享数据
				//request.getSession().setAttribute("JT_USER", "user");
				UserThreadLocal.set(user);
				return true;
			}
		}
		response.sendRedirect("/user/login.html");
		return false;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		UserThreadLocal.remove();
	}
	
}