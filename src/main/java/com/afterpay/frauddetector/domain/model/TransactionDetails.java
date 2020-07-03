package com.afterpay.frauddetector.domain.model;

import com.afterpay.frauddetector.utils.LocalDateFormatter;
import com.univocity.parsers.annotations.Convert;
import com.univocity.parsers.annotations.Parsed;
import com.univocity.parsers.annotations.Trim;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class TransactionDetails {
    @Trim
    @Parsed(index = 2)
    private double amount;
    @Trim
    @Parsed(index = 1)
    @Convert(conversionClass = LocalDateFormatter.class)
    private LocalDateTime date;
}
