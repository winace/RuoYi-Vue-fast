package com.ruoyi.project.external.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.enums.SpecialCharacter;
import com.ruoyi.common.utils.TreeUtil;
import com.ruoyi.project.external.common.domain.ExModuleFunc;
import com.ruoyi.project.external.common.mapper.ExModuleFuncMapper;
import com.ruoyi.project.external.common.service.IExModuleFuncService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 应用功能 业务实现
 *
 * @author zhaowang
 * @since 2024-11-26 10:04:40
 */
@Service
public class ExModuleFuncServiceImpl extends ServiceImpl<ExModuleFuncMapper, ExModuleFunc> implements IExModuleFuncService {
    @Override
    public List<ExModuleFunc> queryList(ExModuleFunc moduleFunc) {
        return baseMapper.selectModuleFuncList(moduleFunc);
    }

    @Override
    public List<ExModuleFunc> queryTreeList(ExModuleFunc moduleFunc) {
        List<ExModuleFunc> list = baseMapper.selectModuleFuncList(moduleFunc);
        return TreeUtil.toObjTree(list);
    }

    @Override
    public boolean saveModuleFunc(ExModuleFunc moduleFunc) {
        setAncestors(moduleFunc);
        return save(moduleFunc);
    }

    private void setAncestors(ExModuleFunc moduleFunc) {
        ExModuleFunc parent = baseMapper.selectById(moduleFunc.getParentId());
        String newAncestors;
        if (parent != null) {
            newAncestors = parent.getAncestors() + SpecialCharacter.COMMA.getValue() + parent.getId();
        } else {
            newAncestors = moduleFunc.getParentId().toString();
        }
        moduleFunc.setAncestors(newAncestors);
        Long id = moduleFunc.getId();
        if (id != null) {
            ExModuleFunc old = baseMapper.selectById(id);
            if (old.getParentId().longValue() != moduleFunc.getParentId()) {
                Wrapper<ExModuleFunc> wrapper = Wrappers.<ExModuleFunc>query().apply("find_in_set({0}, ancestors)", id);
                List<ExModuleFunc> children = baseMapper.selectList(wrapper);
                children.forEach(c -> c.setAncestors(c.getAncestors().replaceFirst(old.getAncestors(), newAncestors)));
                updateBatchById(children);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateModuleFunc(ExModuleFunc moduleFunc) {
        setAncestors(moduleFunc);
        return updateById(moduleFunc);
    }
}
