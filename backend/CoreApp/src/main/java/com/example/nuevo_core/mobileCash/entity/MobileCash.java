package com.example.nuevo_core.mobileCash.entity;

import com.example.nuevo_core.mobileCash.constants.MobileCashStatus;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MobileCash {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private MobileCashStatus status; //Active, Cancelled, Expired, Redeemed,Failed
    private BigDecimal amount;
    private Long originAccount;
    private String beneficiaryDocumentId;
    private String beneficiaryFullName;
    private String otp;

    @Nullable
    private LocalDateTime redeemedAt = null;
    @Nullable
    private LocalDateTime cancelledAt = null;

    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private int redeemAttempts = 0;
}
