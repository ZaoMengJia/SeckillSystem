package com.zaomengjia.bankmanager.service;

import com.zaomengjia.common.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface UserService {
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
     * 通过id获取管理员与普通用户
     * @param uid
     * @return
     */
    User getAdminById(@Param("uid") long uid);
    User getUserById(@Param("uid") long uid);

    /**
     * 通过名字获取管理员与用户
     * @param name
     * @return
     */
    User getUserByName(@Param("name") String name);
    User getAdminByName(@Param("name") String name);

    /**
     * 添加管理员与用户
     * @param user
     * @return
     */
    int addUser(User user);

    /**
     * 删除管理员、用户
     * @param id
     * @return
     */
    int deleteUser(int id);

    /**
     * 更新管理员、用户
     * @param user
     * @return
     */
    int updateUser(User user);
}
