package com.tour.seckill.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.jt.common.mapper.SysMapper;
import com.tour.seckill.pojo.City;

public interface CityMapper extends SysMapper<City>{
	@Select("select * from city")
	List<City> selectCities();

}
