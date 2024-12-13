package com.ruoyi.project.external.common.vo;

import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.project.external.common.domain.ExUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhaowang
 * @since 2024/12/13 11:56
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("外部应用用户Vo")
public class ExUserVo extends ExUser {
    @ApiModelProperty("所属应用名称")
    @Excel(name = "所属应用名称")
    private String appName;
    @ApiModelProperty("已授权功能名称")
    @Excel(name = "已授权功能名称")
    private String funcNames;
}
