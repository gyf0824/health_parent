package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.OrderService;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao_gyf;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.entity.Result;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 体检预约服务
 */
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao_gyf memberDao;
    @Autowired
    private OrderDao orderDao;

    /**
     * 体检预约
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public Result order(Map map) throws Exception {
        //1、检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行预约
        String orderDate = (String) map.get("orderDate");
        Date date = DateUtils.parseString2Date(orderDate);
        //获取预约设置
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(date);
        if (orderSetting == null) {
            //说明预约设置没有进行设置，不能进行提前预约
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //走到这里说明预约设置可以使用
        // 2、检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约
        int number = orderSetting.getNumber();//可预约人数
        int reservations = orderSetting.getReservations();//已预约人数
        if (reservations >= number) {
            //预约已满，不能预约
            return new Result(false, MessageConstant.ORDER_FULL);
        }
        //检查当前用户是否为会员，根据手机号判断
        String telephone = (String) map.get("telephone");
        //根据手机号判断是否为会员
        Member member = memberDao.findByTelephone(telephone);
        //防止重复预约
        // 3、检查用户是否重复预约（同一个用户在同一天预约了同一个套餐），如果是重复预约则无法完成再次预约
        if (member != null) {
            Integer memberId = member.getId();
            int setmealId = Integer.parseInt((String) map.get("setmealId"));
            Order order = new Order(memberId, date, setmealId);
            List<Order> list = orderDao.findByCondition(order);
            if (list != null && list.size() > 0) {
                //已经完成预约不能重复预约
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        }

        //到这一步，说明能预约了
        //所以设置已预约人数加一
        orderSetting.setReservations(orderSetting.getReservations() + 1);
        orderSettingDao.editReservationsByOrderDate(orderSetting);

        // 4、检查当前用户是否为会员，如果是会员则直接完成预约，如果不是会员则自动完成注册并进行预约
        if (member == null) {
            member = new Member();
            member.setName((String) map.get("name"));
            member.setPhoneNumber(telephone);
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(new Date());
            memberDao.add(member);
        }
        // 5、预约成功，更新当日的已预约
        // 保存预约信息到预约表
        Order order = new Order(member.getId(),date,(String)map.get("orderType"),Order.ORDERSTATUS_NO,Integer.parseInt((String)map.get("setmealId")));
        orderDao.add(order);
        return new Result(true, MessageConstant.ORDER_SUCCESS, order.getId());
    }

    //根据预约ID查询预约相关信息（体检人姓名、预约日期、套餐名称、预约类型）
    @Override
    public Map findById(Integer id) throws Exception{
        Map map = orderDao.findById4Detail(id);
        if(map != null){
            //处理日期格式
            Date orderDate = (Date) map.get("orderDate");
            map.put("orderDate",DateUtils.parseDate2String(orderDate));
        }
        return map;
    }
}
