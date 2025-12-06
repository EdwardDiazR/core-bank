package com.example.nuevo_core.transaction.service;

import com.example.nuevo_core.transaction.dto.CreateTransactionDto;
import com.example.nuevo_core.transaction.interfaces.ITransactionService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class TransactionService implements ITransactionService {

    @Transactional
    public void createTransaction(CreateTransactionDto createTransactionDto) {
    }


}
