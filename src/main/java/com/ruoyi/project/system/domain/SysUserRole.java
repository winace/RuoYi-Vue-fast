package com.ruoyi.project.system.domain;

import com.ruoyi.framework.web.domain.TenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 用户和角色关联 sys_user_role
 * 
 * @author ruoyi
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class SysUserRole extends TenantEntity
{
    /** 用户ID */
    private Long userId;
    /** 角色ID */
    private Long roleId;
}
