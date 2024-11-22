package com.ruoyi.framework.mybatis.handler;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.mybatis.properties.TenantProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;

/**
 * 租户处理器
 *
 * @author zhaowang
 */
@Slf4j
@AllArgsConstructor
public class MyTenantHandler implements TenantLineHandler {

    private final TenantProperties properties;

    /**
     * 获取租户ID
     *
     * @return 租户ID
     */
    @Override
    public Expression getTenantId() {
        try {
            SecurityUtils.getLoginUser();
        } catch (Exception e) {
            return new LongValue(9999);
        }
        return new LongValue(SecurityUtils.getTenantId());
    }

    /**
     * 获取租户字段名称
     *
     * @return 租户字段名称
     */
    @Override
    public String getTenantIdColumn() {
        return properties.getColumn();
    }

    /**
     * 过滤租户表
     *
     * @param tableName 表名
     * @return 是否忽略, true:表示忽略，false:需要解析并拼接多租户条件
     */
    @Override
    public boolean ignoreTable(String tableName) {
        return properties.getIgnoreTables().contains(tableName);
    }
}
