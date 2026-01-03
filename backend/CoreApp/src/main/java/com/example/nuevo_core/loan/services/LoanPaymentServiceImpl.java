package com.example.nuevo_core.loan.services;

import com.example.nuevo_core.loan.entity.LoanCharge;
import com.example.nuevo_core.account.interfaces.IAccountService;
import com.example.nuevo_core.loan.entity.LoanPayment;
import com.example.nuevo_core.loanAmortization.amortizationTable.IAmortizationService;
import com.example.nuevo_core.loanAmortization.amortizationTableItem.AmortizationTableItem;
import com.example.nuevo_core.loan.interfaces.ILoanService;
import com.example.nuevo_core.loan.entity.Loan;
import com.example.nuevo_core.loan.repository.LoanRepository;
import com.example.nuevo_core.loanAmortization.amortizationTableItem.IAmortizationTableItemRepository;
import com.example.nuevo_core.loan.interfaces.ILoanPaymentService;
import com.example.nuevo_core.loan.constants.PaymentStatus;
import com.example.nuevo_core.loan.dto.loanPayment.PayLoanDto;
import com.example.nuevo_core.loan.repository.LoanPaymentRepository;
import com.example.nuevo_core.transaction.constants.TransactionCategory;
import com.example.nuevo_core.transaction.constants.TransactionStatus;
import com.example.nuevo_core.transaction.constants.TransactionType;
import com.example.nuevo_core.transaction.dto.CreateAccountTxDto;
import com.example.nuevo_core.transaction.dto.CreateTransactionDto;
import com.example.nuevo_core.transaction.interfaces.ITransactionService;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
public class LoanPaymentServiceImpl implements ILoanPaymentService {

    private final ILoanService _loanService;
    private final IAmortizationService _amortizationService;
    private final IAmortizationTableItemRepository _amortizationItemRepo;
    private final LoanRepository _loanRepository;
    private final LoanPaymentRepository _loanPaymentRepository;
    private final IAccountService _accountService;
    private final ITransactionService _transactionService;

    public LoanPaymentServiceImpl(ILoanService loanService,
                                  IAmortizationService amortizationService,
                                  LoanRepository loanRepository,
                                  LoanPaymentRepository repo,
                                  IAmortizationTableItemRepository amortItemRepo,
                                  IAccountService accountService,
                                  ITransactionService transactionService) {
        _loanService = loanService;
        _amortizationService = amortizationService;
        _loanRepository = loanRepository;
        _loanPaymentRepository = repo;
        _amortizationItemRepo = amortItemRepo;
        _accountService = accountService;
        _transactionService = transactionService;
    }

    @Autowired
    EntityManager entityManager;

