package com.ruoyi.common.utils.redis;

import com.alibaba.fastjson2.JSONArray;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.redis.RedisCache;
import com.ruoyi.project.system.domain.SysDictData;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 字典工具类
 * 
 * @author ruoyi
 */
public class DictUtils
{
    /**
     * 分隔符
     */
    public static final String SEPARATOR = ",";

    /**
     * 设置字典缓存
     * 
     * @param key 参数键
     * @param dictDatas 字典数据列表
     */
    public static void setDictCache(String key, List<SysDictData> dictDatas)
    {
        RedisCache.getRedisCache().setCacheObject(getCacheKey(key), dictDatas);
    }

    /**
     * 获取字典缓存
     * 
     * @param key 参数键
     * @return dictDatas 字典数据列表
     */
    public static List<SysDictData> getDictCache(String key)
    {
        JSONArray arrayCache = RedisCache.getRedisCache().getCacheObject(getCacheKey(key));
        if (StringUtils.isNotNull(arrayCache))
        {
            return arrayCache.toList(SysDictData.class);
        }
        return null;
    }

    /**
     * 根据字典类型和字典值获取字典标签
     * 
     * @param dictType 字典类型
     * @param dictValue 字典值
     * @return 字典标签
     */
    public static String getDictLabel(String dictType, String dictValue)
    {
        if (StringUtils.isEmpty(dictValue))
        {
            return StringUtils.EMPTY;
        }
        return getDictLabel(dictType, dictValue, SEPARATOR);
    }

    /**
     * 根据字典类型和字典标签获取字典值
     * 
     * @param dictType 字典类型
     * @param dictLabel 字典标签
     * @return 字典值
     */
    public static String getDictValue(String dictType, String dictLabel)
    {
        if (StringUtils.isEmpty(dictLabel))
        {
            return StringUtils.EMPTY;
        }
        return getDictValue(dictType, dictLabel, SEPARATOR);
    }

    /**
     * 根据字典类型和字典值获取字典标签
     * 
     * @param dictType 字典类型
     * @param dictValue 字典值
     * @param separator 分隔符
     * @return 字典标签
     */
    public static String getDictLabel(String dictType, String dictValue, String separator)
    {
        StringBuilder propertyString = new StringBuilder();
        List<SysDictData> datas = getDictCache(dictType);
        if (StringUtils.isNull(datas))
        {
            return StringUtils.EMPTY;
        }
        if (StringUtils.containsAny(separator, dictValue))
        {
            for (SysDictData dict : datas)
            {
                for (String value : dictValue.split(separator))
                {
                    if (value.equals(dict.getDictValue()))
                    {
                        propertyString.append(dict.getDictLabel()).append(separator);
                        break;
                    }
                }
            }
        }
        else
        {
            for (SysDictData dict : datas)
            {
                if (dictValue.equals(dict.getDictValue()))
                {
                    return dict.getDictLabel();
                }
            }
        }
        return StringUtils.stripEnd(propertyString.toString(), separator);
    }

    /**
     * 根据字典类型和字典标签获取字典值
     * 
     * @param dictType 字典类型
     * @param dictLabel 字典标签
     * @param separator 分隔符
     * @return 字典值
     */
    public static String getDictValue(String dictType, String dictLabel, String separator)
    {
        StringBuilder propertyString = new StringBuilder();
        List<SysDictData> datas = getDictCache(dictType);
        if (StringUtils.isNull(datas))
        {
            return StringUtils.EMPTY;
        }
        if (StringUtils.containsAny(separator, dictLabel))
        {
            for (SysDictData dict : datas)
            {
                for (String label : dictLabel.split(separator))
                {
                    if (label.equals(dict.getDictLabel()))
                    {
                        propertyString.append(dict.getDictValue()).append(separator);
                        break;
                    }
                }
            }
        }
        else
        {
            for (SysDictData dict : datas)
            {
                if (dictLabel.equals(dict.getDictLabel()))
                {
                    return dict.getDictValue();
                }
            }
        }
        return StringUtils.stripEnd(propertyString.toString(), separator);
    }

