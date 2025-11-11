package com.example.nuevo_core.loan.services;

import com.example.nuevo_core.loan.model.LoanCharge;
import com.example.nuevo_core.account.interfaces.IAccountService;
import com.example.nuevo_core.loan.model.LoanPayment;
import com.example.nuevo_core.loanAmortization.amortizationTable.IAmortizationService;
import com.example.nuevo_core.loanAmortization.amortizationTableItem.AmortizationTableItem;
import com.example.nuevo_core.loan.interfaces.ILoanService;
import com.example.nuevo_core.loan.model.Loan;
import com.example.nuevo_core.loan.repository.LoanRepository;
import com.example.nuevo_core.loanAmortization.amortizationTableItem.IAmortizationTableItemRepository;
import com.example.nuevo_core.loan.interfaces.ILoanPaymentService;
import com.example.nuevo_core.loan.constants.PaymentStatus;
import com.example.nuevo_core.loan.dto.loanPayment.PayLoanDto;
import com.example.nuevo_core.loan.repository.LoanPaymentRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class LoanPaymentService implements ILoanPaymentService {
    private final LocalDateTime now = LocalDateTime.now();

    private final ILoanService _loanService;
    private final IAmortizationService _amortizationService;
    private final IAmortizationTableItemRepository _amortizationItemRepo;
    private final LoanRepository _loanRepository;
    private final LoanPaymentRepository _loanPaymentRepository;
    private final IAccountService _accountService;

    public LoanPaymentService(ILoanService loanService,
                              IAmortizationService amortizationService,
                              LoanRepository loanRepository,
                              LoanPaymentRepository repo,
                              IAmortizationTableItemRepository amortItemRepo,
                              IAccountService accountService) {
        _loanService = loanService;
        _amortizationService = amortizationService;
        _loanRepository = loanRepository;
        _loanPaymentRepository = repo;
        _amortizationItemRepo = amortItemRepo;
        _accountService = accountService;
    }

    @Autowired
    EntityManager entityManager;

    @Transactional
    public void generateLoanPaymentInvoices() {
        log.info("Generating Invoices");
        entityManager.clear();
        List<AmortizationTableItem> items = findPendingInstallmentsForInvoicing();

        for (AmortizationTableItem amortizationItem : items) {
            System.out.println(amortizationItem.getAmortizationTable().getLoanId());
        }

        for (AmortizationTableItem amortizationItem : items) {
            boolean exists = _loanPaymentRepository.existsByAmortizationItemId(amortizationItem);
            if (!exists) {
                LoanPayment payment = LoanPayment.builder()
                        .installmentAmount(amortizationItem.getCuota())
                        .outstandingPrincipalDue(amortizationItem.getCapital())
                        .outstandingPrincipalPaid(BigDecimal.ZERO)
                        .interestDue(amortizationItem.getInteres())
                        .interestPaid(BigDecimal.ZERO)
                        .dueDate(amortizationItem.getPaymentDate())
                        .amortizationItemId(amortizationItem)
                        .status(PaymentStatus.PENDING)
                        .lastPaymentDate(null)
                        .isPaid(false)
                        .loanId(amortizationItem.getAmortizationTable().getLoanId())
                        .build();
                entityManager.persist(payment);
            } else {
                continue;
            }
        }


    }

    public List<AmortizationTableItem> findPendingInstallmentsForInvoicing() {
        LocalDate today = LocalDate.now();
        LocalDate fiveDaysLater = today.plusDays(5);

        return _loanPaymentRepository.generateLoanPayments(today, fiveDaysLater);
    }

    public List<LoanPayment> getDueInstallmentsByLoanId(Long loanId) {
        return _loanPaymentRepository.findPendingInstallmentsByLoanId(loanId);
    }

    public List<LoanCharge> getDueChargesByLoanId(Long loanId) {
        return new ArrayList<>();
    }

    public List<Long> getAllDueInstallmentsToAutopay() {
        return _loanPaymentRepository.findLoansWithDueInstallmentsToAutopay(now.toLocalDate());
    }

    @Transactional
    public void payLoan(PayLoanDto payLoanDto) {
        List<LoanPayment> dueInstallments = getDueInstallmentsByLoanId(payLoanDto.loan().getId());
        List<LoanCharge> dueCharges = getDueChargesByLoanId(payLoanDto.loan().getId());

        processPayment(dueInstallments, dueCharges, payLoanDto.loan(), payLoanDto.amount(), payLoanDto.source());
    }

    @Transactional
    public void autoPayLoan(Long loanId) {
        log.info("Trying autopay loan");

        Loan loan = _loanRepository.getReferenceById(loanId);
        List<LoanPayment> dueInstallments = getDueInstallmentsByLoanId(loanId);
        List<LoanCharge> dueCharges = getDueChargesByLoanId(loanId);

        BigDecimal totalDue = calculateTotalDue(dueInstallments, dueCharges);

        //Get account number, balance, and others
        BigDecimal accountBalance = _accountService.checkBalanceByAccountId(loan.getLinkedAccount()); //todo:debit amount from account
        BigDecimal amountToDebit = accountBalance.min(totalDue);

        if (accountBalance.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("No balance");
        }

        String transactionDescription = "Autopago Prestamo " + loan.getId();

        BigDecimal remainingBalance = _accountService.withdrawAmountFromAccount(
                loan.getLinkedAccount(),
                amountToDebit,
                transactionDescription);

        processPayment(dueInstallments, dueCharges, loan, remainingBalance, "AUTO PAGO");
    }

    @Transactional
    public void processPayment(List<LoanPayment> installments,
                               List<LoanCharge> charges,
                               Loan loan,
                               BigDecimal amountToPay,
                               String paymentSource) {

        BigDecimal remainingBalance = amountToPay;

        if (!charges.isEmpty()) {
            for (LoanCharge charge : charges) {
                BigDecimal lateFeePayment = remainingBalance.min(charge.getAmount());

                remainingBalance = remainingBalance.subtract(lateFeePayment);
                //Transfer amount to internal account

                if (lateFeePayment.compareTo(charge.getAmount()) == 0) {
                    charge.setPaid(true);
                }
                //todo: generate transaction in loan of late fee payment
            }
        }

        for (LoanPayment installment : installments) {
            BigDecimal installmentPending = installment.getPendingInstallmentBalance();

            BigDecimal interestPending = installment
                    .getInterestDue()
                    .subtract(installment.getInterestPaid());

            BigDecimal outstandingPrincipalPending = installment
                    .getOutstandingPrincipalDue()
                    .subtract(installment.getOutstandingPrincipalPaid());

            //Subtract capital paid from remaining balance to apply to installment interest
            BigDecimal interestPayment = remainingBalance.min(interestPending);
            remainingBalance = remainingBalance.subtract(interestPayment);

            BigDecimal updatedLoanInterestBalance = loan.getInterestBalance().subtract(interestPayment);
            loan.setInterestBalance(updatedLoanInterestBalance);

            installment.setInterestPaid(installment.getInterestPaid()
                    .add(interestPayment));
            //todo: transfer that amount to interest internal account

            //Subtract capital paid from remaining balance to apply to installment outstandingPrincipal
            BigDecimal outstandingPrincipalPayment = remainingBalance.min(outstandingPrincipalPending);
            remainingBalance = remainingBalance.subtract(outstandingPrincipalPayment);

            BigDecimal updatedLoanOutstandingPrincipalBalance = loan.getOutstandingPrincipalAmount()
                    .subtract(outstandingPrincipalPayment);

            loan.setOutstandingPrincipalAmount(updatedLoanOutstandingPrincipalBalance);

            installment.setOutstandingPrincipalPaid(
                    installment.getOutstandingPrincipalPaid()
                            .add(outstandingPrincipalPayment));

            //todo: transfer that amount to capital internal account

            installment.recalculateTotal();//Recalculate total installment balance pending after payment
            installmentPending = installment.getPendingInstallmentBalance();

            installment.setStatus(updateLoanPaymentStatus(installmentPending, installment.getInstallmentAmount()));

            installment.setPaid(installment.getStatus() == PaymentStatus.PAID);

            installment.setLastPaymentDate(now);
            LocalDate nextPaymentDate = getNextPaymentDateByLoanId(loan.getId());

            if (nextPaymentDate != null) {
                loan.setNextPaymentDate(nextPaymentDate);
            }

            BigDecimal totalPaid = outstandingPrincipalPayment.add(interestPayment);
            loan.setTotalInstallmentBalance(loan.getTotalInstallmentBalance().subtract(totalPaid));
            loan.setLastPaymentDate(now);

            AmortizationTableItem amortizationItem = _amortizationItemRepo.getReferenceById(installment.getAmortizationItemId().getId());
            amortizationItem.setPaid(installment.isPaid());
            amortizationItem.setPaidDate(installment.isPaid() ? now.toLocalDate() : null);

            _amortizationItemRepo.save(amortizationItem);
            //todo: update payments made and pending by amort item where isPaid and !isPaid
          /*  _loanRepository.save(loan);

            _loanPaymentRepository.save(installment);*/

            //todo: save updated amortizationItem

            log.info("Autopay completed loan: {} ", loan.getId());

            if (remainingBalance.compareTo(BigDecimal.ZERO) > 0) {
                //logic to do with remaining balance

            } else {
                break;
            }
        }


    }


    @Transactional
    public void payInstallment(LoanPayment installment) {
    }

    @Transactional
    public void payCharge(LoanCharge loanCharge) {
    }

    public BigDecimal calculateTotalDue(List<LoanPayment> dueInstallments, List<LoanCharge> dueCharges) {
        BigDecimal dueChargesAmount = dueCharges.stream()
                .map(LoanCharge::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal dueInstallmentsAmount = dueInstallments
                .stream()
                .map(LoanPayment::getPendingInstallmentBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return dueInstallmentsAmount.add(dueChargesAmount);
    }

    public LocalDate getNextPaymentDateByLoanId(Long loanId) {

        return _amortizationService.getAmortizationTableByLoanId(loanId).getItem()
                .stream()
                .filter(i -> !i.isPaid()) // solo cuotas no pagadas
                .min(Comparator.comparing(AmortizationTableItem::getPaymentDate)) // la más próxima
                .orElse(null)
                .getPaymentDate();
    }

    public PaymentStatus updateLoanPaymentStatus(BigDecimal installmentPending, BigDecimal installmentAmount) {
        if (installmentPending.compareTo(BigDecimal.ZERO) == 0) {
            return PaymentStatus.PAID;
        } else if (installmentPending.compareTo(BigDecimal.ZERO) > 0 && installmentPending.compareTo(installmentAmount) < 0) {
            return PaymentStatus.PARTIAL;
        }
        //Si el pendiente es igual al monto de la cuota - No se ha pagado nada
        return PaymentStatus.PENDING;
    }

    public List<LoanPayment> findOverDueInstallments(LocalDate date){
      return  _loanPaymentRepository.findOverDueInstallments(date);
    }
}


