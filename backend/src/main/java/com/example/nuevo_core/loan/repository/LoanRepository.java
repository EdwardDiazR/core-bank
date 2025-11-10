package com.example.nuevo_core.loan.repository;

import com.example.nuevo_core.loan.model.Loan;
import com.example.nuevo_core.loan.dto.loan.AdminLoanDto;
import com.example.nuevo_core.loan.dto.loan.DeleteLoanDto;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface LoanRepository extends JpaRepository<Loan, Long> {


    @Query("""
            SELECT new com.example.nuevo_core.loan.dto.loan.DeleteLoanDto(l.id, l.isDeleted)
            FROM Loan l
            WHERE l.id = :id
            """)
    DeleteLoanDto getLoanToDelete(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("""
            UPDATE Loan l SET l.isDeleted = true  WHERE l.id = :id
            """)
    void markLoanAsDeleted(@Param("id") long id);

    @Query("""
            SELECT new com.example.nuevo_core.loan.dto.loan.AdminLoanDto(l.id,
            l.installmentAmount,
            l.nextPaymentDate)
            FROM Loan l WHERE l.id=:id
            """)
    AdminLoanDto getLoanDetailsToAdmin(@Param("id") Long loanId);

    @Query("""
            SELECT l
            FROM Loan l WHERE l.status IN
            ('ACTIVE','DELINQUENT','RESTRUCTURED')
             AND l.outstandingPrincipalAmount > 0
            """)
    List<Loan> getLoansByStatusNormal();

    //String getLoanRelateds(@Param("id") Long loanId);
}
