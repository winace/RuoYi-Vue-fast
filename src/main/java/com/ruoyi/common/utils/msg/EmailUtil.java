package com.ruoyi.common.utils.msg;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * 邮件发送工具类
 *
 * @author zhaowang
 * @since 2024-11-22
 */
@Component
@Slf4j
public class EmailUtil {

    @Value("${spring.mail.username}")
    private String emailFrom;


    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleMail(String subject, String content, String to) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            // 邮件发送者
            message.setFrom(emailFrom);
            // 邮件接受者
            message.setTo(to);
            // 主题
            message.setSubject(subject);
            // 内容
            message.setText(content);
            mailSender.send(message);
        } catch (MailException e) {
            log.info("邮件发送失败:{}", e.getMessage());
        }
    }


}
