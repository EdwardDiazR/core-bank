package com.example.nuevo_core.loan;


import com.example.nuevo_core.amortizationTable.AmortizationTable;
import com.example.nuevo_core.amortizationTable.AmortizationTableItem;
import com.example.nuevo_core.constants.loans.LoanInterestPeriod;
import com.example.nuevo_core.constants.loans.PaymentStatus;
import com.example.nuevo_core.loan.dto.CreateLoanDto;
import com.example.nuevo_core.loan.repository.LoanRepository;
import org.hibernate.annotations.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class LoanServiceImpl implements ILoanService {
    private final int YEAR_BASE_DAYS = 360;
    private final int MONTH_BASE_DAYS = 30;
    private final LocalDateTime now = LocalDateTime.now();


    @Autowired
    private LoanRepository loanRepository;

    public LoanServiceImpl() {
    }

    public Loan getLoanById(Long id) {
        Loan loan = loanRepository.findById(id).orElseThrow();

        BigDecimal roundedInterestRate = loan.getInterestRate()
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);

        loan.setInterestRate(roundedInterestRate);

        return loan;
    }

    public Loan createLoan(CreateLoanDto loanDto) {

        BigDecimal disbursementAmount = loanDto.amount()
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal amount = loanDto.amount()
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal interestRate = loanDto.interestRate()
                .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);

        int interestPeriodInMonths = getInterestPeriodFrequencyInNumber(loanDto.interestPeriodFrequency());//12

        BigDecimal cuota = calculateCuota(amount, interestRate, loanDto.termInMonths())
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal projectedInterest = calculateProjectedInterest(loanDto.amount(), interestRate, loanDto.termInMonths(), loanDto.interestPeriodFrequency());

        BigDecimal dailyInterestFactor =
                calculateDailyInterestFactor(amount, interestRate, loanDto.termInMonths())
                        .setScale(2, RoundingMode.HALF_UP);
        Loan loan = Loan.builder()
                .status(PaymentStatus.NORMAL.toString())
                .type(loanDto.type())
                .disbursementAmount(disbursementAmount)
                .montoCuota(cuota)
                .dailyInterestFactor(dailyInterestFactor)
                .interestRate(interestRate)
                .projectedInterest(projectedInterest.setScale(2, RoundingMode.HALF_UP))
                .term(loanDto.termInMonths())
                .oneCycleTimes(0)
                .twoCycleTimes(0)
                .capitalBalance(disbursementAmount)
                .totalPaidInterest(BigDecimal.ZERO)
                .disbursementDate(null)
                .nextPaymentDate(null)
                .createdAt(now)
                .interestBalance(BigDecimal.ZERO)
                .relateds(loanDto.relateds())
                .availableForDisbursement(BigDecimal.ZERO)
                .isLineOfCredit(false)
                .currency(loanDto.currency())//In future can validate by loan type
                .build();

        loanRepository.save(loan);

        AmortizationTable amortizationTable =
                generateAmortizationTable(loan.getId(), loanDto.amount(), interestRate, loanDto.termInMonths(), 12);
        loan.setAmortizationTable(amortizationTable);

        return loan;

    }

    public void deleteLoanById(Long loanId) {
        Loan loan = loanRepository.findByIdAndIsDeletedFalse(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found or deleted"));

        loan.setIsDeleted(true);
        loanRepository.save(loan);

        System.out.println(loan.getId());
        System.out.println(loan.getIsDeleted());

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

    public AmortizationTable generateAmortizationTable(Long loanId,
                                                       BigDecimal capital,
                                                       BigDecimal interestRate,
                                                       int term,
                                                       int interestPeriodInMonths) {


        //FIRST CHECK IF LOAN ALREADY HAS AN AMORTIZATION TABLE
        BigDecimal cuota = calculateCuota(capital, interestRate, term);

        //todo: Initial date is when the loan is disbursed and already have a paymentDate
        LocalDate initialDate = LocalDate.of(2025, 10, 20);

        List<AmortizationTableItem> payments = new ArrayList<AmortizationTableItem>();

        BigDecimal balance = capital;

        for (int mes = 1; mes <= term; mes++) {
            BigDecimal interes = balance
                    .multiply(interestRate)
                    .divide(new BigDecimal(12), 2, RoundingMode.HALF_UP);


            BigDecimal capitalPagado = cuota
                    .subtract(interes);

            balance = balance.subtract(capitalPagado);

            initialDate = initialDate.plusMonths(1);

            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            /*System.out.printf("%-5d fecha: %s  cuota:%-10.2f int:%-10.2f capita:%-10.2f saldo:%-10.2f%n",
                    mes, formato.format(initialDate), cuota, interes, capitalPagado, balance);*/

            AmortizationTableItem item = AmortizationTableItem.builder()
                    .reference(UUID.randomUUID())
                    .cuota(cuota)
                    .capital(capitalPagado)
                    .interes(interes)
                    .saldo(balance)
                    .paymentNumber(mes)
                    .isPaid(false)
                    .paidDate(null)
                    .paymentDate(initialDate)
                    .build();

            payments.add(item);
        }

        AmortizationTable table = AmortizationTable.builder()
                .id(3995395)
                .loanId(loanId != null ? loanId : 0)
                .item(payments)
                .build();


        return table;

    }

    public BigDecimal calculateDailyInterestFactor(BigDecimal amount,
                                                   BigDecimal interestRate,
                                                   int term
    ) {
        //TODO: in future, validate to calculate yearly or monthly depending of the loan
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
                .getItem()
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
    }

    public void sumDailyFactorToInterestBalance(long id) {
        Loan loan = getLoanById(id);
        BigDecimal interestBalance = loan.getInterestBalance();
        BigDecimal dailyInterestFactor = loan.getDailyInterestFactor();

        loan.setInterestBalance(interestBalance.add(dailyInterestFactor));
    }
}
