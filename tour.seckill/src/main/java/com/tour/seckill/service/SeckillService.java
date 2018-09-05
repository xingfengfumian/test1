package com.tour.seckill.service;

import org.springframework.data.redis.core.RedisTemplate;

public interface SeckillService {
	public boolean seckill(RedisTemplate<String,Object> redisTemplate, String key,String userId);
	public String saveOrder(String productId,Long userId);
}
