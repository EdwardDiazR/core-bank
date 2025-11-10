package com.example.nuevo_core.loan.repository;

import com.example.nuevo_core.loan.model.LoanCharge;
import com.example.nuevo_core.loan.model.LoanPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LoanChargeRepository extends JpaRepository<LoanCharge,Long> {

   /* @Query("SELECT p FROM LoanPayment p WHERE p.dueDate <= :date AND p.dueDate <= :date - :graceDays")
    List<LoanPayment> findOverduePayments(@Param("loanId") Long loanId,
                                          @Param("date") LocalDate date,
                                          @Param("graceDays") long graceDays);*/
}
