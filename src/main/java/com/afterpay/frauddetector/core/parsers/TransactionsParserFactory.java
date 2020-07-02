package com.afterpay.frauddetector.core.parsers;

import com.afterpay.frauddetector.core.parsers.impl.CSVFileTransactionsParser;

public class TransactionsParserFactory {
    public static TransactionsParser getFileTransactionsParser(ParserType parserType) {
        if(ParserType.FILE_PARSER.equals(parserType)){
            return new CSVFileTransactionsParser();
        }
        throw new RuntimeException("Unsupported parser type");
    }
}
