package com.ruoyi.project.external.common.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ruoyi.common.enums.SpecialCharacter;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.TreeNode;
import com.ruoyi.framework.web.domain.TreeTenantEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.ObjectUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 外部应用功能模块 实体对象 ex_module_func
 *
 * @author zhaowang
 * @since 2024-11-25 23:38:24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("外部应用功能模块")
public class ExModuleFunc extends TreeTenantEntity implements TreeNode<Long, ExModuleFunc> {
    /**
     * 编码
     */
    @NotBlank(message = "编码不能为空")
    @Size(min = 0, max = 30, message = "编码长度不能超过30个字符")
    @ApiModelProperty("编码")
    @Excel(name = "编码")
    private String code;
    /**
     * 应用appId
     */
    @ApiModelProperty("应用appId")
    private String appId;

    /**
     * 应用appId
     */
    @TableField(exist = false)
    @ApiModelProperty("所属应用")
    @Excel(name = "所属应用")
    private String appName;
    /**
     * 父节点
     */
    @TableField(exist = false)
    @ApiModelProperty("父节点")
    private ExModuleFunc parent;
    /**
     * 祖孙顺序节点列表
     */
    @TableField(exist = false)
    @ApiModelProperty("祖孙顺序节点列表")
    private List<ExModuleFunc> ancestor;
    /**
     * 子节点集合
     */
    @TableField(exist = false)
    @ApiModelProperty("子节点集合")
    private List<ExModuleFunc> children;


    /**
     * 全code(父子code间以:分隔)
     *
     * @return 全code
     */
    @ApiModelProperty("全code(父子code间以:分隔)")
    public String getAllCode() {
        return ObjectUtils.isNotEmpty(ancestor)
                ? ancestor.stream().map(ExModuleFunc::getCode).collect(Collectors.joining(SpecialCharacter.COLON.getValue().toString()))
                : StringUtils.EMPTY;
    }
}
