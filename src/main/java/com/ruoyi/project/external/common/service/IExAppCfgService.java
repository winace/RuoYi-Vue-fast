package com.ruoyi.project.external.common.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.project.external.common.domain.ExAppCfg;


/**
 * 应用配置 业务接口
 *
 * @author zhaowang
 * @since 2024-11-26 10:04:39
 */
public interface IExAppCfgService extends IService<ExAppCfg> {
    /**
     * 查询应用配置分页列表
     *
     * @param pageDto  分页参数
     * @param exAppCfg 应用配置
     * @return 应用配置集合
     */
    IPage<ExAppCfg> queryPage(Page<ExAppCfg> pageDto, ExAppCfg exAppCfg);

    /**
     * 新增/修改的保存
     *
     * @param appCfg 应用配置
     * @return 是否成功
     */
    boolean saveAppCfg(ExAppCfg appCfg);
}
