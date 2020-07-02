package com.afterpay.frauddetector.core;

import com.afterpay.frauddetector.domain.dto.FraudDetectionDTO;
import com.afterpay.frauddetector.domain.model.CardTransaction;
import com.afterpay.frauddetector.domain.model.TransactionDetails;
import com.google.common.collect.Lists;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 
 * @author Abdelrahman
 *
 */
public class FraudDetector {

	public List<String> detectFraud(FraudDetectionDTO fraudDetectionDTO) {
		Map<String, List<TransactionDetails>> cardsMap =  buildCardTransactionsMap(fraudDetectionDTO.getTransactionList());
		List<String> fraudList = scanForFraudulentTransactions(cardsMap, fraudDetectionDTO.getAmountThreshold());
		return fraudList;
	}

	List<String> scanForFraudulentTransactions(Map<String, List<TransactionDetails>> cardsMap, double amountThreshold) {
		List<String> fraudItems = new ArrayList<>();
		cardsMap.forEach( (cardNumber, transactionsList) -> {
					List<List<TransactionDetails>> allTransactionsWithinWindow = groupBySlidingWidnow(transactionsList);
					List<Double> totalsPerSlidingWindow = reduceToSlidingWindowTotals(allTransactionsWithinWindow);
					List windowTotalsMoreThanThreshold = totalsPerSlidingWindow.stream().
							filter( total -> total > amountThreshold).collect(Collectors.toList());
					if(windowTotalsMoreThanThreshold.size() > 0){
						fraudItems.add(cardNumber);
					}
				});
		return fraudItems;
	}

	List<Double> reduceToSlidingWindowTotals(List<List<TransactionDetails>> all) {
		List<Double> totalPerSlidingWindow = all.stream().map(l -> l.stream()
				.map(TransactionDetails::getAmount)
				.reduce(Double::sum)
				.get()
		).collect(Collectors.toList());
		return totalPerSlidingWindow;
	}

	List<List<TransactionDetails>> groupBySlidingWidnow(List<TransactionDetails> data) {
		LinkedList<List<TransactionDetails>> allTransactionsByWindows = new LinkedList<>(Arrays.asList(new ArrayList<>()));
		data.forEach(transaction -> {
				if (!allTransactionsByWindows.getLast().isEmpty()) {
					// check if in 24h boundary of the first item of the last list
					LocalDateTime windowUpperValue = allTransactionsByWindows.getLast().get(0).getDate().plusHours(24);
					//if not within the 24 boundary of the first item of the last list, then create a new list
					// for this transaction and add to it all the transactions within its past 24 hours
					if (!windowUpperValue.isAfter(transaction.getDate())) {
						// create copy with row older earlier than 24h
						LocalDateTime lowerValue = transaction.getDate().minusHours(24);
						allTransactionsByWindows.add(new ArrayList<>(allTransactionsByWindows.getLast().stream()
								.filter(r -> lowerValue.isBefore(r.getDate()))
								.collect(Collectors.toList())));
					}
				}
				allTransactionsByWindows.getLast().add(transaction);
		});
		return allTransactionsByWindows;
	}

	Map<String, List<TransactionDetails>> buildCardTransactionsMap(List<CardTransaction> transactionList) {
		Map<String, List<TransactionDetails>> cardsMap = new HashMap<>();
		transactionList.stream().forEach(cardTransaction -> {
			String cardNumber = cardTransaction.getHashCardNumber();
			TransactionDetails dateAndAmount = cardTransaction.getTransactionDetails();
			if(cardsMap.get(cardNumber) != null){
				List<TransactionDetails> transactionsDetails = cardsMap.get(cardNumber);
				transactionsDetails.add(dateAndAmount);
			} else {
				cardsMap.put(cardNumber, Lists.newArrayList(dateAndAmount));
			}
		});
		return cardsMap;
	}
}
