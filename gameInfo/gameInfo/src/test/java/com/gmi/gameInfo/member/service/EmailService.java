package com.gmi.gameInfo.member.service;

import com.gmi.gameInfo.member.domain.AuthEmail;
import com.gmi.gameInfo.member.repository.EmailRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailService {

    private final EmailRepository emailRepository;

    @Value("${spring.mail.host}")
    private String email;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.port}")
    private int port;

    @Transactional
    public boolean sendAuthNumberEmail(AuthEmail authEmail) {

        Properties prop = new Properties();
        prop.put("mail.smtp.host", email);
        prop.put("mail.smtp.port", port);
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.ssl.enable", "true");
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        });

        MimeMessage message = new MimeMessage(session);

        try {


        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }


    public boolean validAuthNum(AuthEmail email) {

        return false;
    }

    public String getEmailMessage() {

        String authNum = getAuthNum();

        return "게임인포 인증번호 : [" + authNum + "] 입니다.";
    }

    public String getAuthNum() {
        Random random = new Random();
        StringBuilder authNum = new StringBuilder();
        int len = 6;

        for(int i = 0; i < len; i++) {
            authNum.append(random.nextInt(9));
        }

        return authNum.toString();
    }
}
