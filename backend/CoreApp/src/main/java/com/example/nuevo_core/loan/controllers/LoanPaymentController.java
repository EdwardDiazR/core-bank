package com.example.nuevo_core.loan.controllers;

import com.example.nuevo_core.loan.entity.LoanPayment;
import com.example.nuevo_core.loan.interfaces.ILoanPaymentService;
import com.example.nuevo_core.loan.dto.loanPayment.PayLoanDto;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/loan-payment")
public class LoanPaymentController {

    private final ILoanPaymentService _paymentService;

    public LoanPaymentController(ILoanPaymentService paymentService) {
        _paymentService = paymentService;
    }

    @GetMapping("{id}")
    public ResponseEntity<List<LoanPayment>> getPendingInstallmentsByLoanId(@PathParam("id") Long loanId) {
        try {
            return ResponseEntity.ok(_paymentService.getDueInstallmentsByLoanId(loanId));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("pay")
    public ResponseEntity<String> payLoan(@RequestBody PayLoanDto payDto) {
        try {
            System.out.println(payDto);
            _paymentService.payLoan(payDto);
            return ResponseEntity.ok("Pago realizado correctamente");

        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("autopay")
    public ResponseEntity<String> autoPayBatch(@RequestParam("id") Long loanId){
        _paymentService.autoPayLoan(loanId);
        return ResponseEntity.ok("Autpay executed");
    }

    @PostMapping("generate-invoices")
    public ResponseEntity<String> generateInvoices(){
        _paymentService.generateLoanPaymentInvoices();
        return ResponseEntity.ok("Payments generated");
    }
}
