package com.ruoyi.project.external.common.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.google.common.collect.Lists;
import com.ruoyi.common.enums.SpecialCharacter;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.aspectj.lang.annotation.Sensitive;
import com.ruoyi.framework.aspectj.lang.enums.DesensitizedType;
import com.ruoyi.framework.web.domain.BaseTenantEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 外部应用用户 实体对象 ex_user
 *
 * @author zhaowang
 * @since 2024-11-25 23:38:24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("外部应用用户")
public class ExUser extends BaseTenantEntity {

    /**
     * openid
     */
    @TableId
    @ApiModelProperty("openid")
    @Excel(name = "openid")
    private String openid;
    /**
     * unionid
     */
    @ApiModelProperty("unionid")
    @Excel(name = "unionid")
    private String unionid;
    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    @Excel(name = "手机号")
    @Sensitive(desensitizedType = DesensitizedType.PHONE)
    private String phone;
    /**
     * 昵称
     */
    @ApiModelProperty("昵称")
    @Excel(name = "昵称")
    private String nickName;
    /**
     * 用户头像
     */
    @ApiModelProperty("用户头像")
    @Excel(name = "用户头像")
    private String avatar;
    /**
     * 用户性别
     */
    @ApiModelProperty("用户性别")
    @Excel(name = "用户性别")
    private String gender;
    /**
     * 国家
     */
    @ApiModelProperty("国家")
    @Excel(name = "国家")
    private String country;
    /**
     * 省份
     */
    @ApiModelProperty("省份")
    @Excel(name = "省份")
    private String province;
    /**
     * 城市
     */
    @ApiModelProperty("城市")
    @Excel(name = "城市")
    private String city;
    /**
     * 应用appId
     */
    @ApiModelProperty("应用appId")
    @Excel(name = "应用appId")
    private String appId;
    /**
     * 授权功能模块
     */
    @ApiModelProperty("授权功能模块")
    @Excel(name = "授权功能模块")
    private String funcIds;


    public List<Long> getFuncIdList() {
        List<Long> funcIdList = Lists.newArrayList();
        if (StringUtils.isNotBlank(funcIds)) {
            for (String funcId : funcIds.split(SpecialCharacter.COMMA.getValue().toString())) {
                funcIdList.add(Long.valueOf(funcId));
            }
        }
        return funcIdList;
    }
}
