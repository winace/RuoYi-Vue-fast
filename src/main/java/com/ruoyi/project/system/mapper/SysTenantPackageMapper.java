package com.ruoyi.project.system.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.system.domain.SysSimplePackage;
import com.ruoyi.project.system.domain.SysTenant;
import com.ruoyi.project.system.domain.SysTenantPackage;

import java.util.List;

/**
 * 租户套餐Mapper接口
 *
 * @author zhaowang
 * @since 2024-11-22
 */
public interface SysTenantPackageMapper extends BaseMapper<SysTenantPackage> {
    /**
     * 查询租户套餐列表
     */
    default IPage<SysTenantPackage> selectSysTenantPackageList(Page<SysTenantPackage> page, SysTenantPackage sysTenantPackage) {
        Wrapper<SysTenantPackage> wrapper = new LambdaQueryWrapper<SysTenantPackage>()
                .like(StringUtils.isNotBlank(sysTenantPackage.getName()), SysTenantPackage::getName, sysTenantPackage.getName())
                .eq(sysTenantPackage.getStatus() != null, SysTenantPackage::getStatus, sysTenantPackage.getStatus());
        return selectPage(page, wrapper);
    }

    /**
     * 导出租户套餐列表
     */
    default List<SysTenantPackage> selectSysTenantPackageExport(SysTenantPackage sysTenantPackage) {
        Wrapper<SysTenantPackage> wrapper = new LambdaQueryWrapper<SysTenantPackage>()
                .like(StringUtils.isNotBlank(sysTenantPackage.getName()), SysTenantPackage::getName, sysTenantPackage.getName())
                .eq(sysTenantPackage.getStatus() != null, SysTenantPackage::getStatus, sysTenantPackage.getStatus());
        return selectList(wrapper);
    }

    /**
     * 查询租户套餐精简列表
     *
     * @return 租户套餐
     */
    List<SysSimplePackage> getSimpleList();

    /**
     * 批量删除租户套餐
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteSysTenantPackageByIds(Long[] ids);

    /**
     * 根据租户套餐获取当前套餐有多少租户使用
     *
     * @return 租户列表
     */
    List<SysTenant> getTenantByPackage(Long tenantpackageid);


    /**
     * 根据租户套餐获取当前套餐有多少正常租户正在使用
     *
     * @return 租户列表
     */
    Integer getActiveTenantByPackage(Long tenantpackageid);

}
