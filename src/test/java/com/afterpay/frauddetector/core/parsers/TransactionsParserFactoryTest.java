package com.afterpay.frauddetector.core.parsers;

import com.afterpay.frauddetector.core.parsers.impl.CSVFileTransactionsParser;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TransactionsParserFactoryTest {

    @Test
    public void testGetTransactionsParserWithSupportedParser(){
        TransactionsParser parser = TransactionsParserFactory.getTransactionsParser(ParserType.FILE_PARSER);
        assertTrue(parser instanceof  CSVFileTransactionsParser);
    }

    @Test(expected = RuntimeException.class)
    public void testGetTransactionsParserWithUnsupportedParser(){
        TransactionsParserFactory.getTransactionsParser(ParserType.valueOf("anyparser"));
    }
}
