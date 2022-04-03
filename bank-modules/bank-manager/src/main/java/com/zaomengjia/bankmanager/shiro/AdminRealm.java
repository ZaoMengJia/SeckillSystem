package com.zaomengjia.bankmanager.shiro;

import cn.hutool.crypto.SecureUtil;
import com.zaomengjia.bankmanager.service.UserService;
import com.zaomengjia.common.pojo.User;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

public class AdminRealm extends AuthorizingRealm {
    @Autowired
    UserService adminService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("执行了授权");
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("执行了认证");
        UsernamePasswordToken userToken = (UsernamePasswordToken) authenticationToken;
        userToken.setPassword(SecureUtil.md5(String.valueOf(userToken.getPassword())).toCharArray());
        User admin = adminService.getAdminByName(userToken.getUsername());

        if(admin==null){
            System.out.println("认证失败");
            return null;
        }
        return new SimpleAuthenticationInfo("", admin.getPassword(),"");
    }
}
