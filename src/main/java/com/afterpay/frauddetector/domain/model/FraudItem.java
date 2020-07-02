package com.afterpay.frauddetector.domain.model;

import lombok.*;

import java.time.LocalDate;

/**
 * representation of a detected Fraud
 *
 * @author Abdelrahman
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FraudItem {

	private String hashCardNumber;
	private double totalTransactionAmount;
	private LocalDate fraudDate;
}
