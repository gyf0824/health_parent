package com.itheima.service.impl;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.CheckItemService;
import com.itheima.dao.CheckItemDao;
import com.itheima.pojo.CheckItem;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 检查项服务
 */
@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {
    //注入dao
    @Autowired
    private CheckItemDao checkItemDao;
    //新增
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    //检查项分页查询
    @Override
    public PageResult selectByCondition(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();//查询条件
        //完成分页查询，基于mybatis框架提供的分页助手插件完成
        PageHelper.startPage(currentPage,pageSize);
        //select * from t_checkitem limit 0,10
        Page<CheckItem> page = checkItemDao.selectByCondition(queryString);
        System.out.println(page);
        long total = page.getTotal();
        List<CheckItem> rows = page.getResult();
        int pageNum = page.getPageNum();
        return new PageResult(total,rows);
    }


    //根据ID删除检查项

    @Override
    public void deleteById(Integer id) {
        //判断当前检查项是否已经关联到检查组
        long count = checkItemDao.findCountByCheckItemId(id);
        if(count > 0){
            //先删除关联表的关系，再删除checkitem表
            checkItemDao.deleteAssocication(id);
            //当前检查项已经被关联到检查组，不允许删除
//            new RuntimeException();
        }
        //没有与其关联的数据就直接删除
        checkItemDao.deleteById(id);
    }

    //修改检查项数据
    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);
    }

    //根据ID回显编辑检查窗口
    @Override
    public CheckItem findById(Integer id) {
        return checkItemDao.findById(id);
    }

    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }
}