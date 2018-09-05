package com.tour.seckill.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.vo.SysResult;
import com.tour.seckill.service.SeckillService;
import com.tour.seckill.service.SeckillTime;
import com.tour.seckill.thread.UserThreadLocal;

@Controller
@RequestMapping("/seckill")
public class SeckillController {
	@Autowired
	SeckillService seckillService;
	@RequestMapping("/doseckill/{productId}/{userId}")
	@ResponseBody
	public MappingJacksonValue doSeckill(String callback,@PathVariable String productId,@PathVariable Long userId){
		System.out.println(productId);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			Date date = simpleDateFormat.parse(SeckillTime.startTime);
			if(new Date().getTime()-date.getTime()>2000000){
				MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(SysResult.build(202, "抢购结束"));
				mappingJacksonValue.setJsonpFunction(callback);
				return mappingJacksonValue;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Integer userId=UserThreadLocal.get().getId();
		String orderId=seckillService.saveOrder(productId, userId);
		if("商品已经被抢完了".equals(orderId)){
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(SysResult.build(202, "抢购结束"));
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		}
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(SysResult.oK(orderId));
		mappingJacksonValue.setJsonpFunction(callback);
		return mappingJacksonValue;
	}
	@RequestMapping("/queryTime")
	@ResponseBody
	public MappingJacksonValue queryTime(String callback){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date;
		try {
			date = simpleDateFormat.parse(SeckillTime.startTime);
			Date nowTime = new Date();
			long time=date.getTime()-nowTime.getTime();
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(SysResult.oK(time));
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(SysResult.build(201,"cuowu"));
		mappingJacksonValue.setJsonpFunction(callback);
		return mappingJacksonValue;
	}
}
