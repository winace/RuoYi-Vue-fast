package com.ruoyi.framework.mybatis.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author zhaowang
 * @since 2024/11/22 08:49
 */
@Configuration
@ConfigurationProperties(prefix = "mybatis-plus.tenant")
@Data
public class TenantProperties {
    /**
     * 是否开启多租户
     */
    private Boolean enable;
    /**
     * 租户字段名
     */
    private String column;
    /**
     * 需要忽略的租户表名
     */
    private List<String> ignoreTables;
}
