package com.ruoyi.framework.mybatis.util;


import com.ruoyi.common.utils.SecurityUtils;

/**
 * 多租户 Util
 *
 * @author 芋道源码
 */
public class TenantUtils {

    /**
     * 使用指定租户，执行对应的逻辑
     * <p>
     * 注意，如果当前是忽略租户的情况下，会被强制设置成不忽略租户
     * 当然，执行完成后，还是会恢复回去
     *
     * @param tenantId 租户编号
     * @param runnable 逻辑
     */
    public static void execute(Long tenantId, Runnable runnable) {
        Long oldTenantId = SecurityUtils.getTenantId();
        try {
            SecurityUtils.setTenantId(tenantId);
            // 执行逻辑
            runnable.run();
        } finally {
            SecurityUtils.setTenantId(oldTenantId);
        }
    }

}
