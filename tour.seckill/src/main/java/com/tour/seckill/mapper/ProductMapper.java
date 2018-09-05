package com.tour.seckill.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.jt.common.mapper.SysMapper;
import com.tour.seckill.pojo.Product;

public interface  ProductMapper extends SysMapper<Product>{
	public void insertProductList(
			@Param("productList")Product[] abc);
	@Update("update product set num =num-1 where product_id=#{productId}")
	public void updateNum(String productId);
}
