package com.example.nuevo_core.mobileCash.services;

import com.example.nuevo_core.mobileCash.constants.MobileCashStatus;
import com.example.nuevo_core.mobileCash.dto.GenerateMobileCashDto;
import com.example.nuevo_core.mobileCash.dto.MobileCashDto;
import com.example.nuevo_core.mobileCash.dto.RedeemMobileCashDto;
import com.example.nuevo_core.mobileCash.entity.MobileCash;
import com.example.nuevo_core.utils.OtpService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MobileCashServiceImpl {

    @Autowired
    private OtpService _otpService;

    private final int MAX_FAILED_REDEEM_ATTEMPTS = 5;
    private final LocalDateTime today = LocalDateTime.now();

    public MobileCashServiceImpl() {
    }

    @Transactional()
    public MobileCashDto createMobileCash(GenerateMobileCashDto generateMobileCashDto) {

        //First check balance and Hold or reserve amount in origin account
        MobileCash mobileCash = MobileCash.builder()
                .status(MobileCashStatus.ACTIVE)
                .amount(generateMobileCashDto.amount())
                .originAccount(generateMobileCashDto.originAccount())
                .beneficiaryDocumentId(generateMobileCashDto.documentId())
                .beneficiaryFullName("Customer name")
                .otp(_otpService.generateOtp())
                .createdAt(today)
                .expiresAt(today.plusHours(24))
                .redeemAttempts(0)
                .build();

        //todo: Save in database


        return new MobileCashDto(mobileCash.getOtp(),
                mobileCash.getAmount(),
                mobileCash.getOriginAccount(),
                mobileCash.getBeneficiaryDocumentId(),
                mobileCash.getExpiresAt(),
                getStatusInSpanish(mobileCash.getStatus()));
    }

    public void redeemMobileCash(RedeemMobileCashDto redeemMobileCashDto) {

        //Find by otp, then validate DocId, Otp and amount
        MobileCash mc = new MobileCash();
        boolean isCustomerIdValid = redeemMobileCashDto.documentId().equals(mc.getBeneficiaryDocumentId());
        if (!isCustomerIdValid) {
            throw new RuntimeException("Datos incorrectos");
        }

        boolean isOtpValid = redeemMobileCashDto.otp().equals(mc.getOtp());
        if (!isOtpValid) {
            throw new RuntimeException("Datos incorrectos");
        }

        boolean isAmountValid = redeemMobileCashDto.amount().compareTo(mc.getAmount()) == 0;
        if (!isAmountValid) {
            throw new RuntimeException("Datos incorrectos");
        }
        
        mc.setRedeemedAt(today);
        mc.setStatus(MobileCashStatus.REDEEMED);

        //todo: save in db and then atm dispense cash to customer or subagent gives money to customer
    }

    public void cancelMobileCash(Long mobileCashId) {
    }

    public String getStatusInSpanish(MobileCashStatus status) {
        return switch (status) {
            case ACTIVE -> "Activo";
            case REDEEMED -> "Utilizado";
            case EXPIRED -> "Expirado";
            case CANCELLED -> "Cancelado";
        };
    }
}