    @Override
    @Transactional
    public void generateLoanPaymentInvoices() {
        log.info("Generating Invoices");
        entityManager.clear();
        List<AmortizationTableItem> pendingInstallmentsForInvoicing = findPendingInstallmentsForInvoicing();
        List<LoanPayment> loanPaymentsInvoices = new ArrayList<>();

        for (AmortizationTableItem amortizationItem : pendingInstallmentsForInvoicing) {
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
                        .loan(amortizationItem.getAmortizationTable().getLoan())
                        .build();
                loanPaymentsInvoices.add(payment);
            }
        }
        if (!loanPaymentsInvoices.isEmpty()) {
            _loanPaymentRepository.saveAll(loanPaymentsInvoices);
        }
    }

    @Override
    public List<AmortizationTableItem> findPendingInstallmentsForInvoicing() {
        LocalDate today = LocalDate.now();
        LocalDate fiveDaysLater = today.plusDays(5);
        return _loanPaymentRepository.generateLoanPayments(today, fiveDaysLater);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanPayment> getDueInstallmentsByLoanId(Long loanId) {
        return _loanPaymentRepository.findPendingInstallmentsByLoanId(loanId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanCharge> getDueChargesByLoanId(Long loanId) {
        return new ArrayList<>();
    }

    public List<Long> getAllDueInstallmentsToAutopay() {
        LocalDateTime now = LocalDateTime.now();

        return _loanPaymentRepository.findLoansWithDueInstallmentsToAutopay(now.toLocalDate());
    }

    @Transactional
    public void payLoan(PayLoanDto payLoanDto) {
        Loan loan = _loanService.getLoanById(payLoanDto.loanId());
        List<LoanPayment> dueInstallments = getDueInstallmentsByLoanId(loan.getId());
        List<LoanCharge> dueCharges = getDueChargesByLoanId(loan.getId());

        processPayment(dueInstallments, dueCharges, loan, payLoanDto.amount(), payLoanDto.source(), null);
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
        Long referenceId = _transactionService.generateReferenceId();

        String debitTxDescription = String.format("Autopago Prest. %s",
                loan.getFinancialProduct().getProductNumber());

        CreateAccountTxDto accountDebitTx = new CreateAccountTxDto(
                debitTxDescription,
                amountToDebit,
                TransactionType.DEBIT,
                "BANK", // ejemplo: "CUENTA-1234"
                TransactionStatus.COMPLETED,
                TransactionCategory.PAYMENT,
                loan.getFinancialProduct(),
                referenceId,
                loan.getCurrency()
        );

        BigDecimal remainingBalance = _accountService.withdraw(
                loan.getLinkedAccount(),
                amountToDebit,
                accountDebitTx);

        if (remainingBalance.compareTo(BigDecimal.ZERO) > 0) {
            processPayment(dueInstallments, dueCharges, loan, remainingBalance, "AUTO PAGO", referenceId);
        }
    }

    public void processPayment(List<LoanPayment> installments,
                               List<LoanCharge> charges,
                               Loan loan,
                               BigDecimal amountToPay,
                               String paymentSource,
                               @Nullable Long transactionReferenceId) {
        try {
            LocalDateTime now = LocalDateTime.now();
            BigDecimal remainingBalance = amountToPay;
            BigDecimal totalDue = installments.stream()
                    .map(LoanPayment::getPendingInstallmentBalance)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            if (totalDue.compareTo(remainingBalance) < 0) {
                throw new IllegalArgumentException("Monto pagado no puede ser mayor al adeudado");
            }

            if(installments.isEmpty()){
                throw new IllegalArgumentException("No hay pagos pendientes");

            }

            //Pay charges first
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


            //Pay installments
            for (LoanPayment installment : installments) {
                //todo: resetear el reference id,
                // porque despues del primer loop
                // ya no es null e intenta usar
                // el mismo asignado anteriormente

                Long txReferenceId = transactionReferenceId != null && installments.size() == 1 ? transactionReferenceId :  _transactionService.generateReferenceId();
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


                BigDecimal totalPaid = outstandingPrincipalPayment.add(interestPayment);

                if (installmentPending.compareTo(BigDecimal.ZERO) == 0) {
                    if(loan.getPaymentsMade().compareTo(loan.getTermInMonths()) < 0) {
                        loan.setPaymentsMade(loan.getPaymentsMade() + 1);
                    }
                    if (loan.getPaymentsPending() > 0) {
                        loan.setPaymentsPending(loan.getPaymentsPending() - 1);
                    }

                }

                loan.setTotalInstallmentBalance(loan.getTotalInstallmentBalance().subtract(totalPaid));
                loan.setLastPaymentDate(now);

                AmortizationTableItem amortizationItem = _amortizationItemRepo.getReferenceById(installment.getAmortizationItemId().getId());
                amortizationItem.setPaid(installment.isPaid());
                amortizationItem.setPaidDate(installment.isPaid() ? now.toLocalDate() : null);


                //todo: save updated amortizationItem
                String LoanTransactionDesc = String.format("PAGO CUOTA %s",
                        installment.getDueDate()
                                .format(DateTimeFormatter
                                        .ofPattern("MMM-yy", new Locale("es", "DO"))));

                CreateTransactionDto tdto = new CreateTransactionDto(
                        LoanTransactionDesc.toUpperCase(),
                        totalPaid,
                        TransactionType.CREDIT,
                        "bank",
                        TransactionStatus.COMPLETED,
                        TransactionCategory.PAYMENT,
                        loan.getFinancialProduct(),
                        txReferenceId,
                        loan.getOutstandingPrincipalAmount(),
                        loan.getCurrency()
                );
                _amortizationItemRepo.save(amortizationItem);

                LocalDate nextPaymentDate = getNextPaymentDateByLoan(loan);

                if (nextPaymentDate != null) {
                    loan.setNextPaymentDate(nextPaymentDate);
                }else{
                    loan.setNextPaymentDate(loan.getDueDate());
                }
                //todo: update payments made and pending by amort item where isPaid and !isPaid
                _loanRepository.save(loan);
                _loanPaymentRepository.save(installment);
                _transactionService.createTransaction(tdto);
                log.info("Pago realizado correctamente Prest. No.: {} ", loan.getId());

                txReferenceId = null;

                if (remainingBalance.compareTo(BigDecimal.ZERO) > 0) {
                    //logic to do with remaining balance

                } else {
                    break;
                }
            }

        } catch (Exception e) {
            log.error("ROLLBACK CAUSE → {}", e.getClass().getName());
            log.error("MESSAGE → {}", e.getMessage());
            throw e;
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

    public LocalDate getNextPaymentDateByLoan(Loan loan) {

        return Objects.requireNonNull(_amortizationService.getAmortizationTableByLoan(loan).getItems()
                        .stream()
                        .filter(i -> !i.isPaid()) // solo cuotas no pagadas
                        .min(Comparator.comparing(AmortizationTableItem::getPaymentDate)) // la más próxima
                        .orElse(null))
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

    public List<LoanPayment> findOverDueInstallments(LocalDate date) {
        return _loanPaymentRepository.findOverDueInstallments(date);
    }
}


