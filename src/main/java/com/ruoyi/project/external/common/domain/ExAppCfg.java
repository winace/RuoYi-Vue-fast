package com.ruoyi.project.external.common.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseTenantEntity;
import com.ruoyi.project.external.common.enums.ExAppType;
import com.ruoyi.project.external.common.enums.ExMsgDataFormat;
import com.ruoyi.project.external.common.enums.ExPlatform;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 外部应用配置 实体对象 ex_app_cfg
 *
 * @author zhaowang
 * @since 2024-11-25 23:38:24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("外部应用配置")
public class ExAppCfg extends BaseTenantEntity {

    /**
     * 应用appId
     */
    @TableId
    @ApiModelProperty("应用appId")
    private String appId;
    /**
     * 应用名称
     */
    @ApiModelProperty("应用名称")
    @Excel(name = "应用名称")
    private String name;
    /**
     * 应用密钥
     */
    @ApiModelProperty("应用密钥")
    @Excel(name = "应用密钥")
    private String secret;
    /**
     * 令牌
     */
    @ApiModelProperty("令牌")
    @Excel(name = "令牌")
    private String token;
    /**
     * 消息加密密钥
     */
    @ApiModelProperty("消息加密密钥")
    @Excel(name = "消息加密密钥")
    private String aesKey;
    /**
     * 数据格式
     */
    @ApiModelProperty("数据格式")
    @Excel(name = "数据格式")
    private ExMsgDataFormat msgDataFormat;
    /**
     * 所属平台
     */
    @ApiModelProperty("所属平台")
    @Excel(name = "所属平台")
    private ExPlatform platform;
    /**
     * 应用类型
     */
    @ApiModelProperty("应用类型")
    @Excel(name = "应用类型")
    private ExAppType type = ExAppType.miniapp;


    public String getPlatformLabel() {
        return platform != null ? platform.getLabel() : null;
    }

    public String getTypeLabel() {
        return type != null ? type.getLabel() : null;
    }
}
