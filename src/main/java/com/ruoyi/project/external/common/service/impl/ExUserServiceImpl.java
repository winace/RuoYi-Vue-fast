package com.ruoyi.project.external.common.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.project.external.common.vo.ExUserVo;
import org.springframework.stereotype.Service;
import com.ruoyi.project.external.common.mapper.ExUserMapper;
import com.ruoyi.project.external.common.domain.ExUser;
import com.ruoyi.project.external.common.service.IExUserService;


/**
 * 应用用户 业务实现
 *
 * @author zhaowang
 * @since 2024-11-26 10:04:40
 */
@Service
public class ExUserServiceImpl extends ServiceImpl<ExUserMapper, ExUser> implements IExUserService {
    @Override
    public IPage<ExUserVo> queryPage(Page<ExUser> pageDto, ExUser user) {
        return baseMapper.selectDataPage(pageDto, user);
    }

    @Override
    public boolean saveUser(ExUser user) {
        ExUser old = baseMapper.selectById(user.getOpenid());
        int res;
        if (old == null) {
            res = baseMapper.insert(user);
        } else {
            res = baseMapper.updateById(user);
        }
        return res > 0;
    }
}
