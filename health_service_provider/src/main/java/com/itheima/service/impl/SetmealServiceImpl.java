package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.SetmealService;
import com.itheima.constant.RedisConstant;
import com.itheima.dao.SetmealDao;
import com.itheima.pojo.Setmeal;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealDao setmealDao;
    //使用JedisPool操作Redis服务
    @Autowired
    private JedisPool jedisPool;

    //分页模糊查询
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage,pageSize);
        Page<Setmeal> page = setmealDao.selectByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        setmealDao.add(setmeal);
        Integer setmealId = setmeal.getId();
        this.setSetmealAndCheckGroup(setmealId,checkgroupIds);
        //将图片名称保存到Redis
        this.savePic2Redis(setmeal.getImg());
    }

    @Override
    public void deleteById(Integer id) {
        long count = setmealDao.findCountBySetmealId(id);
        if(count > 0){
            //先删除关联表的关系，再删除setmeal表
            setmealDao.deleteAssocication(id);
        }
        //没有与其关联的数据就直接删除
        setmealDao.deleteById(id);
    }
    //编辑套餐信息，同时需要关联检查组
    @Override
    public void edit(Setmeal setmeal, Integer[] checkgroupIds) {
        //修改检查组基本信息，操作检查组t_setmeal表
        setmealDao.edit(setmeal);
        //清理当前套餐关联的检查组，操作中间关系表t_setmeal_checkgroup表
        setmealDao.deleteAssocication(setmeal.getId());
        //重新建立当前套餐和检查组的关联关系
        Integer setmealId = setmeal.getId();
        this.setSetmealAndCheckGroup(setmealId,checkgroupIds);
    }

    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }

    @Override
    public List<Integer> findCheckGroupIdsBySetmealId(Integer id) {
        return setmealDao.findCheckGroupIdsBySetmealId(id);

    }

    //根据套餐ID查询套餐详细信息
    @Override
    public Setmeal findById(Integer id) {
        return setmealDao.findById(id);
    }

    //查询套餐预约占比数据
    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return setmealDao.findSetmealCount();
    }


    //将图片名称保存到Redis
    private void savePic2Redis(String fileName){
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,fileName);
    }

    //建立套餐和检查组多对多关系
    public void setSetmealAndCheckGroup(Integer setmealId,Integer[] checkgroupIds){
        if(checkgroupIds != null && checkgroupIds.length > 0){
            for (Integer checkgroupId : checkgroupIds) {
                Map<String,Integer> map = new HashMap<>();
                map.put("setmeal_id",setmealId);
                map.put("checkgroup_id",checkgroupId);
                setmealDao.setSetmealAndCheckGroup(map);
            }
        }
    }

}
