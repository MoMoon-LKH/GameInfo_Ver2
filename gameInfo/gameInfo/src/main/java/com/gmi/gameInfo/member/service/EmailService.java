package com.gmi.gameInfo.member.service;

import com.gmi.gameInfo.member.domain.AuthEmail;
import com.gmi.gameInfo.member.domain.Member;
import com.gmi.gameInfo.member.exception.DifferentAuthEmailNumberException;
import com.gmi.gameInfo.member.exception.DuplicateEmailException;
import com.gmi.gameInfo.member.exception.NotFoundAuthEmailException;
import com.gmi.gameInfo.member.exception.SendEmailFailException;
import com.gmi.gameInfo.member.repository.EmailRepository;
import com.gmi.gameInfo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailService {

    private final EmailRepository emailRepository;

    private final MemberRepository memberRepository;

    @Value("${spring.mail.username}")
    private String email;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.port}")
    private int port;


    @Transactional
    public AuthEmail save(AuthEmail email) {
        return emailRepository.save(email);
    }

    @Transactional
    public void deleteById(String id) {
        emailRepository.deleteById(id);
    }

    public AuthEmail findOneById(String id) {
        return emailRepository.findById(id)
                .orElseThrow(NotFoundAuthEmailException::new);
    }

    public AuthEmail findByEmail(String email) {
        return emailRepository.findAuthEmailByEmail(email)
                .orElseThrow(NotFoundAuthEmailException::new);
    }

    @Transactional
    public AuthEmail sendAndSaveAuthEmail(AuthEmail authEmail) {

        checkDuplicateEmail(authEmail.getEmail());
        Optional<AuthEmail> existEmail = emailRepository.findAuthEmailByEmail(authEmail.getEmail());

        if (existEmail.isPresent()) {
            emailRepository.delete(existEmail.get());
        }

        boolean boolSendEmail = sendAuthNumberEmail(authEmail);

        if(!boolSendEmail) {
            throw new SendEmailFailException();
        }

        return emailRepository.save(authEmail);
    }

    public boolean sendAuthNumberEmail(AuthEmail authEmail) {

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", port);
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");

        try {
            Session session = Session.getInstance(prop, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(email, password);
                }
            });

            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(email));
            // 수신자 
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(authEmail.getEmail()));
            message.setSubject("게임인포 인증번호");
            message.setText(getEmailMessage(authEmail.getAuthNum()));

            Transport.send(message);

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            throw new SendEmailFailException();
        }
    }

    private String getEmailMessage(String authNum) {
        return "게임인포 이메일 인증번호는 [" + authNum + "] 입니다.";
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


    public boolean confirmAuthNum(AuthEmail authEmail, String authNum) {

        if (!authEmail.getAuthNum().equals(authNum)) {
            throw new DifferentAuthEmailNumberException();
        }

        return true;
    }

    public boolean checkDuplicateEmail(String email) {
        Optional<Member> member = memberRepository.findMemberByEmail(email);

        if (member.isPresent()) {
            throw new DuplicateEmailException();
        }

        return true;
    }


}
