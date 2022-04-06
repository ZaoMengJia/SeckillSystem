package com.zaomengjia.bankmanager.service.impl;

import com.zaomengjia.common.dao.UserMapper;
import com.zaomengjia.common.pojo.User;
import com.zaomengjia.bankmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Boolean userExist(String userName) {
        return userMapper.getByTypeAndUserName(1, userName) != null;
    }

    @Override
    public Map<String, Object> getAdminList(int pageIndex, int pageSize) {
        return getPageInfo(userMapper.getByType(1, PageRequest.of(pageIndex, pageSize)));
    }

    private Map<String, Object> getPageInfo(Page<User> page) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("records", page.toList());
        map.put("total", page.getTotalElements());
        return map;
    }

    @Override
    public Map<String, Object> getUserList(int pageIndex, int pageSize) {
        return getPageInfo(userMapper.getByType(1, PageRequest.of(pageIndex, pageSize)));
    }

    @Override
    public Map<String, Object> searchAdminList(String keyword, int pageIndex, int pageSize) {
        return null;
    }

    @Override
    public Map<String, Object> searchUserList(String keyword, int pageIndex, int pageSize) {
        return null;
    }

    @Override
    public User getAdminById(long uid) {
        return userMapper.getByTypeAndUid(1, uid);
    }

    @Override
    public User getUserById(long uid) {
        return userMapper.getByTypeAndUid(0, uid);
    }

    @Override
    public User getUserByName(String name) {
        return userMapper.getByTypeAndUserName(0, name);
    }

    @Override
    public User getAdminByName(String name) {
        return userMapper.getByTypeAndUserName(1, name);
    }

    @Override
    public void addUser(User user) {
        userMapper.save(user);
    }

    @Override
    public void deleteUser(long id) {
        userMapper.deleteById(id);
    }

    @Override
    public void updateUser(User user) {
        userMapper.save(user);
    }
}
