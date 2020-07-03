package com.afterpay.frauddetector.core.parsers.impl;

import com.afterpay.frauddetector.domain.model.CardTransaction;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class CSVFileTransactionsParserTest {
    private static final String TEST_FILE_PATH = "src/test/resources/transactionslist.csv";

    @Test
    public void testParseFromSource(){
        CSVFileTransactionsParser csvFileTransactionsParser = new CSVFileTransactionsParser();
        List<CardTransaction> transactions = csvFileTransactionsParser.parseFromSource(TEST_FILE_PATH);
        assertEquals(35, transactions.size());
    }
}
