package com.ruoyi.project.external.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.project.external.common.domain.ExModuleFunc;

import java.util.List;

/**
 * 应用功能 数据操作接口
 *
 * @author zhaowang
 * @since 2024-11-26 10:04:40
 */
public interface ExModuleFuncMapper extends BaseMapper<ExModuleFunc> {
    /**
     * 查询应用功能列表
     *
     * @param moduleFunc 应用功能
     * @return 应用功能集合
     */
    List<ExModuleFunc> selectModuleFuncList(ExModuleFunc moduleFunc);
}
