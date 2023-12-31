package com.example.demo.common;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMessage.RecipientType;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MailComponent {
  private final JavaMailSender javaMailSender;
  @Value("${spring.mail.username}")
  private String senderEmail;

  public String sendEmailWithCode(String email) {
    String authCode = createCode();
    MimeMessage message = createEmailForm(email, authCode);
    javaMailSender.send(message);
    return authCode;
  }

  private String createCode() {
    Random random = new Random();
    StringBuilder sb = new StringBuilder();
    for(int i = 0; i < 6; i++) {
      sb.append(random.nextInt(0, 10));
    }
    return sb.toString();
  }

  private MimeMessage createEmailForm(String email, String authCode) {
   MimeMessage message = javaMailSender.createMimeMessage();
    try {
      message.setFrom(senderEmail);
      message.setRecipients(RecipientType.TO, email);
      message.setSubject("회원 가입 인증 이메일입니다.");
      String body = "<h2>이메일 인증 번호입니다.</h2>"
                  + "<h1>" + authCode + "</h1>";
      message.setText(body, "UTF-8", "html");
    } catch (MessagingException e) {
      e.printStackTrace();
      log.warn("[" + email + "] 으로 이메일 전송 실패");
    }
    return message;
  }
}
