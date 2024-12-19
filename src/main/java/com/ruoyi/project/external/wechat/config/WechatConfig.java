package com.ruoyi.project.external.wechat.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.WxMaConfig;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaRedissonConfigImpl;
import com.baomidou.mybatisplus.core.plugins.IgnoreStrategy;
import com.baomidou.mybatisplus.core.plugins.InterceptorIgnoreHelper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.project.external.common.domain.ExAppCfg;
import com.ruoyi.project.external.common.enums.ExAppType;
import com.ruoyi.project.external.common.mapper.ExAppCfgMapper;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import me.chanjar.weixin.mp.config.impl.WxMpRedissonConfigImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.TransportMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhaowang
 * @since 2024/12/18 09:55
 */
@Component
@RequiredArgsConstructor
public class WechatConfig {
    @Autowired
    private RedisProperties redisProperties;
    @Autowired
    private ExAppCfgMapper appCfgMapper;
    @Lazy
    @Autowired
    private WxMaService wxMaService;
    @Lazy
    @Autowired
    private WxMpService wxMpService;
    @Lazy
    @Autowired
    private RedissonClient redisson;


    @Bean
    public WxMaService wxMaService() {
        WxMaService wxMaService = new WxMaServiceImpl();
        return wxMaService;
    }

    @Bean
    public WxMpService wxMpService() {
        WxMpService wxMpService = new WxMpServiceImpl();
        return wxMpService;
    }

    @Bean
    public RedissonClient redissonClient(RedisProperties redisProperties) {
        Config config = new Config();
        config.useSingleServer().setDatabase(redisProperties.getDatabase())
                .setAddress(String.format("redis://%s:%s", redisProperties.getHost(), redisProperties.getPort()))
                .setPassword(redisProperties.getPassword());
        config.setTransportMode(TransportMode.NIO);
        return Redisson.create(config);
    }

    @PostConstruct
    public void reloadCfg() {
        InterceptorIgnoreHelper.handle(IgnoreStrategy.builder().tenantLine(true).build());
        List<ExAppCfg> appCfgList = appCfgMapper.selectList(Wrappers.lambdaQuery(ExAppCfg.class));
        InterceptorIgnoreHelper.clearIgnoreStrategy();
        List<ExAppCfg> miniappCfgList = appCfgList.stream().filter(appCfg -> appCfg.getType() == ExAppType.miniapp).collect(Collectors.toList());
        if (ObjectUtils.isNotEmpty(miniappCfgList)) {
            Map<String, WxMaConfig> appConfigMap = miniappCfgList.stream().map(appCfg -> {
                WxMaDefaultConfigImpl wxMaConfig = new WxMaRedissonConfigImpl(redisson);
                wxMaConfig.setAppid(appCfg.getAppId());
                wxMaConfig.setSecret(appCfg.getSecret());
                wxMaConfig.setToken(appCfg.getToken());
                wxMaConfig.setAesKey(appCfg.getAesKey());
                wxMaConfig.setMsgDataFormat(appCfg.getMsgDataFormat().name());
                return wxMaConfig;
            }).collect(Collectors.toMap(WxMaDefaultConfigImpl::getAppid, c -> c, (m, n) -> m));
            wxMaService.setMultiConfigs(appConfigMap);
        }
        List<ExAppCfg> mpCfgList = appCfgList.stream().filter(appCfg -> appCfg.getType() == ExAppType.mp).collect(Collectors.toList());
        if (ObjectUtils.isNotEmpty(mpCfgList)) {
            Map<String, WxMpConfigStorage> appConfigMap = mpCfgList.stream().map(appCfg -> {
                WxMpDefaultConfigImpl wxMpConfig = new WxMpRedissonConfigImpl(redisson);
                wxMpConfig.setAppId(appCfg.getAppId());
                wxMpConfig.setSecret(appCfg.getSecret());
                wxMpConfig.setToken(appCfg.getToken());
                wxMpConfig.setAesKey(appCfg.getAesKey());
                return wxMpConfig;
            }).collect(Collectors.toMap(WxMpDefaultConfigImpl::getAppId, c -> c, (m, n) -> m));
            wxMpService.setMultiConfigStorages(appConfigMap);
        }
    }
}
