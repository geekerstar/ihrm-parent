package com.geekerstar.system.shiro.realm;

import com.geekerstar.common.shiro.realm.IhrmRealm;
import com.geekerstar.domain.system.Permission;
import com.geekerstar.domain.system.User;
import com.geekerstar.domain.system.response.ProfileResult;
import com.geekerstar.system.service.PermissionService;
import com.geekerstar.system.service.UserService;
import org.apache.shiro.authc.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRealm extends IhrmRealm {

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;

    //认证方法
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //1.获取用户的手机号和密码
        UsernamePasswordToken upToken = (UsernamePasswordToken) authenticationToken;
        String mobile = upToken.getUsername();
        String password = new String(upToken.getPassword());
        //2.根据手机号查询用户
        User user = userService.findByMobile(mobile);
        //3.判断用户是否存在，用户密码是否和输入密码一致
        if (user != null && user.getPassword().equals(password)) {
            //4.构造安全数据并返回（安全数据：用户基本数据，权限信息 profileResult）
            ProfileResult result = null;
            if ("user".equals(user.getLevel())) {
                result = new ProfileResult(user);
            } else {
                Map map = new HashMap();
                if ("coAdmin".equals(user.getLevel())) {
                    map.put("enVisible", "1");
                }
                List<Permission> list = permissionService.findAll(map);
                result = new ProfileResult(user, list);
            }
            //构造方法：安全数据，密码，realm域名
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(result, user.getPassword(), this.getName());
            return info;
        }
        //返回null，会抛出异常，标识用户名和密码不匹配
        return null;
    }
}
