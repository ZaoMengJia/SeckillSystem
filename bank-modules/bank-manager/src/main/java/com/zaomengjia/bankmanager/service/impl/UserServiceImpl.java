package com.zaomengjia.bankmanager.service.impl;

import com.zaomengjia.bankmanager.dto.WeixinUserDto;
import com.zaomengjia.bankmanager.service.UserService;
import com.zaomengjia.common.constant.ResultCode;
import com.zaomengjia.common.dao.AdminUserMapper;
import com.zaomengjia.common.dao.WeixinUserMapper;
import com.zaomengjia.bankmanager.dto.AdminUserDto;
import com.zaomengjia.common.entity.AdminUser;
import com.zaomengjia.common.entity.WeixinUser;
import com.zaomengjia.common.exception.AppException;
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

    public UserServiceImpl(AdminUserMapper adminUserMapper, WeixinUserMapper weixinUserMapper) {
        this.adminUserMapper = adminUserMapper;
        this.weixinUserMapper = weixinUserMapper;
    }

    private AdminUserVO entityToVO(AdminUser adminUser) {
        if(adminUser == null) {
            return null;
        }
        AdminUserVO vo = new AdminUserVO();
        BeanUtils.copyProperties(adminUser, vo);
        return vo;
    }

    private WeixinUserVO entityToVO(WeixinUser weixinUser) {
        if(weixinUser == null) {
            return null;
        }
        WeixinUserVO vo = new WeixinUserVO();
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
        Page<AdminUser> entityPage = adminUserMapper.findAll(PageRequest.of(pageNum, pageSize));
        return new PageVO<>(entityPage.map(this::entityToVO));
    }

    @Override
    public PageVO<WeixinUserVO> getWeixinUserList(int pageNum, int pageSize) {
        Page<WeixinUser> entityPage = weixinUserMapper.findAll(PageRequest.of(pageNum, pageSize));
        return new PageVO<>(entityPage.map(this::entityToVO));
    }

    @Override
    public AdminUserVO getAdminUserByUsername(String username) {
        return entityToVO(adminUserMapper.findByUsername(username));
    }

    @Override
    public PageVO<WeixinUserVO> searchWeixinUserByNickname(String keyword, int pageNum, int pageSize) {
        Page<WeixinUser> entityPage = weixinUserMapper.searchByNicknameLike(keyword, PageRequest.of(pageNum, pageSize));
        return new PageVO<>(entityPage.map(this::entityToVO));
    }

    @Override
    public PageVO<WeixinUserVO> searchWeixinUserByRealName(String keyword, int pageNum, int pageSize) {
        Page<WeixinUser> entityPage = weixinUserMapper.searchByRealNameLike(keyword, PageRequest.of(pageNum, pageSize));
        return new PageVO<>(entityPage.map(this::entityToVO));
    }

    @Override
    public PageVO<AdminUserVO> searchAdminUserByUsername(String keyword, int pageNum, int pageSize) {
        Page<AdminUser> entityPage = adminUserMapper.searchByUsernameLike(keyword, PageRequest.of(pageNum, pageSize));
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
        BeanUtils.copyProperties(dto, entity);

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



}
