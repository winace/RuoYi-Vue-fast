package com.ruoyi.project.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.project.system.domain.SysTenant;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 租户管理 Mapper接口
 *
 * @author zhaowang
 * @since 2024-11-22
 */
public interface SysTenantMapper extends BaseMapper<SysTenant> {

    /**
     * 查询租户管理列表-分页
     *
     * @param sysTenant 租户管理
     * @return 租户管理集合
     */
    IPage<SysTenant> selectSysTenantList(Page<SysTenant> page, @Param("query") SysTenant sysTenant);

    /**
     * 查询租户管理列表
     *
     * @param sysTenant 租户管理
     * @return 租户管理集合
     */
    List<SysTenant> selectSysTenantList(@Param("query") SysTenant sysTenant);

    /**
     * 批量删除租户管理
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteSysTenantByIds(Long[] ids);
}
