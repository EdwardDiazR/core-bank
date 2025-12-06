package com.example.nuevo_core.mobileCash.controller;

import com.example.nuevo_core.utils.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/mobile-cash")
public class MobileCashController {

    @Autowired
    private OtpService _otpService;

    @GetMapping("generate-otp")
    public ResponseEntity<String> generateOtp() {

        return ResponseEntity.ok(_otpService.generateOtp());
    }
}
