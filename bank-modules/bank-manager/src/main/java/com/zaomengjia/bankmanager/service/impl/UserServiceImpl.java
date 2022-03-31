package com.zaomengjia.bankmanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zaomengjia.common.dao.UserMapper;
import com.zaomengjia.common.pojo.User;
import com.zaomengjia.bankmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Boolean userExist(String userName) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_admin",1).eq("user_name",userName);
        return userMapper.selectOne(queryWrapper)!=null;
    }

    @Override
    public Map<String, Object> getAdminList(int pageIndex, int pageSize) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_admin", 1);
        return getPageInfo(pageIndex, pageSize, queryWrapper);
    }

    private Map<String, Object> getPageInfo(int pageIndex, int pageSize, QueryWrapper<User> queryWrapper) {
        Page<User> page = new Page<>(pageIndex, pageSize);
        Map<String, Object> map = new HashMap<>(5);
        userMapper.selectPage(page, queryWrapper);
        map.put("records", page.getRecords());
        map.put("total", page.getTotal());
        return map;
    }

    @Override
    public Map<String, Object> getUserList(int pageIndex, int pageSize) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_admin", 0);
        return getPageInfo(pageIndex, pageSize, queryWrapper);
    }

    @Override
    public User getAdminById(long uid) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_admin", 1).eq("uid",uid);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public User getUserById(long uid) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_admin", 0).eq("uid",uid);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public User getUserByName(String name) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_admin", 0).eq("user_name",name);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public User getAdminByName(String name) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_admin", 1).eq("user_name",name);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public int addUser(User user) {
        return userMapper.insert(user);
    }

    @Override
    public int deleteUser(int id) {
        return userMapper.deleteById(id);
    }

    @Override
    public int updateUser(User user) {
        return userMapper.updateById(user);
    }
}
