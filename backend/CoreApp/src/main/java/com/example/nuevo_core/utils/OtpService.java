package com.example.nuevo_core.utils;

import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class OtpService {

    public String generateOtp() {
        int otp = ThreadLocalRandom.current().nextInt(100000, 999999);
        return String.valueOf(otp);
    }

    public String formatOtp(String otp) {
        return otp.substring(0, 3) + " " + otp.substring(3, 6);
    }
}
