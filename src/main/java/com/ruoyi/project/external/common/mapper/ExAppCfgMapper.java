package com.ruoyi.project.external.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.project.external.common.domain.ExAppCfg;
import org.apache.ibatis.annotations.Param;


/**
 * 应用配置 数据操作接口
 * 
 * @author zhaowang
 * @since  2024-11-26 10:04:39
 */
public interface ExAppCfgMapper extends BaseMapper<ExAppCfg> {
    /**
     * 查询应用配置分页数据
     *
     * @param pageDto 分页参数
     * @param appCfg 应用配置
     * @return 应用配置分页数据
     */
    IPage<ExAppCfg> selectDataPage(Page<ExAppCfg> pageDto, @Param("appCfg") ExAppCfg appCfg);
}
