package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.CheckItemService;
import com.itheima.constant.MessageConstant;
import com.itheima.pojo.CheckItem;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
检查项查询
 */
//RestController中不只包含了Controller，而且还有将数据json化返回
@RestController
@RequestMapping("/checkitem")
public class CheckItemController {
    //查找服务
    @Reference
    private CheckItemService checkItemService;

    //新增服务项
    @PreAuthorize("hasAuthority('CHECKITEM_ADD')")//权限
    @RequestMapping("/add")
    public Result add(@RequestBody CheckItem checkItem){
        try{
            checkItemService.add(checkItem);
            //打印一下
            System.out.println(checkItem);
        }catch (Exception e){
            e.printStackTrace();
            //服务调用失败
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
        //服务调用成功
        return new Result(true,MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    //检查项分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = checkItemService.selectByCondition(queryPageBean);
        //打印一下
        System.out.println(pageResult);
        return pageResult;
    }

    //删除检查项
    @PreAuthorize("hasAuthority('CHECKITEM_DELETE')")//权限
    @RequestMapping("/delete")
    public Result delete(Integer id){
        try{
            checkItemService.deleteById(id);
            //打印一下
            System.out.println(id);
        }catch (Exception e){
            e.printStackTrace();
            //服务调用失败
            return new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        return  new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }
    //编辑窗口检查项
    @PreAuthorize("hasAuthority('CHECKITEM_EDIT')")//权限
    @RequestMapping("/edit")
    public Result edit(@RequestBody CheckItem checkItem){
        try{
            checkItemService.edit(checkItem);
        }catch (Exception e){
            e.printStackTrace();
            //服务调用失败
            return new Result(false, MessageConstant.EDIT_CHECKITEM_FAIL);
        }
        return  new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }
    //根据id回显编辑数据
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try{
            CheckItem checkItem = checkItemService.findById(id);
            //打印一下
            System.out.println(checkItem);
            return  new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);
        }catch (Exception e){
            e.printStackTrace();
            //服务调用失败
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    @RequestMapping("/findAll")
    public Result findAll(){
        try{
            List<CheckItem> items = checkItemService.findAll();
            //打印一下
            System.out.println(items);
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,items);
        }catch(Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

}
