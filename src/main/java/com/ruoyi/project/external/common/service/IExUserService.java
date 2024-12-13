package com.ruoyi.project.external.common.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.project.external.common.domain.ExUser;
import com.ruoyi.project.external.common.vo.ExUserVo;


/**
 * 应用用户 业务接口
 *
 * @author zhaowang
 * @since 2024-11-26 10:04:40
 */
public interface IExUserService extends IService<ExUser> {
    /**
     * 查询应用用户分页列表
     *
     * @param pageDto 分页参数
     * @param user    应用用户
     * @return 应用用户集合
     */
    IPage<ExUserVo> queryPage(Page<ExUser> pageDto, ExUser user);

    /**
     * 保存应用用户
     *
     * @param user 应用用户
     * @return 是否保存成功
     */
    boolean saveUser(ExUser user);
}
