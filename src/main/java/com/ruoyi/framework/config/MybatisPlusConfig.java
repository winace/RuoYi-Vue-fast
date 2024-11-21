package com.ruoyi.framework.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.ruoyi.framework.config.handler.MyMetaObjectHandler;
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
public class MybatisPlusConfig {

    /*
     * 性能分析拦截器，不建议生产使用
     */
    /*@Bean
    public PerformanceInterceptor performanceInterceptor(){
        return new PerformanceInterceptor();
    }*/

    /**
     * 分页插件
     *
     * @return 插件集合
     */
    @Bean
    public MybatisPlusInterceptor paginationInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }

    /**
     * 预 SQL 自动填充
     *
     * @return 自动填充工具
     */
    @Bean
    public MyMetaObjectHandler myMetaObjectHandler() {
        return new MyMetaObjectHandler();
    }

}
