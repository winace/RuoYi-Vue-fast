package com.ruoyi.project.system.domain;

import com.ruoyi.framework.web.domain.TenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色和菜单关联 sys_role_menu
 * 
 * @author ruoyi
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysRoleMenu extends TenantEntity
{
    /** 角色ID */
    private Long roleId;
    /** 菜单ID */
    private Long menuId;
}
