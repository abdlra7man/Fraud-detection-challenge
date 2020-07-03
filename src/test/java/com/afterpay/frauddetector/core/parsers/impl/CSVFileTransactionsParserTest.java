package com.afterpay.frauddetector.core.parsers.impl;

import com.afterpay.frauddetector.core.exception.ValidationException;
import com.afterpay.frauddetector.domain.model.CardTransaction;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.*;

import java.util.List;

public class CSVFileTransactionsParserTest {
    private static final String TEST_FILE_PATH = "src/test/resources/transactions_sample.csv";
    private static final String MALFORMATED_FILE_PATH = "src/test/resources/transactions_malformated.csv";

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testParseFromSource(){
        CSVFileTransactionsParser csvFileTransactionsParser = new CSVFileTransactionsParser();
        List<CardTransaction> transactions = csvFileTransactionsParser.parseFromSource(TEST_FILE_PATH);
        assertEquals(35, transactions.size());
    }

    @Test
    public void testParseMalformated(){
        exceptionRule.expect(ValidationException.class);
        exceptionRule.expectMessage("Incorrect file format, please verify data is formatted correctly");
        CSVFileTransactionsParser csvFileTransactionsParser = new CSVFileTransactionsParser();
        csvFileTransactionsParser.parseFromSource(MALFORMATED_FILE_PATH);
    }
}
