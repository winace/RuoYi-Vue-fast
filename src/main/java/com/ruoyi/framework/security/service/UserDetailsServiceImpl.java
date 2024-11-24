package com.ruoyi.framework.security.service;

import com.baomidou.mybatisplus.core.plugins.IgnoreStrategy;
import com.baomidou.mybatisplus.core.plugins.InterceptorIgnoreHelper;
import com.ruoyi.common.enums.UserStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 用户验证处理
 *
 * @author ruoyi
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private ISysUserService userService;
    
    @Autowired
    private SysPasswordService passwordService;

    @Autowired
    private SysPermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username)
    {
        try {
            //此时还没有租户，所以全部关闭租户功能
            InterceptorIgnoreHelper.handle(IgnoreStrategy.builder().tenantLine(true).build());
            SysUser user = userService.selectUserByUserName(username);
            if (StringUtils.isNull(user))
            {
                log.info("登录用户：{} 不存在.", username);
                throw new ServiceException(MessageUtils.message("user.not.exists"));
            }
            //线程塞入租户ID
            SecurityUtils.setTenantId(user.getTenantId());
            if (UserStatus.DELETED.getCode().equals(user.getDelFlag()))
            {
                log.info("登录用户：{} 已被删除.", username);
                throw new ServiceException(MessageUtils.message("user.password.delete"));
            }
            else if (UserStatus.DISABLE.getCode().equals(user.getStatus()))
            {
                log.info("登录用户：{} 已被停用.", username);
                throw new ServiceException(MessageUtils.message("user.blocked"));
            } else if (user.getTenantStatus() != null && UserStatus.DISABLE.getCode().equals(user.getTenantStatus().toString())) {//先查询是否被停用了租户
                log.info("登录用户：{} 租户已经被停用.", username);
                throw new ServiceException(MessageUtils.message("tenant.status.error"));
            } else if (user.getTenantEndDate() != null && user.getTenantEndDate().compareTo(new Date()) < 0) {
                log.info("登录用户：{} 租户已超过租赁日期.", username);
                throw new ServiceException(MessageUtils.message("tenant.date.overdue"));
            }

            passwordService.validate(user);

            return createLoginUser(user);
        } finally {
            //恢复租户功能
            InterceptorIgnoreHelper.clearIgnoreStrategy();
        }
    }

    public UserDetails createLoginUser(SysUser user)
    {
        return new LoginUser(user.getUserId(), user.getDeptId(), user, permissionService.getMenuPermission(user));
    }
}
