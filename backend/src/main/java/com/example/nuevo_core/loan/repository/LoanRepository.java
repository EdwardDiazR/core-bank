package com.example.nuevo_core.loan.repository;

import com.example.nuevo_core.loan.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface LoanRepository extends JpaRepository<Loan, Long> {
    Optional<Loan> findById(Long id);

    @Query("SELECT l FROM Loan l WHERE l.id = :id AND l.isDeleted = false")
    Optional<Loan> findByIdAndIsDeletedFalse(@Param("id") Long id);
}
