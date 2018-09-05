package com.tour.seckill.thread;

import com.tour.seckill.pojo.User;

public class UserThreadLocal {
	/**
	 * 如果需要传递多值则使用map集合封装
	 */
	private static ThreadLocal<User>userThread=new ThreadLocal();
	
	public static void set(User user){
		userThread.set(user);
	}
	public static User  get(){
		
		return userThread.get();
	}
	public static void remove(){
		userThread.remove();
	}
}
