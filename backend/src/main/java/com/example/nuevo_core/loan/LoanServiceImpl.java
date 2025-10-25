package com.example.nuevo_core.loan;

import com.example.nuevo_core.constants.loans.LoanInterestPeriod;
import com.example.nuevo_core.constants.loans.PaymentStatus;
import com.example.nuevo_core.loan.dto.CreateLoanDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LoanServiceImpl implements ILoanService {
    private final int YEAR_BASE_DAYS = 360;
    private final LocalDateTime now = LocalDateTime.now();

    public LoanServiceImpl() {
    }

    public Loan getLoanById(long id) {
        Loan loan = createLoan(new CreateLoanDto(250000,
                10f,
                12,
                LoanInterestPeriod.ANNUAL
        ));

        payCuota(loan);
        return loan;
    }

    public Loan createLoan(CreateLoanDto loanDto) {

        float interestRate = loanDto.interestRate() / 100;
        int interestPeriodInMonths = getInterestPeriod(loanDto.interestPeriodFrequency());

        BigDecimal cuota = new BigDecimal(calculateCuota(loanDto.amount(), interestRate / interestPeriodInMonths, loanDto.termInMonths()))
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal projectedInterest = calculateProjectedInterest(loanDto.amount(), interestRate, loanDto.termInMonths(), loanDto.interestPeriodFrequency());

        double dailyInterestFactor = calculateDailyInterestFactor(loanDto.amount(), interestRate, loanDto.termInMonths(), loanDto.interestPeriodFrequency());

        AmortizationTable amortizationTable = generateAmortizationTable(loanDto.amount(), interestRate, loanDto.termInMonths());

        List<String> related = new ArrayList<>();

        related.add("MANUEL");
        related.add("JUANA");

        Loan loan = Loan.builder()
                .id(895437847)
                .status(PaymentStatus.NORMAL.toString())
                .type("PRESTAMO HIPOTECARIO")
                .disbursementAmount(loanDto.amount())
                .montoCuota(Double.parseDouble(cuota.toString()))
                .dailyInterestFactor(dailyInterestFactor)
                .interestRate(interestRate)
                .projectedInterest(Float.parseFloat(projectedInterest.toString()))
                .term(loanDto.termInMonths())
                .oneCycleTimes(0)
                .twoCycleTimes(0)
                .capitalBalance(loanDto.amount())
                .totalPaidInterest(0)
                .disbursementDate(null)
                .nextPaymentDate(null)
                .createdAt(now)
                .interestBalance(0)
                .relateds(related)
                .amortizationTable(amortizationTable)
                .build();

        return loan;

    }


    public double calculateCuota(double capital,
                                 double tasa,
                                 int meses) {
        // Fórmula: C = P * [i(1+i)^n] / [(1+i)^n - 1]
        double factor = Math.pow(1 + tasa, meses);
        double cuota = capital * (tasa * factor) / (factor - 1);
        return cuota;
    }

    public AmortizationTable generateAmortizationTable(double amount,
                                                       float interestRate,
                                                       int term) {
        double capital = amount;
        double tasa = interestRate / 12;
        int meses = term;
        double cuota = calculateCuota(capital, tasa, meses);

        LocalDate initialDate = LocalDate.of(2025, 8, 10);

        List<AmortizationTableItem> payments = new ArrayList<AmortizationTableItem>();

        for (int mes = 1; mes <= term; mes++) {
            double interes = amount * interestRate / 12;
            double capitalPagado = cuota - interes;
            amount -= capitalPagado;
            initialDate = initialDate.plusMonths(1);
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String formatedDate = formato.format(initialDate);

            System.out.printf("%-5d fecha: %s  cuota:%-10.2f int:%-10.2f capita:%-10.2f saldo:%-10.2f%n",
                    mes, formatedDate, cuota, interes, capitalPagado, amount);

            AmortizationTableItem item = AmortizationTableItem.builder()
                    .reference(UUID.randomUUID())
                    .saldo(new BigDecimal(amount).setScale(2, RoundingMode.HALF_UP))
                    .capital(new BigDecimal(capitalPagado).setScale(2, RoundingMode.HALF_UP))
                    .cuota(new BigDecimal(cuota).setScale(2, RoundingMode.HALF_UP))
                    .interes(new BigDecimal(interes).setScale(2, RoundingMode.HALF_UP))
                    .paymentNumber(mes)
                    .isPaid(false)
                    .paidDate(null)
                    .paymentDate(initialDate)
                    .build();

            payments.add(item);
        }

        AmortizationTable table = AmortizationTable.builder()
                .loanId(8494939)
                .id(3995395)
                .item(payments)
                .build();


        return table;

    }

    public double calculateDailyInterestFactor(double amount,
                                               float interestRate,
                                               int term,
                                               LoanInterestPeriod period) {

        BigDecimal dailyInterestAmount = new BigDecimal((amount * interestRate) / (period.equals(LoanInterestPeriod.ANNUAL) ? YEAR_BASE_DAYS : 30))
                .setScale(2, RoundingMode.HALF_UP);

        return Double.parseDouble(dailyInterestAmount.toString());
    }

    private int getInterestPeriod(LoanInterestPeriod interestPeriodFrequency) {
        switch (interestPeriodFrequency) {
            case MONTHLY -> {
                return 1;
            }
            default -> {
                return 12;
            }
        }
    }


    public BigDecimal calculateProjectedInterest(double amount,
                                                 float interestRate,
                                                 int term,
                                                 LoanInterestPeriod period) {

        int periodInMonths;
        switch (period) {
            case ANNUAL -> periodInMonths = 12;
            case MONTHLY -> periodInMonths = 1;
            default -> periodInMonths = 12;
        }
        var convertedInterestRateToPercentual = (interestRate) / periodInMonths;
        return new BigDecimal(amount * convertedInterestRateToPercentual * term)
                .setScale(2, RoundingMode.HALF_UP);
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
                  NumberFormat.getInstance().format(n.getCuota()), n.getInteres(), n.getCapital(), n.getSaldo(),n.getPaymentDate());
        }

        BigDecimal sumaCapital = pagosPendientes.stream()
                .map(AmortizationTableItem::getCuota)
                .reduce(BigDecimal.ZERO,BigDecimal::add);

        BigDecimal sumaInteres = pagosPendientes.stream()
                .map(AmortizationTableItem::getInteres)
                .reduce(BigDecimal.ZERO,BigDecimal::add);

        System.out.printf(" Suma Capital:%-10.2f Suma interes:%-10.2f",sumaCapital,sumaInteres);
    }

    public void calculateMora() {
    }

    public void sumDailyFactorToInterestBalance(long id) {
        Loan loan = getLoanById(id);
        double interestBalance = loan.getInterestBalance();
        double dailyInterestFactor = loan.getDailyInterestFactor();
        loan.setInterestBalance(interestBalance += dailyInterestFactor);
    }
}
