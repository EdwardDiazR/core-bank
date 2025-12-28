package com.example.nuevo_core.transaction.service;

import com.example.nuevo_core.transaction.constants.TransactionType;
import com.example.nuevo_core.transaction.dto.CreateTransactionDto;
import com.example.nuevo_core.transaction.interfaces.ITransactionService;
import com.example.nuevo_core.transaction.model.Transaction;
import com.example.nuevo_core.transaction.repository.TransactionRepository;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TransactionService implements ITransactionService {
    private final EntityManager entityManager;
    private final TransactionRepository _repository;
    public TransactionService(TransactionRepository repository, EntityManager entityManager){
        _repository = repository;
        this.entityManager = entityManager;
    }
    public Long generateReferenceId (){
        return ((Number) entityManager
                .createNativeQuery("SELECT " + "transaction_reference_seq" + ".NEXTVAL FROM dual\n")
                .getSingleResult())
                .longValue();
    }
    @Transactional
    public void createTransaction(CreateTransactionDto createTransactionDto) {


        boolean isInTransit = false;
        BigDecimal finalBalanceAfter;

        if(createTransactionDto.type() == TransactionType.DEBIT){
            finalBalanceAfter = createTransactionDto.balanceAfter().abs().negate();
        }else{
            finalBalanceAfter = createTransactionDto.balanceAfter();
        }

        Transaction transaction = Transaction.builder()
                .amount(createTransactionDto.amount())
                .type(createTransactionDto.type())
                .category(createTransactionDto.category())
                .description(createTransactionDto.description())
                .balanceAfter(finalBalanceAfter)
                .status(createTransactionDto.status())
                .entryDate(LocalDateTime.now())
                .effectiveDate(isInTransit ? null : LocalDateTime.now())
                .channel(createTransactionDto.channel())
                .currency(createTransactionDto.currency())
                .financialProduct(createTransactionDto.financialProduct())
                .referenceId(createTransactionDto.referenceId()) //Temporal
                .build();
              _repository.save(transaction);
    }
}
