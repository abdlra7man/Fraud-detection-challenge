package com.afterpay.frauddetector.domain.model;

import lombok.*;

/**
 * representation of a single credit card transaction
 * 
 * @author Abdelrahman
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardTransaction {
	private String hashCardNumber;
	private TransactionDetails transactionDetails;
}
