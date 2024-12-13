package com.ruoyi.framework.web.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Tree基类
 *
 * @author ruoyi
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TreeTenantEntity extends TreeEntity {
    /**
     * 租户ID
     */
    private Long tenantId;
}
