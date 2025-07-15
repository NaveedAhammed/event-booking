package com.event.booking.userservice.service;

import com.event.booking.userservice.exception.OtpSendException;
import com.event.booking.userservice.exception.enums.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;

import static com.event.booking.userservice.constant.Constants.FAILED_TO_SEND_OTP;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpService {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String OTP_PREFIX = "otp:";

    private final JavaMailSender mailSender;

    public String generateAndStoreOtp(String mobile) {
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        redisTemplate.opsForValue().set(OTP_PREFIX + mobile, otp, Duration.ofMinutes(5));

        return otp;
    }

    public boolean validateOtp(String mobile, String otp) {
        log.info("Mobile: {}, OTP: {}", mobile, otp);
        String key = OTP_PREFIX + mobile;
        String savedOtp = redisTemplate.opsForValue().get(key);

        log.info("Key: {}, Saved OTP: {}", key, savedOtp);

        if (savedOtp != null && savedOtp.equals(otp)){
            redisTemplate.delete(key);
            return true;
        }
        return false;
    }

    public void sendOtp(String mobile, String otp, String email) {
        String subject = "Verification Code";
        String messageBody = "Your OTP code is: " + otp;

        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("event.booking@gmail.com");
            mailMessage.setTo(email);
            mailMessage.setSubject(subject);
            mailMessage.setText(messageBody);

            mailSender.send(mailMessage);
            log.info("Mail sent successfully");
        }catch (Exception e) {
            log.error("Failed to send otp: {}", e.getMessage());
            throw new OtpSendException(ExceptionCode.OTP_SEND_FAILED, FAILED_TO_SEND_OTP, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
