package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;

import com.itheima.UserService;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
用户查询
 */
//RestController中不只包含了Controller，而且还有将数据json化返回
@RestController
@RequestMapping("/user")
public class UserController {
    //查找服务
    @Reference
    private UserService userService;

    //新增服务项
    @PreAuthorize("hasAuthority('USER_ADD')")//权限
    @RequestMapping("/add")
    public Result add(@RequestBody User user){
        try{
            userService.add(user);
            //打印一下
            System.out.println(user);
        }catch (Exception e){
            e.printStackTrace();
            //服务调用失败
            return new Result(false, MessageConstant.ADD_USER_FAIL);
        }
        //服务调用成功
        return new Result(true,MessageConstant.ADD_USER_SUCCESS);
    }

    //检查项分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = userService.selectByCondition(queryPageBean);
        //打印一下
        System.out.println(pageResult);
        return pageResult;
    }

    //删除检查项
    @PreAuthorize("hasAuthority('USER_DELETE')")//权限
    @RequestMapping("/delete")
    public Result delete(Integer id){
        try{
            userService.deleteById(id);
            //打印一下
            System.out.println(id);
        }catch (Exception e){
            e.printStackTrace();
            //服务调用失败
            return new Result(false, MessageConstant.DELETE_USER_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_USER_SUCCESS);
    }
    //编辑窗口检查项
    @PreAuthorize("hasAuthority('USER_EDIT')")//权限
    @RequestMapping("/edit")
    public Result edit(@RequestBody User user){
        try{
            userService.edit(user);
        }catch (Exception e){
            e.printStackTrace();
            //服务调用失败
            return new Result(false, MessageConstant.EDIT_USER_FAIL);
        }
        return  new Result(true, MessageConstant.EDIT_USER_SUCCESS);
    }
    //根据id回显编辑数据
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try{
            User user = userService.findById(id);
            //打印一下
            System.out.println(user);
            return  new Result(true, MessageConstant.QUERY_USER_SUCCESS,user);
        }catch (Exception e){
            e.printStackTrace();
            //服务调用失败
            return new Result(false, MessageConstant.QUERY_USER_FAIL);
        }
    }

    @RequestMapping("/findAll")
    public Result findAll(){
        try{
            List<User> userList = userService.findAll();
            //打印一下
            System.out.println(userList);
            return new Result(true,MessageConstant.QUERY_USER_SUCCESS,userList);
        }catch(Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_USER_FAIL);
        }
    }

}
