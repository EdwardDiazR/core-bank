package com.example.nuevo_core.loan.dto.loan;

import com.example.nuevo_core.utils.BooleanToNumberConverter;
import jakarta.persistence.Convert;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteLoanDto {
    private long id;

    @Convert(converter = BooleanToNumberConverter.class)
    private Boolean isDeleted;


}
