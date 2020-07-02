package com.afterpay.frauddetector.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * representation of a single credit card transaction
 * 
 * @author Abdelrahman
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardTransaction {
	private String hashCardNumber;
	private double transactionAmount;
	private LocalDateTime transactionDate;
}
