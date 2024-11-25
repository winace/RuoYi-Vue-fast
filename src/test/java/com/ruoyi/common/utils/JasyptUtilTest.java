package com.ruoyi.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author zhaowang
 * @since 2024/11/25 11:51
 */
@Slf4j
public class JasyptUtilTest {

    @Test
    public void testCrypt() {
        String salt = "company";
        String username = "wa_demo_test@163.com";
        assertCrypt(username, salt);
        String pwd = "HJOYJPDYWOTJPDDZ";
        assertCrypt(pwd, salt);
    }

    private static void assertCrypt(String str, String salt) {
        String enUsername = JasyptUtil.encrypt(str, salt);
        log.info("Source str {}\nJasypt.encrypt: ENC({})\n\n", str, enUsername);
        Assert.assertNotNull(enUsername);
        String deUsername = JasyptUtil.decrypt(enUsername, salt);
        Assert.assertNotNull(deUsername);
        Assert.assertEquals(str, deUsername);
    }
}