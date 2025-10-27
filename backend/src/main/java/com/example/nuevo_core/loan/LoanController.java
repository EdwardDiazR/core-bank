package com.example.nuevo_core.loan;


import com.example.nuevo_core.constants.loans.LoanInterestPeriod;
import com.example.nuevo_core.loan.dto.CreateLoanDto;
import com.example.nuevo_core.loan.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/loan")
public class LoanController {

    private final ILoanService _loanService;

    @Autowired
    LoanRepository repo;

    public LoanController(ILoanService loanService) {
        _loanService = loanService;
    }

    @PostMapping()
    public ResponseEntity<Loan> createLoan(@RequestBody CreateLoanDto loanDto) {

        Loan loan = _loanService.createLoan(loanDto);
        return ResponseEntity.ok(loan);
    }

    @GetMapping()
    public ResponseEntity<Loan> getLoanById(@RequestParam long id) {
        Loan loan = _loanService.getLoanById(id);

        try {
            return ResponseEntity.ok(loan);
        } catch (Exception e) {

            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping()
    private ResponseEntity deleteLoan(@RequestParam long id) {
        try {

            _loanService.deleteLoanById(id);
            return ResponseEntity.ok("Loan deleted successfully");

        } catch (Exception e) {
            return ResponseEntity.ofNullable(e.getMessage());
        }
    }
}
