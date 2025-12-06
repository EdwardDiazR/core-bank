package com.example.nuevo_core.account.dto.responsesDto;

import com.example.nuevo_core.account.entity.Account;
import org.apache.logging.log4j.message.Message;

public record CreateAccountResponse(Account account,
                                    String message) {
}
