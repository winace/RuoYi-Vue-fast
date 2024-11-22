package com.ruoyi.framework.mybatis.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.ruoyi.framework.mybatis.handler.MyMetaObjectHandler;
import com.ruoyi.framework.mybatis.handler.MyTenantHandler;
import com.ruoyi.framework.mybatis.plugins.MyPaginationInnerInterceptor;
import com.ruoyi.framework.mybatis.properties.TenantProperties;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author zhaowang
 * @version 1.0
 * @since 2022/05/16 17:12
 */
@Configuration
@MapperScan(basePackages = {"com.zoneway.**.mapper"})
@EnableTransactionManagement
@RequiredArgsConstructor
public class MybatisPlusConfig {
    private final TenantProperties tenantProperties;

    /*
     * 性能分析拦截器，不建议生产使用
     */
    /*@Bean
    public PerformanceInterceptor performanceInterceptor(){
        return new PerformanceInterceptor();
    }*/

    /**
     * 预 SQL 自动填充
     *
     * @return 自动填充工具
     */
    @Bean
    public MyMetaObjectHandler myMetaObjectHandler() {
        return new MyMetaObjectHandler();
    }

    /**
     * 插件集合
     *
     * @return 插件集合
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 租户插件
        interceptor.addInnerInterceptor(tenantLineInnerInterceptor());
        // 租户插件必须在分页插件之前
        // 分页插件
        interceptor.addInnerInterceptor(paginationInnerInterceptor());
        // 乐观锁插件
        interceptor.addInnerInterceptor(optimisticLockerInnerInterceptor());
        // 阻断插件
        interceptor.addInnerInterceptor(blockAttackInnerInterceptor());
        return interceptor;
    }

    /**
     * 多租户插件
     *
     * @return 多租户插件
     */
    private TenantLineInnerInterceptor tenantLineInnerInterceptor() {
        return new TenantLineInnerInterceptor(new MyTenantHandler(tenantProperties));
    }

    /**
     * <a href="https://baomidou.com/plugins/pagination">分页插件</a>
     */
    public MyPaginationInnerInterceptor paginationInnerInterceptor() {
        MyPaginationInnerInterceptor paginationInnerInterceptor = new MyPaginationInnerInterceptor();
        // 设置数据库类型为mysql
        paginationInnerInterceptor.setDbType(DbType.MYSQL);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        paginationInnerInterceptor.setMaxLimit(-1L);
        return paginationInnerInterceptor;
    }

    /**
     * <a href="https://baomidou.com/plugins/optimistic-locker">乐观锁插件</a>
     */
    public OptimisticLockerInnerInterceptor optimisticLockerInnerInterceptor() {
        return new OptimisticLockerInnerInterceptor();
    }

    /**
     * <a href="https://baomidou.com/plugins/block-attack">安全阻断插件</a>，如果是对全表的删除或更新操作，就会终止该操作
     */
    public BlockAttackInnerInterceptor blockAttackInnerInterceptor() {
        return new BlockAttackInnerInterceptor();
    }

}
