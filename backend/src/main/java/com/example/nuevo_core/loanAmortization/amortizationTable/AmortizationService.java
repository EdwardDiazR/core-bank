package com.example.nuevo_core.loanAmortization.amortizationTable;

import com.example.nuevo_core.loanAmortization.amortizationTable.repository.AmortizationTableRepository;
import com.example.nuevo_core.loanAmortization.amortizationTableItem.AmortizationTableItem;
import com.example.nuevo_core.loanAmortization.amortizationTableItem.IAmortizationTableItemRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AmortizationService implements IAmortizationService {
    private final AmortizationTableRepository _amortizationTableRepository;
    private final IAmortizationTableItemRepository _amortizationItemRepository;

    public AmortizationService(AmortizationTableRepository amortizationTableRepository,
                               IAmortizationTableItemRepository amortizationTableItemRepository) {

        _amortizationTableRepository = amortizationTableRepository;
        _amortizationItemRepository = amortizationTableItemRepository;
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public AmortizationTable generateAmortizationTable(Long loanId,
                                                       BigDecimal installmentAmount,
                                                       BigDecimal principalAmount,
                                                       BigDecimal interestRate,
                                                       int term,
                                                       int interestPeriodInMonths) {


        //FIRST CHECK IF LOAN ALREADY HAS AN AMORTIZATION TABLE
        if (_amortizationTableRepository.existsByLoanId(loanId)) {
            String msj = "Este prestamo tiene amortizacion " +loanId;
            throw new RuntimeException(msj);
        }

        //Save table in db, then add items with insert batch
        AmortizationTable table = AmortizationTable.builder()
                .loanId(loanId)
                .isActive(true)
                .build();

        entityManager.persist(table);

        BigDecimal cuota = installmentAmount;

        //todo: Initial date is when the loan is disbursed and already have a paymentDate
        LocalDate initialDate = LocalDate.of(2025, 9, 20);

        List<AmortizationTableItem> payments = new ArrayList<AmortizationTableItem>();

        BigDecimal balance = principalAmount;

        int batchSize = 50;

        for (int paymentNumber = 1; paymentNumber <= term; paymentNumber++) {

            BigDecimal interest = balance
                    .multiply(interestRate)
                    .divide(new BigDecimal(12), 2, RoundingMode.HALF_UP);


            BigDecimal capitalPagado = cuota
                    .subtract(interest);

            balance = balance.subtract(capitalPagado);

            initialDate = initialDate.plusMonths(1);

            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            AmortizationTableItem item = AmortizationTableItem.builder()
                    .cuota(cuota)
                    .capital(capitalPagado)
                    .interes(interest)
                    .saldo(balance)
                    .installmentNumber(paymentNumber)
                    .isPaid(false)
                    .paidDate(null)
                    .paymentDate(initialDate)
                    .charges(new BigDecimal(0))
                    .amortizationTable(table)
                    .build();

            //payments.add(item); --Sustituir este por usar insert batch

            // Persistimos el item
            entityManager.persist(item);

            // Cada 50 inserts, vaciamos el contexto de persistencia
            if (paymentNumber % batchSize == 0) {
                entityManager.flush(); // fuerza los inserts pendientes
                entityManager.clear(); // limpia el contexto y evita usar demasiada memoria
            }
        }

        entityManager.flush(); // fuerza los inserts pendientes
        entityManager.clear();

        return table;
    }


    public void saveAmortizationTable(AmortizationTable table) {
        _amortizationTableRepository.save(table);

    }

    public AmortizationTable getAmortizationTableByLoanId(Long loanId) {
        return _amortizationTableRepository.findByLoanId(loanId);
    }

    public Optional<AmortizationTableItem> getAmortizationTableItemById(Long id){
      return   _amortizationItemRepository.findById(id);
    }
}
