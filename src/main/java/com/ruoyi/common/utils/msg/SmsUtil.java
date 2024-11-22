package com.ruoyi.common.utils.msg;


import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 短信发送工具类
 *
 * @author zhaowang
 * @since 2024-11-22
 */
@Component
@Slf4j
public class SmsUtil {

    @Value("${aliyun.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.accessKeySecret}")
    private String accessKeySecret;

    @Value("${aliyun.signName}")
    private String signName;

    @Value("${aliyun.templateCode}")
    private String templateCode;

    @Value("${aliyun.endpoint}")
    private String endpoint;


    public void send(String tel, String code) throws ClientException {
        DefaultProfile profile = DefaultProfile.getProfile(endpoint, accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        SendSmsRequest request = new SendSmsRequest();
        request.setSignName(signName);
        request.setTemplateCode(templateCode);
        //设置手机号
        request.setPhoneNumbers(tel);
        Map<String, String> param = new HashMap<>();
        param.put("password", code);
        Gson gson = new Gson();
        String json = gson.toJson(param);
        //设置随机验证码
        request.setTemplateParam(json);
        //发送过程
        SendSmsResponse response = client.getAcsResponse(request);
        log.info(response.getMessage());
    }

}
