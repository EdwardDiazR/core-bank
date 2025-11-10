package com.example.nuevo_core.customer.controller;

import com.example.nuevo_core.customer.model.Customer;
import com.example.nuevo_core.customer.dto.CustomerGeneralView;
import com.example.nuevo_core.customer.dto.FinancialAccounts;
import com.example.nuevo_core.customer.dto.GetCustomerByNationalIdDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("api/v1/customer")
public class CustomerController {
    public CustomerController() {
    }

    @GetMapping("nationalId/{id}")
    public ResponseEntity<CustomerGeneralView> getCustomerByNationalId(@RequestParam GetCustomerByNationalIdDto dto) {
        Customer customer;
        FinancialAccounts financialAccounts;
        CustomerGeneralView generalView = null;

        return ResponseEntity.ok(generalView);
    }

    @GetMapping("c/{customerCode}")
    public ResponseEntity<CustomerGeneralView> getCustomerByCustomerCode(@RequestParam Long customerCode) {
        Customer customer;
        FinancialAccounts financialAccounts;
        CustomerGeneralView generalView = null;

        return ResponseEntity.ok(generalView);
    }

    @PostMapping()
    public ResponseEntity<Customer> createCustomer() {
        Customer c = null;
        URI created = URI.create("api/cutomer/c/" + c.getId());
        return ResponseEntity.created(created).body(c);
    }
}
