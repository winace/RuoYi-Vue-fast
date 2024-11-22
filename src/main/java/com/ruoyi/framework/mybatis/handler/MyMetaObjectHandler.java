package com.ruoyi.framework.mybatis.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.ruoyi.common.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * @author zhaowang
 * @version 1.0
 * @since 2022/05/16 17:12
 */
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * createTime 属性名Key
     */
    private static final String CREATE_TIME_ATTR = "createTime";
    /**
     * createBy 属性名Key
     */
    private static final String CREATE_BY_ATTR = "createBy";
    /**
     * updateTime 属性名Key
     */
    private static final String UPDATE_TIME_ATTR = "updateTime";
    /**
     * updateBy 属性名Key
     */
    private static final String UPDATE_BY_ATTR = "updateBy";

    @Override
    public void insertFill(MetaObject metaObject) {
        if (metaObject.hasSetter(CREATE_TIME_ATTR)) {
            // 起始版本 3.3.0(推荐使用)
            this.strictInsertFill(metaObject, CREATE_TIME_ATTR, Date.class, new Date());
        }
        if (metaObject.hasSetter(CREATE_BY_ATTR)) {
            // 起始版本 3.3.0(推荐使用)
            this.strictInsertFill(metaObject, CREATE_BY_ATTR, String.class, getUser());
        }
        updateFill(metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if (metaObject.hasSetter(UPDATE_TIME_ATTR)) {
            // 起始版本 3.3.0(推荐)
            this.strictUpdateFill(metaObject, UPDATE_TIME_ATTR, Date.class, new Date());
        }
        if (metaObject.hasSetter(UPDATE_BY_ATTR)) {
            // 起始版本 3.3.0(推荐使用)
            this.strictUpdateFill(metaObject, UPDATE_BY_ATTR, String.class, getUser());
        }
    }

    private String getUser() {
        try {
            String username = SecurityUtils.getUsername();
            if (username != null) {
                return username;
            }
        } catch (Exception ignored) {
        }
        return null;
    }
}
