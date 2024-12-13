package com.ruoyi.project.external.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.project.external.common.domain.ExUser;
import com.ruoyi.project.external.common.vo.ExUserVo;
import org.apache.ibatis.annotations.Param;


/**
 * 应用用户 数据操作接口
 *
 * @author zhaowang
 * @since 2024-11-26 10:04:40
 */
public interface ExUserMapper extends BaseMapper<ExUser> {
    /**
     * 查询应用用户分页数据
     *
     * @param pageDto 分页参数
     * @param user  应用用户
     * @return 应用用户分页数据
     */
    IPage<ExUserVo> selectDataPage(Page<ExUser> pageDto, @Param("user") ExUser user);
}
