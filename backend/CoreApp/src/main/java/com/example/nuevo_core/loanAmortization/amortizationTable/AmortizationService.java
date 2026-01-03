package com.example.nuevo_core.loanAmortization.amortizationTable;

import com.example.nuevo_core.loan.entity.Loan;
import com.example.nuevo_core.loan.interfaces.ILoanService;
import com.example.nuevo_core.loan.repository.LoanRepository;
import com.example.nuevo_core.loanAmortization.amortizationTable.dto.AmortizationTableDTO;
import com.example.nuevo_core.loanAmortization.amortizationTable.repository.AmortizationTableRepository;
import com.example.nuevo_core.loanAmortization.amortizationTableItem.AmortizationTableItem;
import com.example.nuevo_core.loanAmortization.amortizationTableItem.IAmortizationTableItemRepository;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.openpdf.text.Chunk;
import org.openpdf.text.Document;
import org.openpdf.text.PageSize;
import org.openpdf.text.Paragraph;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AmortizationService implements IAmortizationService {

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private LoanRepository loanRepository;

    private final AmortizationTableRepository _amortizationTableRepository;
    private final IAmortizationTableItemRepository _amortizationItemRepository;
    private final ILoanService _loanService;
    //provisional

    public AmortizationService(AmortizationTableRepository amortizationTableRepository,
                               IAmortizationTableItemRepository amortizationTableItemRepository,
                               ILoanService loanService
    ) {

        _amortizationTableRepository = amortizationTableRepository;
        _amortizationItemRepository = amortizationTableItemRepository;
        _loanService = loanService;
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public AmortizationTable generateAmortizationTable(Long loanId,
                                                       BigDecimal installmentAmount,
                                                       BigDecimal principalAmount,
                                                       BigDecimal interestRate,
                                                       int term,
                                                       int interestPeriodInMonths, LocalDate firstPaymentDate) {


        //FIRST CHECK IF LOAN ALREADY HAS AN AMORTIZATION TABLE

        Loan loan = _loanService.getLoanById(loanId);
        if (_amortizationTableRepository.existsByLoan(loan)) {
            String msj = "Este prestamo tiene amortizacion " + loanId;
            throw new RuntimeException(msj);
        }

        //Save table in db, then add items with insert batch
        AmortizationTable table = AmortizationTable.builder()
                .loan(loan)
                .isActive(true)
                .build();

        entityManager.persist(table);

        BigDecimal cuota = installmentAmount;

        //todo: Initial date is when the loan is disbursed and already have a paymentDate
        LocalDate initialDate =  firstPaymentDate;

        List<AmortizationTableItem> payments = new ArrayList<AmortizationTableItem>();

        BigDecimal balance = principalAmount;

        int batchSize = 50;

        for (int paymentNumber = 1; paymentNumber <= term; paymentNumber++) {

            BigDecimal interest = balance
                    .multiply(interestRate)
                    .divide(new BigDecimal(12), 2, RoundingMode.HALF_UP);


            BigDecimal capitalPagado;

            if (paymentNumber == term) {
                //Ajustar la ultima cuota para que el saldo quede en 0
                cuota = balance.add(interest);
                capitalPagado = balance;
                balance = balance.subtract(capitalPagado);

                //payments.add(item); --Sustituir este por usar insert batch
                if (balance.abs().compareTo(new BigDecimal("0.01")) > 0) {
                    throw new IllegalStateException("Error de amortización");
                }
            } else {
                //De lo contrario restarle al saldo el monto pagado de capital
                capitalPagado = cuota
                        .subtract(interest);
                balance = balance.subtract(capitalPagado);

            }

            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");


            AmortizationTableItem item = AmortizationTableItem.builder()
                    .cuota(cuota)
                    .capital(capitalPagado)
                    .interes(interest)
                    .saldo(balance)
                    .installmentNumber(paymentNumber)
                    .isPaid(false)
                    .paidDate(null)
                    .paymentDate(initialDate)
                    .charges(new BigDecimal(0))
                    .amortizationTable(table)
                    .build();

            initialDate = initialDate.plusMonths(1);

            // Persistimos el item
            entityManager.persist(item);

            // Cada 50 inserts, vaciamos el contexto de persistencia
            if (paymentNumber % batchSize == 0) {
                entityManager.flush(); // fuerza los inserts pendientes
                entityManager.clear(); // limpia el contexto y evita usar demasiada memoria
            }
        }
        table.setItems(payments);
        entityManager.flush(); // fuerza los inserts pendientes
        entityManager.clear();

        //Ajustar el prestamo a que su primera fecha de pago sea la primera fecha de la amortizacion

        return table;
    }


    public void saveAmortizationTable(AmortizationTable table) {
        _amortizationTableRepository.save(table);

    }

    public AmortizationTable getAmortizationTableByLoan(Loan loanId) {
        return _amortizationTableRepository.findByLoan(loanId);
    }

    public Optional<AmortizationTableItem> getAmortizationTableItemById(Long id) {
        return _amortizationItemRepository.findById(id);
    }

    public byte[] generatePdfTable(AmortizationTableDTO tableDTO) {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        Context ctx = new Context();

        ctx.setVariable("loanNumber", tableDTO.loanNumber());
        ctx.setVariable("rows", tableDTO.items());
        ctx.setVariable("currency", "DOP");
        ctx.setVariable("fecha", date.format(formatter));

        String html = templateEngine.process("pdf/amortization", ctx);
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {

            String baseUri = Objects.requireNonNull(
                    getClass()
                            .getClassLoader()
                            .getResource("templates/pdf/")
            ).toExternalForm();

            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, baseUri);
            builder.toStream(os);
            builder.run();

            return os.toByteArray();
        } catch (Exception e) {
            throw new IllegalStateException("PDF generation failed", e);
        }
    }

}
