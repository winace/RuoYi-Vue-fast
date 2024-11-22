package com.ruoyi.project.system.domain;

import com.ruoyi.framework.web.domain.TenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 用户和岗位关联 sys_user_post
 * 
 * @author ruoyi
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class SysUserPost extends TenantEntity
{
    /** 用户ID */
    private Long userId;
    /** 岗位ID */
    private Long postId;
}
