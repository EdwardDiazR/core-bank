package com.example.nuevo_core.financialProduct;

import com.example.nuevo_core.financialProduct.dto.SearchFinancialProductResponse;
import com.example.nuevo_core.financialProduct.interfaces.FinancialProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("api/financial-product")
@CrossOrigin(origins = "http://localhost:3000", methods = {RequestMethod.GET, RequestMethod.POST,RequestMethod.DELETE},allowCredentials = "true")

public class FinancialProductController {
    private final FinancialProductService _financialProductService;

    public FinancialProductController(FinancialProductService financialProductService){

        _financialProductService = financialProductService;
    }

    @GetMapping("/{productNumber}")
    public ResponseEntity<Set<SearchFinancialProductResponse>> getFinancialProductByNumber(@PathVariable String productNumber){
        var response = _financialProductService.findFinancialProductByNumber(productNumber);
        return ResponseEntity.ok(response);

    }
}
