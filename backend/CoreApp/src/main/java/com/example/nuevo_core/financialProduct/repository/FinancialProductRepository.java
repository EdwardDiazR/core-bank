package com.example.nuevo_core.financialProduct.repository;

import com.example.nuevo_core.financialProduct.dto.SearchFinancialProductResponse;
import com.example.nuevo_core.financialProduct.entity.FinancialProduct;
import com.example.nuevo_core.loan.dto.loan.AdminLoanDto;
import com.example.nuevo_core.loan.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface FinancialProductRepository extends JpaRepository<FinancialProduct, Long> {
     FinancialProduct getByProductNumber(String productNumber);

    @Query("""
            SELECT new com.example.nuevo_core.financialProduct.dto.SearchFinancialProductResponse(
            fp.productNumber,
            fp.publicId,
            fp.productType)
            FROM FinancialProduct fp WHERE fp.productNumber=:number
            """)
    Set<SearchFinancialProductResponse> getFinancialProductSearchResult(@Param("number") String number);
}
