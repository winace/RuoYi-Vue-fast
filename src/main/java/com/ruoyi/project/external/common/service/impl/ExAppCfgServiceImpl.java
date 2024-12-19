package com.ruoyi.project.external.common.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.project.external.common.enums.ExAppType;
import com.ruoyi.project.external.wechat.config.WechatConfig;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.stereotype.Service;
import com.ruoyi.project.external.common.mapper.ExAppCfgMapper;
import com.ruoyi.project.external.common.domain.ExAppCfg;
import com.ruoyi.project.external.common.service.IExAppCfgService;
import org.springframework.transaction.annotation.Transactional;


/**
 * 应用配置 业务实现
 *
 * @author zhaowang
 * @since 2024-11-26 10:04:39
 */
@Slf4j
@Service
public class ExAppCfgServiceImpl extends ServiceImpl<ExAppCfgMapper, ExAppCfg> implements IExAppCfgService {
    @Override
    public IPage<ExAppCfg> queryPage(Page<ExAppCfg> pageDto, ExAppCfg exAppCfg) {
        return baseMapper.selectDataPage(pageDto, exAppCfg);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveAppCfg(ExAppCfg appCfg) {
        ExAppCfg old = baseMapper.selectById(appCfg.getAppId());
        int res;
        if (old == null) {
            res = baseMapper.insert(appCfg);
        } else {
            res = baseMapper.updateById(appCfg);
        }
        WechatConfig wechatConfig = SpringUtils.getBean(WechatConfig.class);
        wechatConfig.reloadCfg();
        checkWechatCfg(appCfg);
        return res > 0 && checkWechatCfg(appCfg);
    }

    private boolean checkWechatCfg(ExAppCfg appCfg) {
        String appId = appCfg.getAppId();
        String accessToken;
        ExAppType appType = appCfg.getType();
        try {
            if (appType == ExAppType.miniapp) {
                WxMaService wxMaService = SpringUtils.getBean(WxMaService.class);
                wxMaService.switchover(appId);
                accessToken = wxMaService.getAccessToken();
            } else {
                WxMpService wxMpService = SpringUtils.getBean(WxMpService.class);
                wxMpService.switchover(appId);
                accessToken = wxMpService.getAccessToken();
            }
            return StringUtils.isNotBlank(accessToken);
        } catch (WxErrorException e) {
            String msg = appType.getLabel() + "配置错误，无法获取AccessToken";
            log.error("app config: {}, {}\n wechat error msg: {}", JSONUtil.toJsonStr(appCfg), msg, e.getMessage());
            throw new ServiceException(msg);
        }
    }
}
