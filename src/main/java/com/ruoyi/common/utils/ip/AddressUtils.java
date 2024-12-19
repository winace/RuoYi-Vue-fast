package com.ruoyi.common.utils.ip;

import cn.hutool.json.JSON;
import com.google.common.collect.Maps;
import com.ruoyi.common.utils.http.HttpUtil;
import com.ruoyi.framework.config.RuoYiConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 获取地址类
 *
 * @author ruoyi
 */
public class AddressUtils
{
    private static final Logger log = LoggerFactory.getLogger(AddressUtils.class);

    // IP地址查询
    public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp";

    // 未知地址
    public static final String UNKNOWN = "XX XX";

    public static String getRealAddressByIP(String ip)
    {
        // 内网不查询
        if (IpUtils.internalIp(ip))
        {
            return "内网IP";
        }
        if (RuoYiConfig.isAddressEnabled())
        {
            try
            {
                Map<String, Object> params = Maps.newHashMap();
                params.put("ip", ip);
                params.put("json", true);
                JSON obj = HttpUtil.get(IP_URL, params);
                String region = obj.getByPath("pro", String.class);
                String city = obj.getByPath("city", String.class);
                return String.format("%s %s", region, city);
            }
            catch (Exception e)
            {
                log.error("获取地理位置异常 {}", ip);
            }
        }
        return UNKNOWN;
    }
}
