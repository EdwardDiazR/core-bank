package com.example.nuevo_core.transaction.interfaces;

import com.example.nuevo_core.transaction.dto.CreateTransactionDto;

public interface ITransactionService {
    void createTransaction(CreateTransactionDto transactionDto);

    Long generateReferenceId();
}
