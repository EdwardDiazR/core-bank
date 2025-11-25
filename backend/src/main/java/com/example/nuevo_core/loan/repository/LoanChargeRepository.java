package com.example.nuevo_core.loan.repository;

import com.example.nuevo_core.loan.entity.LoanCharge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanChargeRepository extends JpaRepository<LoanCharge,Long> {

   /* @Query("SELECT p FROM LoanPayment p WHERE p.dueDate <= :date AND p.dueDate <= :date - :graceDays")
    List<LoanPayment> findOverduePayments(@Param("loanId") Long loanId,
                                          @Param("date") LocalDate date,
                                          @Param("graceDays") long graceDays);*/
}
