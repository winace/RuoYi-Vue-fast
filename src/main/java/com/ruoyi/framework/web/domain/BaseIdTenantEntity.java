package com.ruoyi.framework.web.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Entity基类 + id
 *
 * @author zoneway
 * @since 2022-08-20 16:00:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class BaseIdTenantEntity extends BaseTenantEntity {
    /**
     * 主键id
     */
    @TableId
    private String id;


    public BaseIdTenantEntity(String id) {
        this.id = id;
    }
}
