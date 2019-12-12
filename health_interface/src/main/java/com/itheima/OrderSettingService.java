package com.itheima;

import com.itheima.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {
    void add(List<OrderSetting> dataList);

    List<Map> getOrderSettingByMonth(String date);//参数格式为：2019‐03

    void editNumberByDate(OrderSetting orderSetting);
}
