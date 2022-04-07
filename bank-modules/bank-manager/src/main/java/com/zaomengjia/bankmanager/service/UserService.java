package com.zaomengjia.bankmanager.service;

import com.zaomengjia.bankmanager.dto.AdminUserDto;
import com.zaomengjia.bankmanager.dto.WeixinUserDto;
import com.zaomengjia.common.vo.page.PageVO;
import com.zaomengjia.common.vo.user.AdminUserVO;
import com.zaomengjia.common.vo.user.WeixinUserVO;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/7 23:59
 */
public interface UserService {
    AdminUserVO getAdminUserById(String id);

    PageVO<AdminUserVO> getAdminUserList(int pageNum, int pageSize);

    PageVO<WeixinUserVO> getWeixinUserList(int pageNum, int pageSize);

    AdminUserVO getAdminUserByUsername(String username);

    PageVO<WeixinUserVO> searchWeixinUserByNickname(String nickname, int pageNum, int pageSize);

    PageVO<WeixinUserVO> searchWeixinUserByRealName(String keyword, int pageNum, int pageSize);

    PageVO<AdminUserVO> searchAdminUserByUsername(String keyword, int pageNum, int pageSize);

    String insertAdminUser(AdminUserDto dto);

    void updateAdminUser(String userId, AdminUserDto dto);

    void updateWeixinUser(String userId, WeixinUserDto dto);

    void deleteAdminUser(String userId);

    void deleteWeixinUser(String userId);
}
