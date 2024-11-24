package com.ruoyi.project.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.project.system.domain.SysTenant;

import java.util.List;

/**
 * 租户管理Service接口
 *
 * @author zhaowang
 * @since 2024-11-22
 */
public interface ISysTenantService {
    /**
     * 查询租户管理
     *
     * @param id 租户管理主键
     * @return 租户管理
     */
    SysTenant selectSysTenantById(Long id);

    /**
     * 查询租户管理列表-分页
     *
     * @param sysTenant 租户管理
     * @return 租户管理集合
     */
    IPage<SysTenant> selectSysTenantPage(SysTenant sysTenant);

    /**
     * 查询租户管理列表
     *
     * @param sysTenant 租户管理
     * @return 租户管理集合
     */
    List<SysTenant> selectSysTenantList(SysTenant sysTenant);

    /**
     * 新增租户管理
     *
     * @param sysTenant 租户管理
     * @return 结果
     */
    boolean insertSysTenant(SysTenant sysTenant);

    /**
     * 修改租户管理
     *
     * @param sysTenant 租户管理
     * @return 结果
     */
    int updateSysTenant(SysTenant sysTenant);

    /**
     * 重置租户管理员密码
     *
     * @param id 租户id
     * @return 是否成功
     */
    boolean resetTenantUserPwd(String id);

    /**
     * 批量删除租户管理
     *
     * @param ids 需要删除的租户管理主键集合
     * @return 结果
     */
    int deleteSysTenantByIds(Long[] ids);

    /**
     * 删除租户管理信息
     *
     * @param id 租户管理主键
     * @return 结果
     */
    int deleteSysTenantById(Long id);
}