    /**
     * 根据字典类型获取字典所有值
     *
     * @param dictType 字典类型
     * @return 字典值
     */
    public static String getDictValues(String dictType)
    {
        StringBuilder propertyString = new StringBuilder();
        List<SysDictData> datas = getDictCache(dictType);
        if (StringUtils.isNull(datas))
        {
            return StringUtils.EMPTY;
        }
        for (SysDictData dict : datas)
        {
            propertyString.append(dict.getDictValue()).append(SEPARATOR);
        }
        return StringUtils.stripEnd(propertyString.toString(), SEPARATOR);
    }

    /**
     * 根据字典类型获取字典所有标签
     *
     * @param dictType 字典类型
     * @return 字典值
     */
    public static String getDictLabels(String dictType)
    {
        StringBuilder propertyString = new StringBuilder();
        List<SysDictData> datas = getDictCache(dictType);
        if (StringUtils.isNull(datas))
        {
            return StringUtils.EMPTY;
        }
        for (SysDictData dict : datas)
        {
            propertyString.append(dict.getDictLabel()).append(SEPARATOR);
        }
        return StringUtils.stripEnd(propertyString.toString(), SEPARATOR);
    }

    /**
     * 获取字典 value-label map
     *
     * @param key type
     * @return 字典 value-label map
     */
    public static Map<String, String> getDictMap(String key) {
        List<SysDictData> dictDataList = getDictCache(key);
        if (CollectionUtils.isNotEmpty(dictDataList)) {
            return dictDataList.stream().collect(Collectors.toMap(SysDictData::getDictValue, SysDictData::getDictLabel));
        }
        return null;
    }

    /**
     * 获取字典 label-value map
     *
     * @param key type
     * @return 字典 label-value map
     */
    public static Map<String, String> getLabelValueDictMap(String key) {
        List<SysDictData> dictDataList = getDictCache(key);
        if (CollectionUtils.isNotEmpty(dictDataList)) {
            return dictDataList.stream().collect(Collectors.toMap(SysDictData::getDictLabel, SysDictData::getDictValue));
        }
        return null;
    }

    /**
     * 获取多个字典 value-label map
     *
     * @param key type
     * @return 字典 {key:{value:label},...} map
     */
    public static Map<String, Map<String, String>> getMultiDictMap(String... key) {
        Map<String, Map<String, String>> map = new HashMap<>();
        if (ArrayUtils.isNotEmpty(key)) {
            for (String k : key) {
                List<SysDictData> dictDataList = getDictCache(k);
                if (CollectionUtils.isNotEmpty(dictDataList)) {
                    map.put(k, dictDataList.stream().collect(Collectors.toMap(SysDictData::getDictValue, SysDictData::getDictLabel)));
                }
            }
        }
        return map;
    }

    /**
     * 获取多个字典 label-value map
     *
     * @param key type
     * @return 字典 {key:{label:value},...} map
     */
    public static Map<String, Map<String, String>> getMultiLabelValueDictMap(String... key) {
        Map<String, Map<String, String>> map = new HashMap<>();
        if (ArrayUtils.isNotEmpty(key)) {
            for (String k : key) {
                List<SysDictData> dictDataList = getDictCache(k);
                if (CollectionUtils.isNotEmpty(dictDataList)) {
                    map.put(k, dictDataList.stream().collect(Collectors.toMap(SysDictData::getDictLabel, SysDictData::getDictValue)));
                }
            }
        }
        return map;
    }

    /**
     * 删除指定字典缓存
     * 
     * @param key 字典键
     */
    public static void removeDictCache(String key)
    {
        RedisCache.getRedisCache().deleteObject(getCacheKey(key));
    }

    /**
     * 清空字典缓存
     */
    public static void clearDictCache()
    {
        Collection<String> keys = RedisCache.getRedisCache().keys(CacheConstants.SYS_DICT_KEY + "*");
        RedisCache.getRedisCache().deleteObject(keys);
    }

    /**
     * 设置cache key
     * 
     * @param configKey 参数键
     * @return 缓存键key
     */
    public static String getCacheKey(String configKey)
    {
        return CacheConstants.SYS_DICT_KEY + configKey;
    }
}
