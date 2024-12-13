package com.ruoyi.project.external.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 应用类型(小程序/公众号等)
 *
 * @author zhaowang
 * @since 2024/11/25 19:53
 */
@AllArgsConstructor
@Getter
public enum ExAppType {
    miniapp("小程序"),
    mp("公众号"),
    ;
    private final String label;
}
