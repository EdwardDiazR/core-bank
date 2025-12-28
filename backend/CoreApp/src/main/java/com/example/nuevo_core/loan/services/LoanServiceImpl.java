package com.example.nuevo_core.loan.services;


import com.example.nuevo_core.financialProduct.dto.CreateFinancialProductDTO;
import com.example.nuevo_core.financialProduct.dto.relative.RelativeDTO;
import com.example.nuevo_core.financialProduct.entity.FinancialProduct;
import com.example.nuevo_core.financialProduct.constants.ProductType;
import com.example.nuevo_core.financialProduct.interfaces.FinancialProductService;
import com.example.nuevo_core.loan.dto.loan.*;
import com.example.nuevo_core.loan.entity.LoanPayment;
import com.example.nuevo_core.loan.exceptions.LoanNotFoundException;
import com.example.nuevo_core.loan.interfaces.ILoanService;
import com.example.nuevo_core.loan.entity.Loan;
import com.example.nuevo_core.loanAmortization.amortizationTable.AmortizationTable;
import com.example.nuevo_core.loanAmortization.amortizationTable.dto.AmortizationTableDTO;
import com.example.nuevo_core.loanAmortization.amortizationTableItem.AmortizationTableItem;
import com.example.nuevo_core.loanAmortization.amortizationTable.IAmortizationService;
import com.example.nuevo_core.constants.loans.LoanInterestPeriod;
import com.example.nuevo_core.constants.loans.PaymentFrequency;
import com.example.nuevo_core.loan.constants.LoanStatus;
import com.example.nuevo_core.loan.exceptions.LoanAlreadyDeletedException;
import com.example.nuevo_core.loan.repository.LoanRepository;
import com.example.nuevo_core.transaction.dto.TransactionDTO;
import com.example.nuevo_core.transaction.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class LoanServiceImpl implements ILoanService {

    private static final Logger log = LoggerFactory.getLogger(LoanServiceImpl.class);
    private final LocalDateTime now = LocalDateTime.now();

    private final IAmortizationService _amortizationService;
    private final LoanRepository loanRepository;

    private final FinancialProductService _financialProductService;
    private final LoanPaymentServiceImpl _loanPaymentService;


    public LoanServiceImpl(@Lazy IAmortizationService amortizationService,
                           LoanRepository repository,
                           FinancialProductService financialProductService,
                           @Lazy LoanPaymentServiceImpl loanPaymentService) {
        loanRepository = repository;
        _amortizationService = amortizationService;
        _financialProductService = financialProductService;
        _loanPaymentService = loanPaymentService;
    }

    public Loan getLoanById(Long loanId) {
        Loan loan = loanRepository.findById(loanId).orElseThrow(() -> new LoanNotFoundException("Prestamo no existe"));
        return loan;
    }

    @Transactional(readOnly = true)
    public LoanDTO getLoanByProductNumber(String loanNumber) {

        Loan loan = loanRepository.findByFinancialProduct_ProductNumber(loanNumber)
                .orElseThrow(() -> new LoanNotFoundException("Prestamo No." + loanNumber + " no existe"));

        List<LoanPayment> pendingInstallments = _loanPaymentService.getDueInstallmentsByLoanId(loan.getId());

        //System.out.println(pendingInstallments);
        BigDecimal roundedInterestRate = loan.getInterestRate()
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);

        loan.setInterestRate(roundedInterestRate);

        Set<RelativeDTO> relatives = loan.getFinancialProduct()
                .getRelatives()
                .stream()
                .map(r -> {
                    return new RelativeDTO(r.getCustomerId(), r.getRelationCondition().toString());
                }).collect(Collectors.toSet());

        AmortizationTableDTO amortizationTableDTO = new AmortizationTableDTO(loan.getFinancialProduct().getProductNumber(), loan.getAmortizationTable().getItems());

        if (loan.getAmortizationTable().getItems().isEmpty()) {
            amortizationTableDTO = null;
        }

        List<TransactionDTO> transactions = loan.getFinancialProduct()
                .getTransactions()
                .stream()
                .map(t ->
                        new TransactionDTO(t.getAmount(),
                                t.getBalanceAfter(),
                                t.getType(),
                                t.getDescription(),
                                t.getReferenceId()))
                .toList();

        LoanDTO loanDto = LoanDTO.builder()
                .number(loan.getFinancialProduct().getProductNumber())
                .currency(loan.getCurrency())
                .status(loan.getStatus())
                .termInMonths(loan.getTermInMonths())
                .paymentsMade(loan.getPaymentsMade())
                .paymentsPending(loan.getPaymentsPending())
                .principalAmount(loan.getPrincipalAmount())
                .outstandingPrincipalBalance(loan.getOutstandingPrincipalAmount())
                .availableAmountForDisbursement(loan.getAvailableAmountForDisbursement())
                .interestBalance(loan.getInterestBalance())
                .interestRate(loan.getInterestRate())
                .installmentAmount(loan.getInstallmentAmount())
                .transactions(transactions)
                .pendingInstallments(pendingInstallments)
                .amortizationTable(amortizationTableDTO)
                .nextPaymentDate(loan.getNextPaymentDate())
                .lastPaymentDate(loan.getLastPaymentDate())
                .dueDate(loan.getDueDate())
                .lastInterestRateReviewDate(loan.getLastInterestRateReviewDate())
                .relatives(relatives)
                .build();

        return loanDto;
    }

    @Transactional()
    public Loan createLoan(CreateLoanDto loanDto) {

        BigDecimal disbursementAmount = loanDto.amount()
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal amount = loanDto.amount()
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal interestRate = loanDto.interestRate()
                .divide(BigDecimal.valueOf(100), 10, RoundingMode.UNNECESSARY);

        BigDecimal cuota = calculateCuota(amount, interestRate, loanDto.termInMonths())
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal projectedInterest = calculateProjectedInterest(loanDto.amount(), interestRate, loanDto.termInMonths(), loanDto.interestPeriodFrequency());

        BigDecimal dailyInterestFactor = calculateDailyInterestFactor(amount, interestRate, loanDto.termInMonths())
                .setScale(2, RoundingMode.HALF_UP);

        LocalDate loanDueDate = now.toLocalDate()
                .plusMonths(loanDto.termInMonths());


        float LATE_FEE_RATE = (float) 6 / 100;


        FinancialProduct fp = _financialProductService.createFinancialProduct(
                new CreateFinancialProductDTO(ProductType.LOAN, loanDto.relatives(), loanDto.signType())
        );

        Loan loan = Loan.builder()
                .financialProduct(fp)
                .status(LoanStatus.APPROVED)
                .type(loanDto.type())
                .currency(loanDto.currency())//In future can validate by loan type
                .principalAmount(disbursementAmount)
                .availableAmountForDisbursement(BigDecimal.ZERO)
                .outstandingPrincipalAmount(disbursementAmount)
                .interestBalance(BigDecimal.ZERO)
                .interestRate(interestRate)
                .termInMonths(loanDto.termInMonths())
                .paymentFrequency(PaymentFrequency.MONTHLY)
                .dailyInterestFactor(dailyInterestFactor)
                .installmentAmount(cuota)
                .lateFeeRate(new BigDecimal(LATE_FEE_RATE))
                .lateFeeBalance(new BigDecimal(0))
                .totalInstallmentBalance(cuota)
                .totalPaidInterest(BigDecimal.ZERO)
                .projectedInterest(projectedInterest.setScale(2, RoundingMode.HALF_UP))
                .oneCycleTimes(0)
                .twoCycleTimes(0)
                .paymentsMade(0)
                .paymentsPending(loanDto.termInMonths())
                .firstPaymentDate(null)
                .nextPaymentDate(null)
                .lastPaymentDate(null)
                .lastInterestBalanceUpdateDate(null)
                .disbursementDate(null)
                .lastInterestRateReviewDate(now)
                .dueDate(loanDueDate)
                .updatedAt(null)
                .linkedAccount(null)
                .canAutoDebit(false)
                .isLineOfCredit(false)
                .isDeleted(false)
                .build();

        // loan.getFinancialProduct().addRelatives(loanDto.relateds());
        loanRepository.save(loan);


        //todo: if is line of credit, set available amount for disbursement, after check if revol is active

        AmortizationTable amortizationTable = _amortizationService.generateAmortizationTable(
                loan.getId(),
                cuota,
                loanDto.amount(),
                interestRate,
                loanDto.termInMonths(), 12);

        loan.setAmortizationTable(amortizationTable);

        log.info("loan created: loanId={}", loan.getId());
        return loan;

    }

    public void deleteLoanById(Long loanId) {
        DeleteLoanDto loan = loanRepository.getLoanToDelete(loanId);

        if (loan.getIsDeleted()) {
            throw new LoanAlreadyDeletedException("Loan is already deleted");
        } else {
            loanRepository.markLoanAsDeleted(loanId);
        }
    }

    public BigDecimal calculateCuota(BigDecimal capital,
                                     BigDecimal tasa,
                                     int meses
    ) {

        BigDecimal monthlyRate = tasa.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);

        // (1 + r)^n
        BigDecimal onePlusR = BigDecimal.ONE.add(monthlyRate);
        BigDecimal factor = onePlusR.pow(meses);

        // r * (1 + r)^n
        BigDecimal numerator = monthlyRate.multiply(factor);

        // (1 + r)^n - 1
        BigDecimal denominator = factor.subtract(BigDecimal.ONE);

        // Cuota = P * (numerator / denominator)
        BigDecimal cuota = capital.multiply(numerator)
                .divide(denominator, 2, RoundingMode.HALF_UP);

        return cuota.setScale(2, RoundingMode.HALF_UP);


    }

    public BigDecimal calculateDailyInterestFactor(BigDecimal amount,
                                                   BigDecimal interestRate,
                                                   int term
    ) {
        //TODO: in future, validate to calculate yearly or monthly depending of the loan
        int YEAR_BASE_DAYS = 360;
        BigDecimal dailyFactorInPercentage = interestRate
                .divide(BigDecimal.valueOf(YEAR_BASE_DAYS), 10, RoundingMode.HALF_UP);

        return amount
                .multiply(dailyFactorInPercentage)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private int getInterestPeriodFrequencyInNumber(LoanInterestPeriod interestPeriodFrequency) {
        switch (interestPeriodFrequency) {
            case MONTHLY -> {
                return 1;
            }
            default -> {
                return 12;
            }
        }
    }


    public BigDecimal calculateProjectedInterest(BigDecimal amount,
                                                 BigDecimal interestRate,
                                                 int term,
                                                 LoanInterestPeriod period
    ) {


        var termInYears = BigDecimal.valueOf(term / getInterestPeriodFrequencyInNumber(period));
        var i = amount
                .multiply(interestRate)
                .multiply(termInYears);

        return i;


    }

    private List<String> getLoanRelateds(long id) {
        List<String> related = new ArrayList<>();

        related.add("MANUEL");
        related.add("JUANA");
        return related;
    }

    public void payCuota(Loan loan) {
        //Search account to debit
        List<AmortizationTableItem> pagosPendientes = loan.getAmortizationTable()
                .getItems()
                .stream()
                .filter(c -> !c.isPaid() && c.getPaymentDate().isBefore(now.toLocalDate()))
                .toList();

        System.out.println("Pagos: Pendientes");

        for (AmortizationTableItem n : pagosPendientes) {
            System.out.printf("cuota:%s int:%-10.2f capita:%-10.2f saldo:%-10.2f%n fecha: %s%n",
                    NumberFormat.getInstance().format(n.getCuota()), n.getInteres(), n.getCapital(), n.getSaldo(), n.getPaymentDate());
        }

        BigDecimal sumaCapital = pagosPendientes.stream()
                .map(AmortizationTableItem::getCuota)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal sumaInteres = pagosPendientes.stream()
                .map(AmortizationTableItem::getInteres)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        System.out.printf(" Suma Capital:%-10.2f Suma interes:%-10.2f", sumaCapital, sumaInteres);
    }

    public void calculateMora() {
        int day = 1;
//        String dayName = switch (day){case 1 -> "Lunes";};
    }

    public AdminLoanDto getLoanDetailsToAdmin(Long loanId) {
        return loanRepository.getLoanDetailsToAdmin(loanId);
    }

    public void RecalculateCuota() {
    }


}
