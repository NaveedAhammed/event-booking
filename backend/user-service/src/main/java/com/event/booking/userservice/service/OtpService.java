package com.event.booking.userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpService {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String OTP_PREFIX = "otp:";

    public void generateAndStoreOtp(String mobile) {
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        redisTemplate.opsForValue().set(OTP_PREFIX + mobile, otp, Duration.ofMinutes(5));

        System.out.println("OTP sent to " + mobile + " : " + otp);
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
}
