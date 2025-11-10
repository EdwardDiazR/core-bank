package com.example.nuevo_core.loan.controllers;

import com.example.nuevo_core.loan.model.LoanPayment;
import com.example.nuevo_core.loan.interfaces.ILoanPaymentService;
import com.example.nuevo_core.loan.dto.loanPayment.PayLoanDto;
import jakarta.websocket.server.PathParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<String> payLoan(PayLoanDto payDto) {
        try {
            _paymentService.payLoan(payDto);
            return ResponseEntity.ok("Pago realizado correctamente");

        } catch (Exception e) {
            return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("autopay")
    public ResponseEntity<String> autoPayBatch(@RequestParam("id") Long loanId){
        _paymentService.autoPayLoan(loanId);
        return ResponseEntity.ok("Autpay executed");
    }
}
