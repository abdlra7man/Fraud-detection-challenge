package com.afterpay.frauddetector.domain.model;

import com.univocity.parsers.annotations.Nested;
import com.univocity.parsers.annotations.Parsed;
import com.univocity.parsers.annotations.Trim;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardTransaction {
	@Trim
	@Parsed(index = 0)
	private String hashCardNumber;

	@Nested
	private TransactionDetails transactionDetails;
}
