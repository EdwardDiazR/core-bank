package com.example.nuevo_core.account.model;

import com.example.nuevo_core.account.constants.AccountStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(name = "global_seq",sequenceName = "global_seq")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "global_seq")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    private String currency;
    private BigDecimal totalBalance;
    private BigDecimal inTransitAmount;
    private BigDecimal holdAmount;
    private BigDecimal availableBalance;

    private LocalDateTime createdAt;
    private LocalDateTime lastDepositDate;
    private LocalDateTime lastActivityDate;

    private Boolean hasFirstDeposit;
    private List<AccountRelative> relatives;
}
