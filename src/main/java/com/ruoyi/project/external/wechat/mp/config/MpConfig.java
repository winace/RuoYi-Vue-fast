package com.ruoyi.project.external.wechat.mp.config;

import cn.hutool.json.JSONUtil;
import com.ruoyi.project.external.wechat.mp.builder.TextBuilder;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType.EVENT;
import static me.chanjar.weixin.mp.constant.WxMpEventConstants.CustomerService.*;
import static me.chanjar.weixin.mp.constant.WxMpEventConstants.POI_CHECK_NOTIFY;

/**
 * @author zhaowang
 * @since 2024/12/19 15:08
 */
@Slf4j
@Configuration
public class MpConfig {
    @Bean
    public WxMpMessageRouter messageRouter(WxMpService wxMpService) {
        final WxMpMessageRouter newRouter = new WxMpMessageRouter(wxMpService);

        // 记录所有事件的日志 （异步执行）
        newRouter.rule().handler(logHandler).next();

        // 接收客服会话管理事件
        newRouter.rule().async(false).msgType(EVENT).event(KF_CREATE_SESSION).handler(kfSessionHandler).end();
        newRouter.rule().async(false).msgType(EVENT).event(KF_CLOSE_SESSION).handler(kfSessionHandler).end();
        newRouter.rule().async(false).msgType(EVENT).event(KF_SWITCH_SESSION).handler(kfSessionHandler).end();

        // 门店审核事件
        newRouter.rule().async(false).msgType(EVENT).event(POI_CHECK_NOTIFY).handler(storeCheckNotifyHandler).end();

        // 自定义菜单事件
        newRouter.rule().async(false).msgType(EVENT).event(WxConsts.EventType.CLICK).handler(menuHandler).end();

        // 点击菜单连接事件
        newRouter.rule().async(false).msgType(EVENT).event(WxConsts.EventType.VIEW).handler(nullHandler).end();

        // 关注事件
        newRouter.rule().async(false).msgType(EVENT).event(WxConsts.EventType.SUBSCRIBE).handler(subscribeHandler).end();

        // 取消关注事件
        newRouter.rule().async(false).msgType(EVENT).event(WxConsts.EventType.UNSUBSCRIBE).handler(unsubscribeHandler).end();

        // 上报地理位置事件
        newRouter.rule().async(false).msgType(EVENT).event(WxConsts.EventType.LOCATION).handler(locationHandler).end();

        // 接收地理位置消息
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.LOCATION).handler(locationHandler).end();

        // 扫码事件
        newRouter.rule().async(false).msgType(EVENT).event(WxConsts.EventType.SCAN).handler(scanHandler).end();

        // 默认
        newRouter.rule().async(false).handler(msgHandler).end();

        return newRouter;
    }

    private final WxMpMessageHandler logHandler = (wxMessage, map, wxMpService1, wxSessionManager) -> {
        log.info("\n接收到请求消息，内容：{}", JSONUtil.toJsonStr(wxMessage));
        return null;
    };

    private final WxMpMessageHandler kfSessionHandler = (wxMessage, map, wxMpService, wxSessionManager) -> null;
    // 处理门店审核事件
    private final WxMpMessageHandler storeCheckNotifyHandler = (wxMessage, map, wxMpService, wxSessionManager) -> null;

    private final WxMpMessageHandler menuHandler = (wxMessage, map, wxMpService, wxSessionManager) -> {
        String msg = String.format("type:%s, event:%s, key:%s",
                wxMessage.getMsgType(), wxMessage.getEvent(),
                wxMessage.getEventKey());
        if (WxConsts.EventType.VIEW.equals(wxMessage.getEvent())) {
            return null;
        }

        return WxMpXmlOutMessage.TEXT().content(msg)
                .fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser())
                .build();
    };
    private final WxMpMessageHandler nullHandler = (wxMessage, map, wxMpService, wxSessionManager) -> null;

    private final WxMpMessageHandler subscribeHandler = (wxMessage, map, wxMpService, wxSessionManager) -> {
        log.info("新关注用户 OPENID: " + wxMessage.getFromUser());

        // 获取微信用户基本信息
        try {
            WxMpUser userWxInfo = wxMpService.getUserService()
                    .userInfo(wxMessage.getFromUser(), null);
            if (userWxInfo != null) {
                // TODO 可以添加关注用户到本地数据库
            }
        } catch (WxErrorException e) {
            if (e.getError().getErrorCode() == 48001) {
                log.info("该公众号没有获取用户信息权限！");
            }
        }


        WxMpXmlOutMessage responseResult = null;
        try {
//            处理特殊请求，比如如果是扫码进来的，可以做相应处理
//            responseResult = this.handleSpecial(wxMessage);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        if (responseResult != null) {
            return responseResult;
        }

        try {
            return new TextBuilder().build("感谢关注", wxMessage, wxMpService);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    };
    private final WxMpMessageHandler unsubscribeHandler = (wxMessage, map, wxMpService, wxSessionManager) -> {
        String openId = wxMessage.getFromUser();
        log.info("取消关注用户 OPENID: " + openId);
        // TODO 可以更新本地数据库为取消关注状态
        return null;
    };

    private final WxMpMessageHandler locationHandler = (wxMessage, map, wxMpService, wxSessionManager) -> {
        if (wxMessage.getMsgType().equals(WxConsts.XmlMsgType.LOCATION)) {
            //TODO 接收处理用户发送的地理位置消息
            try {
                String content = "感谢反馈，您的的地理位置已收到！";
                return new TextBuilder().build(content, wxMessage, null);
            } catch (Exception e) {
                log.error("位置消息接收处理失败", e);
                return null;
            }
        }

        //上报地理位置事件
        log.info("上报地理位置，纬度 : {}，经度 : {}，精度 : {}",
                wxMessage.getLatitude(), wxMessage.getLongitude(), String.valueOf(wxMessage.getPrecision()));
        //TODO  可以将用户地理位置信息保存到本地数据库，以便以后使用
        return null;
    };

    private final WxMpMessageHandler scanHandler = (wxMessage, map, wxMpService, wxSessionManager) -> null;
    private final WxMpMessageHandler msgHandler = (wxMessage, map, wxMpService, wxSessionManager) -> {
        if (!wxMessage.getMsgType().equals(WxConsts.XmlMsgType.EVENT)) {
            //TODO 可以选择将消息保存到本地
        }

        //当用户输入关键词如“你好”，“客服”等，并且有客服在线时，把消息转发给在线客服
        try {
            if (StringUtils.startsWithAny(wxMessage.getContent(), "你好", "客服")
                    && !wxMpService.getKefuService().kfOnlineList().getKfOnlineList().isEmpty()) {
                return WxMpXmlOutMessage.TRANSFER_CUSTOMER_SERVICE()
                        .fromUser(wxMessage.getToUser())
                        .toUser(wxMessage.getFromUser()).build();
            }
        } catch (WxErrorException e) {
            e.printStackTrace();
        }

        //TODO 组装回复消息
        String content = "收到信息内容：" + JSONUtil.toJsonStr(wxMessage);

        return new TextBuilder().build(content, wxMessage, wxMpService);
    };
}
