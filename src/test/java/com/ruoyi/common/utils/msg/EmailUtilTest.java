package com.ruoyi.common.utils.msg;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author zhaowang
 * @since 2024/11/24 9:38
 */
@SpringBootTest
public class EmailUtilTest {

    @Autowired
    private EmailUtil emailUtil;

    @Test
    public void sendSimpleMail() {
//        emailUtil.sendSimpleMail("test", "正在测试邮件配置" + DateUtils.getTime(), "15991262961@163.com");
    }
}