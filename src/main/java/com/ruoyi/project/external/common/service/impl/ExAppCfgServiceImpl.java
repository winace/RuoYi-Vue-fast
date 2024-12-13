package com.ruoyi.project.external.common.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.ruoyi.project.external.common.mapper.ExAppCfgMapper;
import com.ruoyi.project.external.common.domain.ExAppCfg;
import com.ruoyi.project.external.common.service.IExAppCfgService;


/**
 * 应用配置 业务实现
 *
 * @author zhaowang
 * @since 2024-11-26 10:04:39
 */
@Service
public class ExAppCfgServiceImpl extends ServiceImpl<ExAppCfgMapper, ExAppCfg> implements IExAppCfgService {
    @Override
    public IPage<ExAppCfg> queryPage(Page<ExAppCfg> pageDto, ExAppCfg exAppCfg) {
        return baseMapper.selectDataPage(pageDto, exAppCfg);
    }

    @Override
    public boolean saveAppCfg(ExAppCfg appCfg) {
        ExAppCfg old = baseMapper.selectById(appCfg.getAppId());
        int res;
        if (old == null) {
            res = baseMapper.insert(appCfg);
        } else {
            res = baseMapper.updateById(appCfg);
        }
        return res > 0;
    }
}
