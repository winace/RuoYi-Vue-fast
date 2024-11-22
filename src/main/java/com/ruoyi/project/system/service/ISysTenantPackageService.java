package com.ruoyi.project.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.project.system.domain.SysSimplePackage;
import com.ruoyi.project.system.domain.SysTenantPackage;

import java.util.List;

/**
 * 租户套餐Service接口
 *
 * @author zhaowang
 * @since 2024-11-22
 */
public interface ISysTenantPackageService {
    /**
     * 查询租户套餐
     *
     * @param id 租户套餐主键
     * @return 租户套餐
     */
    SysTenantPackage selectSysTenantPackageById(Long id);

    /**
     * 查询租户套餐
     *
     * @return 租户套餐
     */
    List<SysSimplePackage> getSimpleList();

    /**
     * 查询租户套餐列表
     *
     * @param sysTenantPackage 租户套餐
     * @return 租户套餐集合
     */
    IPage<SysTenantPackage> selectSysTenantPackageList(SysTenantPackage sysTenantPackage);

    /**
     * 新增租户套餐
     *
     * @param sysTenantPackage 租户套餐
     * @return 结果
     */
    int insertSysTenantPackage(SysTenantPackage sysTenantPackage);

    /**
     * 修改租户套餐
     *
     * @param sysTenantPackage 租户套餐
     * @return 结果
     */
    int updateSysTenantPackage(SysTenantPackage sysTenantPackage);

    /**
     * 批量删除租户套餐
     *
     * @param ids 需要删除的租户套餐主键集合
     * @return 结果
     */
    int deleteSysTenantPackageByIds(Long[] ids);


    /**
     * 查询导出
     *
     * @return 租户套餐
     */
    List<SysTenantPackage> selectSysTenantPackageExport(SysTenantPackage sysTenantPackage);
}
