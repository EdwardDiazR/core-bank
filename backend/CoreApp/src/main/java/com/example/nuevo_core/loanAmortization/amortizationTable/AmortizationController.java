package com.example.nuevo_core.loanAmortization.amortizationTable;

import com.example.nuevo_core.loan.interfaces.ILoanService;
import com.example.nuevo_core.loanAmortization.amortizationTable.dto.AmortizationTableDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loan/amortization")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE})

public class AmortizationController {
    private final IAmortizationService _amortizationService;
    private final ILoanService _loanService;

    public AmortizationController(IAmortizationService amortizationService, ILoanService loanService) {
        _amortizationService = amortizationService;
        _loanService = loanService;
    }

    @GetMapping(value = "{loanPublicId}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generateAmortizationTable(@PathVariable String loanPublicId
    ) throws Exception {
        try {

            AmortizationTableDTO table = _loanService.getLoanByPublicId(loanPublicId).getAmortizationTable(); //todo: get loan Id
            //Temporalmente devuelvo el loan completo
            System.out.println(table.loanNumber());
            byte[] pdf = _amortizationService.generatePdfTable(table);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=amortization.pdf")
                    .body(pdf);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
