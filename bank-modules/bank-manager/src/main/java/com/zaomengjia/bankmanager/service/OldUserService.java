package com.zaomengjia.bankmanager.service;

import com.zaomengjia.common.entity.User;

import java.util.Map;

@Deprecated
public interface OldUserService {
    /**
     * 判断用户是否存在
     * @param userName
     * @return
     */
    Boolean userExist(String userName);

    /**
     * 获取所有管理员与普通用户
     * @param pageIndex
     * @param pageSize
     * @return
     */
    Map<String,Object> getAdminList(int pageIndex,int pageSize);
    Map<String,Object> getUserList(int pageIndex,int pageSize);

    /**
     * 查询管理员以及用户
     * @param keyword
     * @param pageIndex
     * @param pageSize
     * @return
     */
    Map<String, Object> searchAdminList(String keyword, int pageIndex, int pageSize);
    Map<String, Object> searchUserList(String keyword, int pageIndex, int pageSize);

    /**
     * 通过id获取管理员与普通用户
     * @param uid
     * @return
     */
    User getAdminById(long uid);
    User getUserById(long uid);

    /**
     * 通过名字获取管理员与用户
     * @param name
     * @return
     */
    User getUserByName(String name);
    User getAdminByName(String name);

    /**
     * 添加管理员与用户
     * @param user
     */
    void addUser(User user);

    /**
     * 删除管理员、用户
     * @param id
     */
    void deleteUser(long id);

    /**
     * 更新管理员、用户
     * @param user
     */
    void updateUser(User user);
}
