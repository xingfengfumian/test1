package com.tour.seckill.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.tour.seckill.mapper.OrderMapper;
import com.tour.seckill.mapper.ProductMapper;
import com.tour.seckill.pojo.Order;
@Service
public class SeckillServiceImpl implements SeckillService{
	@Autowired
	ProductMapper productMapper;
	@Autowired
	OrderMapper orderMapper;
	@Autowired
	RedisTemplate redisTemplate;
	@Override
	public boolean seckill(RedisTemplate<String, Object> redisTemplate, String key,String userId) {
		RedisLock lock = new RedisLock(redisTemplate, key, 10000, 20000);//key为productId
        try {
            if (lock.lock()) {//能否获得锁，获得是true
                // 需要加锁的代码,商品库存
                String pronum=lock.get("pronum");//k-v结构存在redis中的数据

                //修改库存
                if(Integer.parseInt(pronum)-1>=0) {
                    lock.set("pronum",String.valueOf(Integer.parseInt(pronum)-1));
                    System.out.println("库存数量:"+pronum+"成功!!!"+userId);
                    return true;
                }else {
                    System.out.println("没抢到");
                    return false;
                }
            } 

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 为了让分布式锁的算法更稳键些，持有锁的客户端在解锁之前应该再检查一次自己的锁是否已经超时，再去做DEL操作，因为可能客户端因为某个耗时的操作而挂起，
            // 操作完的时候锁因为超时已经被别人获得，这时就不必解锁了。 ————这里没有做
            lock.unlock();
        }
        return false;
    }
	
	public String saveOrder(String productId,Long userId) {
		boolean flag=seckill(redisTemplate, productId, userId+"");
		if(flag){
			Order order=new Order();
			order.setUserId(userId);
			order.setStatus(1);
			Date date = new Date();
			String orderId=userId+""+System.currentTimeMillis();
			order.setCreated(date);
			order.setUpdated(date);
			order.setOrderId(orderId);
			productMapper.updateNum(productId);
			orderMapper.insert(order);
			return orderId;
		}else{
			return "商品已经被抢完了";
		}
	}

}
