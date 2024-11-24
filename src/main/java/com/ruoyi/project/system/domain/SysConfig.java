package com.ruoyi.project.system.domain;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.aspectj.lang.annotation.Excel.ColumnType;
import com.ruoyi.framework.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 参数配置表 sys_config
 *
 * @author ruoyi
 */
@Slf4j
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class SysConfig extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 参数主键
     */
    @Excel(name = "参数主键", cellType = ColumnType.NUMERIC)
    @TableId(type = IdType.AUTO)
    private Long configId;

    /**
     * 参数名称
     */
    @Excel(name = "参数名称")
    @NotBlank(message = "参数名称不能为空")
    @Size(min = 0, max = 100, message = "参数名称不能超过100个字符")
    private String configName;

    /**
     * 参数键名
     */
    @Excel(name = "参数键名")
    @NotBlank(message = "参数键名长度不能为空")
    @Size(min = 0, max = 100, message = "参数键名长度不能超过100个字符")
    private String configKey;

    /**
     * 参数键值
     */
    @Excel(name = "参数键值")
    @NotBlank(message = "参数键值不能为空")
    @Size(min = 0, max = 500, message = "参数键值长度不能超过500个字符")
    private String configValue;

    /**
     * 系统内置（Y是 N否）
     */
    @Excel(name = "系统内置", readConverterExp = "Y=是,N=否")
    private String configType;


    /**
     * 转化 值 为 指定类型
     *
     * @param clazz 指定类型
     * @param <T>   泛型
     * @return 指定类型的值
     */
    @SuppressWarnings("unchecked")
    public <T> T toObject(Class<T> clazz) {
        T t = null;
        String typeName = clazz.getSimpleName();
        try {
            switch (typeName) {
                case "Integer":
                    t = (T) toInteger();
                    break;
                case "Long":
                    t = (T) toLong();
                    break;
                case "Double":
                    t = (T) toDouble();
                    break;
                case "Boolean":
                    t = (T) toBoolean();
                    break;
                default:
                    t = (T) configValue;
                    break;
            }
        } catch (Exception e) {
            log.error("value to {} error: {}", typeName, e.getMessage());
        }
        return t;
    }

    public Integer toInteger() {
        return Integer.parseInt(configValue);
    }

    public Long toLong() {
        return Long.parseLong(configValue);
    }

    public Double toDouble() {
        return Double.parseDouble(configValue);
    }

    public Boolean toBoolean() {
        return Boolean.parseBoolean(configValue);
    }

    private static final Pattern JSON_OBJECT_PATTERN = Pattern.compile("^\\{(\"\\w+\"\\s*:.+,?)+}$");

    /**
     * 判断值 是否 是 JsonObject 格式
     *
     * @return 是否 是 JsonObject 格式
     */
    public boolean hasJsonObject() {
        configValue = configValue.trim();
        return JSON_OBJECT_PATTERN.matcher(configValue).find();
    }

    /**
     * 转换值 为 JsonObject
     *
     * @return JsonObject
     */
    public JSONObject covertJsonObject() {
        if (hasJsonObject()) {
            return JSON.parseObject(configValue);
        }
        return null;
    }

    /**
     * 转换 值 为 java对象
     *
     * @return java对象
     */
    public <T> T covertObject(Class<T> clazz) {
        if (hasJsonObject()) {
            return JSON.parseObject(configValue, clazz);
        }
        return null;
    }

    private static final Pattern JSON_ARRAY_PATTERN = Pattern.compile("^\\[.+]$");

    /**
     * 判断 值 是否 是 JsonArray 格式
     *
     * @return 是否 是 JsonArray 格式
     */
    public boolean hasJsonArray() {
        return JSON_ARRAY_PATTERN.matcher(configValue).find();
    }

    /**
     * 转换 值 为 JsonArray
     *
     * @return JsonArray
     */
    public JSONArray covertJsonArray() {
        if (hasJsonArray()) {
            return JSON.parseArray(configValue);
        }
        return null;
    }

    /**
     * 转换 值 为 java对象集合
     *
     * @return java对象集合
     */
    public <T> List<T> covertList(Class<T> clazz) {
        if (hasJsonArray()) {
            return JSON.parseArray(configValue, clazz);
        }
        return null;
    }
}
