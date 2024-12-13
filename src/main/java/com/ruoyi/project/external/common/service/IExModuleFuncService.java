package com.ruoyi.project.external.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.project.external.common.domain.ExModuleFunc;

import java.util.List;

/**
 * 应用功能 业务接口
 *
 * @author zhaowang
 * @since 2024-11-26 10:04:40
 */
public interface IExModuleFuncService extends IService<ExModuleFunc> {
    /**
     * 查询应用功能列表
     *
     * @param moduleFunc 应用功能
     * @return 应用功能集合
     */
    List<ExModuleFunc> queryList(ExModuleFunc moduleFunc);

    /**
     * 查询应用功能树
     *
     * @param moduleFunc 应用功能
     * @return 应用功能树
     */
    List<ExModuleFunc> queryTreeList(ExModuleFunc moduleFunc);

    /**
     * 新增保存应用功能
     *
     * @param moduleFunc 应用功能
     * @return 是否成功
     */
    boolean saveModuleFunc(ExModuleFunc moduleFunc);

    /**
     * 修改保存应用功能
     *
     * @param moduleFunc 应用功能
     * @return 是否成功
     */
    boolean updateModuleFunc(ExModuleFunc moduleFunc);
}
