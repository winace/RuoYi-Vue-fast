package com.ruoyi.project.system.domain;

import com.ruoyi.framework.web.domain.TenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色和部门关联 sys_role_dept
 * 
 * @author ruoyi
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysRoleDept extends TenantEntity
{
    /** 角色ID */
    private Long roleId;
    /** 部门ID */
    private Long deptId;
}
