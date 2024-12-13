package com.ruoyi.project.external.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 外部各大平台(微信/支付宝/百度/抖音/头条/飞书/QQ/快手/钉钉/淘宝/360/京东/小红书等)枚举
 *
 * @author zhaowang
 * @since 2024/11/25 16:02
 */
@AllArgsConstructor
@Getter
public enum ExPlatform {
    wechat("微信"),
    alipay("支付宝"),
    baidu("百度"),
    douyin("抖音"),
    toutiao("头条"),
    feishu("飞书"),
    qq("QQ"),
    kuaishou("快手"),
    dingtalk("钉钉"),
    taobao("淘宝"),
    qh360("360"),
    jd("京东"),
    xiaohongshu("小红书"),
    ;
    private final String label;

}
