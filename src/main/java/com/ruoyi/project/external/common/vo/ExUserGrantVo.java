package com.ruoyi.project.external.common.vo;

import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.project.external.common.domain.ExModuleFunc;
import com.ruoyi.project.external.common.domain.ExUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author zhaowang
 * @since 2024/12/12 16:54
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@ApiModel("应用用户授权信息")
public class ExUserGrantVo extends ExUser {
    @ApiModelProperty("当前用户对应应用可授权功能树")
    private List<ExModuleFunc> moduleFuncTree;

    public ExUserGrantVo(ExUser user, List<ExModuleFunc> treeList) {
        BeanUtils.copyProperties(user, this);
        moduleFuncTree = treeList;
    }
}
