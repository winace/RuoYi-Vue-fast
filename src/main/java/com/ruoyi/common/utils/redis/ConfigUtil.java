package com.ruoyi.common.utils.redis;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.Builder;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.framework.redis.RedisCache;
import com.ruoyi.project.system.domain.SysConfig;
import com.ruoyi.project.system.mapper.SysConfigMapper;

import java.util.Collection;
import java.util.List;

/**
 * 配置工具类
 *
 * @author zoneway
 */
public class ConfigUtil {

    /**
     * 设置配置缓存
     *
     * @param config 配置数据
     */
    public static void setConfigCache(SysConfig config) {
        RedisCache.getRedisCache().setCacheObject(getCacheKey(config.getConfigKey()), config);
    }

    /**
     * 设置配置缓存
     *
     * @param configList 配置数据列表
     */
    public static void setConfigListCache(List<SysConfig> configList) {
        for (SysConfig config : configList) {
            setConfigCache(config);
        }
    }

    /**
     * 获取配置缓存
     *
     * @param key 参数键
     * @return 配置数据
     */
    public static SysConfig getConfigCache(String key) {
        Object cacheObj = RedisCache.getRedisCache().getCacheObject(getCacheKey(key));
        if (StringUtils.isNotNull(cacheObj)) {
            if (cacheObj instanceof JSONObject) {
                JSONObject json = (JSONObject) cacheObj;
                return json.to(SysConfig.class);
            } else {
                return (SysConfig) cacheObj;
            }
        } else {
            SysConfigMapper configMapper = SpringUtils.getBean(SysConfigMapper.class);
            SysConfig param = Builder.of(SysConfig::new).with(SysConfig::setConfigKey, key).build();
            SysConfig config = configMapper.selectConfig(param);
            if (config != null) {
                setConfigCache(config);
            }
            return config;
        }
    }

    /**
     * 删除指定配置缓存
     *
     * @param key 配置键
     */
    public static void removeConfigCache(String key) {
        RedisCache.getRedisCache().deleteObject(getCacheKey(key));
    }

    /**
     * 清空参数缓存数据
     */
    public static void clearConfigCache() {
        Collection<String> keys = RedisCache.getRedisCache().keys(CacheConstants.SYS_CONFIG_KEY + "*");
        RedisCache.getRedisCache().deleteObject(keys);
    }

    /**
     * 设置cache key
     *
     * @param configKey 参数键
     * @return 缓存键key
     */
    private static String getCacheKey(String configKey) {
        return CacheConstants.SYS_CONFIG_KEY + configKey;
    }

}
