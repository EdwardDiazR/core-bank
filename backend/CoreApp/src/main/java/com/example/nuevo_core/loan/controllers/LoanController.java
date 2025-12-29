package com.example.nuevo_core.loan.controllers;


import com.example.nuevo_core.loan.dto.loan.LoanDTO;
import com.example.nuevo_core.loan.exceptions.LoanNotFoundException;
import com.example.nuevo_core.loan.interfaces.ILoanService;
import com.example.nuevo_core.loan.entity.Loan;
import com.example.nuevo_core.loan.dto.loan.CreateLoanDto;
import com.example.nuevo_core.loan.repository.LoanRepository;
import com.example.nuevo_core.loan.interfaces.ILoanPaymentService;
import com.example.nuevo_core.utils.ApiResponse;
import com.example.nuevo_core.utils.ErrorResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;


@Slf4j
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

    @GetMapping("/search/{loanNumber}")
    public ResponseEntity<ApiResponse> getLoanByNumber(@PathVariable("loanNumber") String number) {
        ApiResponse response;
        try {
            LoanDTO loan = _loanService.getLoanByProductNumber(number);
            response = new ApiResponse(
                    true,
                    "Prestamo consultado exitosamente",
                    HttpStatus.OK.value(),
                    loan,
                    LocalDateTime.now()

            );

            return ResponseEntity.ok(response);
        } catch (LoanNotFoundException notFoundException) {
            /*ErrorResponseDTO error = new ErrorResponseDTO(HttpStatus.NOT_FOUND.value(),
                    notFoundException.getMessage(),
                   );*/

            response = new ApiResponse(false,
                    notFoundException.getMessage(),
                    HttpStatus.NOT_FOUND.value(),
                    null,
                    LocalDateTime.now()
            );

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            log.error(e.getMessage());
            response = new ApiResponse(false,
                    "Ha ocurrido un error",
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null,
                    LocalDateTime.now()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{loanPublicId}")
    public ResponseEntity<ApiResponse> getLoanByPublicId(@PathVariable("loanPublicId") String loanPublicId) {
        ApiResponse response;
        try {
            LoanDTO loan = _loanService.getLoanByPublicId(loanPublicId);
            response = new ApiResponse(
                    true,
                    "Prestamo consultado exitosamente",
                    HttpStatus.OK.value(),
                    loan,
                    LocalDateTime.now()

            );

            return ResponseEntity.ok(response);
        } catch (LoanNotFoundException notFoundException) {
            /*ErrorResponseDTO error = new ErrorResponseDTO(HttpStatus.NOT_FOUND.value(),
                    notFoundException.getMessage(),
                   );*/

            response = new ApiResponse(false,
                    notFoundException.getMessage(),
                    HttpStatus.NOT_FOUND.value(),
                    null,
                    LocalDateTime.now()
            );

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            log.error(e.getMessage());
            response = new ApiResponse(false,
                    "Ha ocurrido un error",
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null,
                    LocalDateTime.now()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }



    @PostMapping()
    public ResponseEntity<Object> createLoan(@RequestBody CreateLoanDto loanDto) {
        try {
            Loan loan = _loanService.createLoan(loanDto);
            return ResponseEntity.created(URI.create("api/loan/" + loan.getId())).body(loan);
        } catch (Exception e) {

            ErrorResponseDTO error = new ErrorResponseDTO(HttpStatus.BAD_REQUEST.value(),
                    e.getMessage(),
                    LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
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

   /* @PutMapping("/update-interest-rate")
    public ResponseEntity<String> updateInterestRate(@RequestBody Long loanId,
                                                     @RequestBody BigDecimal newInterestRate) {
        return ResponseEntity.ok("Updated");
    }*/

  /*  @PutMapping("/change-payment-date")
    public ResponseEntity<String> changePaymentDate(@RequestBody Long loanId,
                                                    @RequestBody int dayOfPayment) {
        return ResponseEntity.ok("PaymentDate changed");
    }*/

    @PostMapping("/generate-payment-invoices")
    public ResponseEntity<String> generatePendingPayments() {
        try {
            _loanPaymentService.generateLoanPaymentInvoices();
            return ResponseEntity.ok("Generated corresponding Invoices");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

   /* @GetMapping("/amortization-table")
    public ResponseEntity<AmortizationTable> getAmortizationTable(@RequestParam String loanNumber) {

        var loan = _loanService.getLoanByProductNumber(loanNumber);

        return ResponseEntity.ok(loan.amortizationTable());
    }*/

}
