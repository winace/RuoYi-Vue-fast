package com.ruoyi.framework.web.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Tree基类
 *
 * @author ruoyi
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TreeEntity extends BaseEntity {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("id")
    private Long id;
    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空")
    @Size(min = 0, max = 30, message = "名称长度不能超过30个字符")
    @ApiModelProperty("名称")
    @Excel(name = "名称")
    private String name;
    /**
     * 父菜单ID
     */
    @ApiModelProperty("父id")
    @Excel(name = "父id")
    private Long parentId;
    /**
     * 显示顺序
     */
    @NotNull(message = "显示顺序不能为空")
    @ApiModelProperty("显示顺序")
    private Integer orderNum;
    /**
     * 祖级id列表
     */
    private String ancestors;

}
