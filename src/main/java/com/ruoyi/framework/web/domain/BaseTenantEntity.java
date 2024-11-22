package com.ruoyi.framework.web.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * Entity基类
 *
 * @author zhaowang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseTenantEntity extends BaseEntity {
    /**
     * 租户ID
     */
    private Long tenantId;
}
