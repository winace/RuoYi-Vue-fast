package com.ruoyi;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 启动程序
 *
 * @author ruoyi
 */
@Slf4j
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class RuoYiApplication
{
    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        ConfigurableApplicationContext application = SpringApplication.run(RuoYiApplication.class, args);
        Environment env = application.getEnvironment();
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");
        if (StringUtils.isNotBlank(path)) {
            path = "/".equals(path) ? "" : path;
        }
        String name = env.getProperty("ruoyi.name");
        String version = env.getProperty("ruoyi.version");

        String knife4jPath = "/doc.html";
        String localPath = String.format("http://localhost:%s%s%s", port, path, knife4jPath);

        String finalPath = path;
        List<String> externalPathList = IpUtils.getMultipleIps()
                .stream().map(ip -> String.format("http://%s:%s%s%s", ip, port, finalPath, knife4jPath))
                .collect(Collectors.toList());
        String externalPath = StringUtils.join(externalPathList, "\n              ");
        log.info(String.format("\n----------------------------------------------------------\n" +
                        "      (♥◠‿◠)ﾉﾞ  服务平台 启动成功   ლ(´ڡ`ლ)ﾞ\n" +
                        "Application %s(%s) is running! Access URLs:\n" +
                        "    Local:    %s\n" +
                        "    External: %s\n" +
                        "----------------------------------------------------------",
                name, version, localPath, externalPath)
        );
    }
}
