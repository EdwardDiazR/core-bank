package com.example.nuevo_core.loan.controllers;


import com.example.nuevo_core.loan.interfaces.ILoanService;
import com.example.nuevo_core.loan.model.Loan;
import com.example.nuevo_core.loanAmortization.amortizationTable.AmortizationTable;
import com.example.nuevo_core.loan.dto.loan.CreateLoanDto;
import com.example.nuevo_core.loan.repository.LoanRepository;
import com.example.nuevo_core.loan.interfaces.ILoanPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


@RestController
@RequestMapping("api/v1/loan")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class LoanController {

    private final ILoanService _loanService;
    private final ILoanPaymentService _loanPaymentService;

    @Autowired
    LoanRepository repo;

    public LoanController(ILoanService loanService, ILoanPaymentService loanPaymentService) {
        _loanService = loanService;
        _loanPaymentService = loanPaymentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Loan> getLoanById(@PathVariable("id") Long id) {
        Loan loan = _loanService.getLoanById(id);

        try {
            return ResponseEntity.ok(loan);
        } catch (Exception e) {

            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping()
    public ResponseEntity<Loan> createLoan(@RequestBody CreateLoanDto loanDto) {

        Loan loan = _loanService.createLoan(loanDto);
        return ResponseEntity.ok(loan);
    }

    @DeleteMapping()
    private ResponseEntity<String> deleteLoanById(@RequestParam Long id) {
        try {
            _loanService.deleteLoanById(id);
            return ResponseEntity.ok("Loan deleted successfully");

        } catch (Exception e) {
            return ResponseEntity.ofNullable(e.getMessage());
        }
    }

    @PutMapping("/update-interest-rate")
    public ResponseEntity<String> updateInterestRate(@RequestBody Long loanId,
                                                     @RequestBody BigDecimal newInterestRate) {
        return ResponseEntity.ok("Updated");
    }

    @PutMapping("/change-payment-date")
    public ResponseEntity<String> changePaymentDate(@RequestBody Long loanId,
                                                    @RequestBody int dayOfPayment) {
        return ResponseEntity.ok("PaymentDate changed");
    }

    @PostMapping("/generate-payment-invoices")
    public ResponseEntity<String> generatePendingPayments() {
        try {
            _loanPaymentService.generateLoanPaymentInvoices();
            return ResponseEntity.ok("Generated corresponding Invoices");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/amortization-table")
    public ResponseEntity<AmortizationTable> getAmortizationTable(@RequestParam Long loanId) {

        Loan loan = _loanService.getLoanById(loanId);

        return ResponseEntity.ok(loan.getAmortizationTable());
    }


}
