package com.itheima;

import com.itheima.entity.Result;

import java.util.Map;

public interface OrderService {
    Result order(Map map) throws Exception;

    //根据id查询预约信息，包括体检人信息，套餐信息
    Map findById(Integer id) throws Exception;
}
