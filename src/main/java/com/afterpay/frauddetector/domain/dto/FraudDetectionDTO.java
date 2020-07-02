package com.afterpay.frauddetector.domain.dto;

import com.afterpay.frauddetector.domain.model.CardTransaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FraudDetectionDTO {
    List<CardTransaction> transactionList;
    double amountThreshold;
}
