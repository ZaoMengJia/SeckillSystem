package com.zaomengjia.bankmanager.service.impl;

import com.zaomengjia.bankmanager.dto.WeixinUserDto;
import com.zaomengjia.bankmanager.service.UserService;
import com.zaomengjia.bankmanager.vo.WeixinUserDetailVO;
import com.zaomengjia.common.constant.ResultCode;
import com.zaomengjia.common.dao.AdminUserMapper;
import com.zaomengjia.common.dao.WeixinUserMapper;
import com.zaomengjia.bankmanager.dto.AdminUserDto;
import com.zaomengjia.common.entity.AdminUser;
import com.zaomengjia.common.entity.WeixinUser;
import com.zaomengjia.common.exception.AppException;
import com.zaomengjia.common.utils.RedisUtils;
import com.zaomengjia.common.vo.page.PageVO;
import com.zaomengjia.common.vo.user.AdminUserVO;
import com.zaomengjia.common.vo.user.WeixinUserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/7 23:59
 */
@Service
public class UserServiceImpl implements UserService {

    private final AdminUserMapper adminUserMapper;

    private final WeixinUserMapper weixinUserMapper;

    private final RedisUtils redisUtils;

    public UserServiceImpl(AdminUserMapper adminUserMapper, WeixinUserMapper weixinUserMapper, RedisUtils redisUtils) {
        this.adminUserMapper = adminUserMapper;
        this.weixinUserMapper = weixinUserMapper;
        this.redisUtils = redisUtils;
    }

    private AdminUserVO entityToVO(AdminUser adminUser) {
        if(adminUser == null) {
            return null;
        }
        AdminUserVO vo = new AdminUserVO();
        BeanUtils.copyProperties(adminUser, vo);
        return vo;
    }

    private WeixinUserDetailVO entityToVO(WeixinUser weixinUser) {
        if(weixinUser == null) {
            return null;
        }
        WeixinUserDetailVO vo = new WeixinUserDetailVO();
        BeanUtils.copyProperties(weixinUser, vo);
        return vo;
    }

    @Override
    public AdminUserVO getAdminUserById(String id) {
        AdminUser result = adminUserMapper.findById(id).orElse(null);
        return entityToVO(result);
    }

    @Override
    public PageVO<AdminUserVO> getAdminUserList(int pageNum, int pageSize) {
        Page<AdminUser> entityPage = adminUserMapper.findAll(PageRequest.of(pageNum - 1, pageSize));
        return new PageVO<>(entityPage.map(this::entityToVO));
    }

    @Override
    public PageVO<WeixinUserDetailVO> getWeixinUserList(int pageNum, int pageSize) {
        Page<WeixinUser> entityPage = weixinUserMapper.findAll(PageRequest.of(pageNum - 1, pageSize));
        return new PageVO<>(entityPage.map(this::entityToVO));
    }

    @Override
    public AdminUserVO getAdminUserByUsername(String username) {
        return entityToVO(adminUserMapper.findByUsername(username));
    }

    @Override
    public PageVO<WeixinUserDetailVO> searchWeixinUserByNickname(String keyword, int pageNum, int pageSize) {
        Page<WeixinUser> entityPage = weixinUserMapper.searchByNicknameContaining(keyword, PageRequest.of(pageNum - 1, pageSize));
        return new PageVO<>(entityPage.map(this::entityToVO));
    }

    @Override
    public PageVO<WeixinUserDetailVO> searchWeixinUserByRealName(String keyword, int pageNum, int pageSize) {
        Page<WeixinUser> entityPage = weixinUserMapper.searchByRealNameContaining(keyword, PageRequest.of(pageNum - 1, pageSize));
        return new PageVO<>(entityPage.map(this::entityToVO));
    }

    @Override
    public PageVO<AdminUserVO> searchAdminUserByUsername(String keyword, int pageNum, int pageSize) {
        Page<AdminUser> entityPage = adminUserMapper.searchByUsernameContaining(keyword, PageRequest.of(pageNum - 1, pageSize));
        return new PageVO<>(entityPage.map(this::entityToVO));
    }

    @Override
    public String insertAdminUser(AdminUserDto dto) {
        AdminUser entity = new AdminUser();
        BeanUtils.copyProperties(dto, entity);
        entity = adminUserMapper.save(entity);
        return entity.getId();
    }


    @Override
    public void updateAdminUser(String userId, AdminUserDto dto) {
        AdminUser entity = adminUserMapper.findById(userId).orElseThrow(() -> new AppException(ResultCode.NO_SUCH_USER));
        String password = entity.getPassword();
        BeanUtils.copyProperties(dto, entity);

        if(dto.getPassword() == null) {
            entity.setPassword(password);
        }

        entity.setId(userId);
        adminUserMapper.save(entity);
    }

    @Override
    public void updateWeixinUser(String userId, WeixinUserDto dto) {
        WeixinUser entity = weixinUserMapper.findById(userId).orElseThrow(() -> new AppException(ResultCode.NO_SUCH_USER));
        com.zaomengjia.common.utils.BeanUtils.copyIgnoringNull(dto, entity);
        entity.setId(userId);
        weixinUserMapper.save(entity);
    }

    @Override
    public void deleteAdminUser(String userId) {
        adminUserMapper.deleteById(userId);
    }

    @Override
    public void deleteWeixinUser(String userId) {
        weixinUserMapper.deleteById(userId);
    }

    @Override
    public void setWeixinUserAudit(String userId, int audit) {
        weixinUserMapper.setAudit(userId, audit);
        redisUtils.set("user-audit::" + userId, audit);
    }


}
