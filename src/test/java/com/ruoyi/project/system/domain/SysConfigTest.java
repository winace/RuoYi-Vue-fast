package com.ruoyi.project.system.domain;


import com.ruoyi.common.utils.bean.Builder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author zhaowang
 * @since 2024/11/24 7:29
 */
@Slf4j
public class SysConfigTest {
    private SysConfig sysConfig;

    @BeforeEach
    public void before() {
        sysConfig = Builder.of(SysConfig::new).with(SysConfig::setConfigValue, "").build();
    }

    @Test
    public void toObject() {
        sysConfig.setConfigValue("1");
        Integer i = sysConfig.toObject(Integer.class);
        Assertions.assertEquals(i, 1);
        sysConfig.setConfigValue("1");
        Long l = sysConfig.toObject(Long.class);
        Assertions.assertEquals(l, 1L);
        sysConfig.setConfigValue("1.2");
        Double d = sysConfig.toObject(Double.class);
        Assertions.assertEquals(d, 1.2D);
        sysConfig.setConfigValue("false");
        Boolean b = sysConfig.toObject(Boolean.class);
        Assertions.assertFalse(b);
    }


    @Test
    public void toInt() {
        sysConfig.setConfigValue("1");
        Integer i = sysConfig.toInteger();
        Assertions.assertEquals(i, 1);
    }

    @Test
    public void toLong() {
        sysConfig.setConfigValue("1");
        Long l = sysConfig.toLong();
        Assertions.assertEquals(l, 1L);
    }

    @Test
    public void toDouble() {
        sysConfig.setConfigValue("1.2");
        Double d = sysConfig.toDouble();
        Assertions.assertEquals(d, 1.2D);
    }

    @Test
    public void toBoolean() {
        sysConfig.setConfigValue("false");
        Boolean b = sysConfig.toBoolean();
        Assertions.assertFalse(b);
    }
}