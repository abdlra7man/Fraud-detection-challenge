package com.afterpay.frauddetector.core.detectors;

import com.afterpay.frauddetector.core.exception.ValidationException;
import com.afterpay.frauddetector.domain.dto.FraudDetectionDTO;
import com.afterpay.frauddetector.domain.model.CardTransaction;
import com.afterpay.frauddetector.domain.model.TransactionDetails;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


public class SlidingWindowScannerFraudDetectorTest {
    private static final String FRAUDULENT_CARD = "1234567";
    private List<TransactionDetails> transactionDetails;
    private Map<String, List<TransactionDetails>> cardsMap;
    private List<CardTransaction> cardTransactions;
    private List<List<TransactionDetails>> transactions;
    private SlidingWindowScannerFraudDetector slidingWindowScannerFraudDetector;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void setup(){
        cardTransactions = generateCardTransactionsList();
        transactionDetails = getTransactionDetails();
        cardsMap = generateCardsMap();
        transactions = getSlidingWindowsTransactionDetails();
        slidingWindowScannerFraudDetector = Mockito.spy(new SlidingWindowScannerFraudDetector());
    }

    @Test
    public void testDetectFraud() {
        FraudDetectionDTO fraudDetectionDTO = FraudDetectionDTO.builder()
                .transactionList(cardTransactions)
                .amountThreshold(25.0).build();
        List<String> fraudList = slidingWindowScannerFraudDetector.detectFraud(fraudDetectionDTO);

        verify(slidingWindowScannerFraudDetector).buildCardTransactionsMap(cardTransactions);
        verify(slidingWindowScannerFraudDetector).scanForFraudulentTransactions(anyMap(), eq(25.0));
        assertEquals( 1, fraudList.size());
        assertEquals("12345678abcdef", fraudList.get(0));
    }

    @Test
    public void testDetectFraudWithInvalidThresholdValue(){
        exceptionRule.expect(ValidationException.class);
        exceptionRule.expectMessage("Threshold must be greater than 0");
        FraudDetectionDTO fraudDetectionDTO = FraudDetectionDTO.builder()
                .transactionList(cardTransactions)
                .amountThreshold(0).build();
        slidingWindowScannerFraudDetector.detectFraud(fraudDetectionDTO);
    }

    @Test
    public void testDetectFraudWithNoTransactions(){
        exceptionRule.expect(ValidationException.class);
        exceptionRule.expectMessage("No Transactions to validate against");
        FraudDetectionDTO fraudDetectionDTO = FraudDetectionDTO.builder()
                .transactionList(null)
                .amountThreshold(50).build();
        slidingWindowScannerFraudDetector.detectFraud(fraudDetectionDTO);
    }

    @Test
    public void testDetectFraudWithNullDTO(){
        exceptionRule.expect(ValidationException.class);
        exceptionRule.expectMessage("FraudDetectionDTO can not be null");
        slidingWindowScannerFraudDetector.detectFraud(null);
    }

    @Test
    public void testScanForFraudulentTransactions(){
        List<String> fraudulentCards = slidingWindowScannerFraudDetector.scanForFraudulentTransactions(cardsMap, 45.0);
        assertEquals(1, fraudulentCards.size());
        assertEquals(FRAUDULENT_CARD, fraudulentCards.get(0));
    }
    @Test
    public void testGroupBySlidingWidnow(){
        List<List<TransactionDetails>> collectedTrans = slidingWindowScannerFraudDetector.groupBySlidingWidnow(transactionDetails);
        assertEquals(2, collectedTrans.size());
        assertEquals(3, collectedTrans.get(0).size());
        assertEquals(3, collectedTrans.get(1).size());
    }

    @Test
    public void testReduceToSlidingWindowTotals () {
        List<Double> totals = slidingWindowScannerFraudDetector.reduceToSlidingWindowTotals(transactions);
        assertEquals(45.0, totals.get(0).doubleValue(), 0.1);
        assertEquals(46.0, totals.get(1).doubleValue(), 0.1);
    }

    @Test
    public void testBuildCardTransactionsMap (){
        Map<String, List<TransactionDetails>> cardTransactionsMap = slidingWindowScannerFraudDetector.buildCardTransactionsMap(cardTransactions);
        assertEquals(3, cardTransactionsMap.keySet().size());
        assertEquals(2, cardTransactionsMap.get("12345678abcdef").size());
    }


    private Map<String, List<TransactionDetails>> generateCardsMap() {
        Map<String, List<TransactionDetails>> cardsMap = new HashMap<>();
        List<TransactionDetails> transactionDetails = getTransactionDetails();
        cardsMap.put(FRAUDULENT_CARD, transactionDetails);
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
