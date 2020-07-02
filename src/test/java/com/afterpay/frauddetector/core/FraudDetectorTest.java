package com.afterpay.frauddetector.core;

import com.afterpay.frauddetector.domain.dto.FraudDetectionDTO;
import com.afterpay.frauddetector.domain.model.CardTransaction;
import com.afterpay.frauddetector.domain.model.TransactionDetails;
import com.google.common.collect.Lists;
import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FraudDetectorTest {
    private static final String FRAUDULANT_CARD = "1234567";

    @Test
    public void testDetectFraud() {
        TransactionDetails transactionDetails = TransactionDetails.builder()
                .date(LocalDateTime.parse("2020-01-01T12:00:00"))
                .amount(200.0)
                .build();
        CardTransaction cardTransaction = CardTransaction.builder()
        .hashCardNumber("xyz12345abc")
        .transactionDetails(transactionDetails)
        .build();

        List<CardTransaction> transactionList = Lists.newArrayList(cardTransaction);
        FraudDetector fraudDetector = new FraudDetector();
        FraudDetectionDTO fraudDetectionDTO = FraudDetectionDTO.builder()
                .transactionList(transactionList)
                .amountThreshold(100).build();
        List<String> fraudList = fraudDetector.detectFraud(fraudDetectionDTO);
        assertEquals("Fraud list should contains 1 item", 1, fraudList.size());
    }

    @Test
    public void testScanForFraudulentTransactions(){
        FraudDetector fraudDetector = new FraudDetector();
        Map<String, List<TransactionDetails>> cardsMap = generateCardsMap();
        List<String> fraudulentCards = fraudDetector.scanForFraudulentTransactions(cardsMap, 45.0);
        assertEquals(1, fraudulentCards.size());
        assertEquals(FRAUDULANT_CARD, fraudulentCards.get(0));
    }
    @Test
    public void testGroupBySlidingWidnow(){
        FraudDetector fraudDetector = new FraudDetector();
        List<TransactionDetails> transactions = getTransactionDetails();
        List<List<TransactionDetails>> collectedTrans = fraudDetector.groupBySlidingWidnow(transactions);
        assertEquals(2, collectedTrans.size());
        assertEquals(3, collectedTrans.get(0).size());
        assertEquals(3, collectedTrans.get(1).size());
    }

    @Test
    public void testReduceToSlidingWindowTotals () {
        FraudDetector fraudDetector = new FraudDetector();
        List<List<TransactionDetails>> transactions = getSlidingWindowsTransactionDetails();
        List<Double> totals = fraudDetector.reduceToSlidingWindowTotals(transactions);
        assertEquals(45.0, totals.get(0).doubleValue(), 0.1);
        assertEquals(46.0, totals.get(1).doubleValue(), 0.1);
    }

    @Test
    public void testBuildCardTransactionsMap (){
        FraudDetector fraudDetector = new FraudDetector();
        List<CardTransaction> cardTransactions = generateCardTransactionsList();
        Map<String, List<TransactionDetails>> cardTransactionsMap = fraudDetector.buildCardTransactionsMap(cardTransactions);
        assertEquals(3, cardTransactionsMap.keySet().size());
        assertEquals(2, cardTransactionsMap.get("12345678abcdef").size());
    }


    private Map<String, List<TransactionDetails>> generateCardsMap() {
        Map<String, List<TransactionDetails>> cardsMap = new HashMap<>();
        List<TransactionDetails> transactionDetails = getTransactionDetails();
        cardsMap.put(FRAUDULANT_CARD, transactionDetails);
        cardsMap.put("abcdefgh", Lists.newArrayList(transactionDetails.get(0)));
        cardsMap.put("abcdefgh1234", Lists.newArrayList(transactionDetails.get(1)));
        return cardsMap;
    }

    private List<CardTransaction> generateCardTransactionsList() {
        CardTransaction cardTransaction1 = CardTransaction.builder()
                .hashCardNumber("12345678abcdef")
                .transactionDetails(getTransactionDetails().get(0))
                .build();

        CardTransaction cardTransaction2 = CardTransaction.builder()
                .hashCardNumber("21345678abcdef")
                .transactionDetails(getTransactionDetails().get(1))
                .build();

        CardTransaction cardTransaction3 = CardTransaction.builder()
                .hashCardNumber("12345678abcdef")
                .transactionDetails(getTransactionDetails().get(2))
                .build();

        CardTransaction cardTransaction4 = CardTransaction.builder()
                .hashCardNumber("32145678abcdef")
                .transactionDetails(getTransactionDetails().get(3))
                .build();
        return Lists.newArrayList(cardTransaction1, cardTransaction2, cardTransaction3, cardTransaction4);
    }

    private List<List<TransactionDetails>> getSlidingWindowsTransactionDetails() {
        List<TransactionDetails> allTransactions = getTransactionDetails();
        List<TransactionDetails> firstSlidingWindowTrans = Lists.newArrayList( allTransactions.get(0),
                allTransactions.get(1),
                allTransactions.get(2));

        List<TransactionDetails> secondSlidingWindowTrans = Lists.newArrayList( allTransactions.get(1),
                allTransactions.get(2),
                allTransactions.get(3));
        return Lists.newArrayList(firstSlidingWindowTrans, secondSlidingWindowTrans);
    }

    private List<TransactionDetails> getTransactionDetails() {
        TransactionDetails transaction1 = TransactionDetails.builder()
                .amount(14)
                .date(LocalDateTime.parse("2019-08-04T10:11:30"))
                .build();
        TransactionDetails transaction2 = TransactionDetails.builder()
                .amount(16)
                .date(LocalDateTime.parse("2019-08-04T11:11:30"))
                .build();
        TransactionDetails transaction3 = TransactionDetails.builder()
                .amount(15)
                .date(LocalDateTime.parse("2019-08-04T19:11:30"))
                .build();
        TransactionDetails transaction4 = TransactionDetails.builder()
                .amount(15)
                .date(LocalDateTime.parse("2019-08-05T10:11:30"))
                .build();
        return Lists.newArrayList(transaction1, transaction2, transaction3, transaction4);
    }
}
