package com.tour.seckill.interceptor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tour.seckill.service.SeckillTime;
import com.tour.seckill.thread.UserThreadLocal;

import redis.clients.jedis.JedisCluster;

public class SeckillInterceptor implements HandlerInterceptor{
	ObjectMapper objectMapper=new ObjectMapper();
	static AtomicInteger count=new AtomicInteger(0);
	
	@Override
	public boolean preHandle(HttpServletRequest request, 
			HttpServletResponse response, Object handler)
			throws Exception {
		Date nowTime = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = simpleDateFormat.parse(SeckillTime.startTime);
		if(nowTime.getTime()>=date.getTime()){
			if(count.compareAndSet(100, 1)){
				return true;
			}
		}
		return false;
	}
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
		
	}
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}
}
