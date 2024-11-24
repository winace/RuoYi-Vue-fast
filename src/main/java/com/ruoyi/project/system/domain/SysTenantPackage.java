package com.ruoyi.project.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 租户套餐对象 sys_tenant_package
 *
 * @author zhaowang
 * @since 2024-11-22
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysTenantPackage extends BaseEntity
{
    /** 套餐编号 */
    @Excel(name = "套餐编号")
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 套餐名 */
    @Excel(name = "套餐名")
    private String name;
    /** 关联的菜单编号 */
    private String menuIds;
    /** 删除标志（0代表存在 1代表删除） */
    private String delFlag;
    /** 部门状态:0正常,1停用 */
    private Integer status;

}
