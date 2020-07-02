package com.afterpay.frauddetector;

import com.afterpay.frauddetector.core.FraudDetector;
import com.afterpay.frauddetector.core.parsers.ParserType;
import com.afterpay.frauddetector.core.parsers.TransactionsParser;
import com.afterpay.frauddetector.core.parsers.TransactionsParserFactory;
import com.afterpay.frauddetector.domain.dto.FraudDetectionDTO;
import com.afterpay.frauddetector.domain.model.CardTransaction;

import java.util.List;

public class Main {
    public static void main(String [] args){
        if (args.length != 2) {
            System.err.println("Invalid number of arguments. usage java -jar fraud-detector-0.1.0.jar threshold filename.csv ");                
            System.exit(1);
        }
        processInput(args[0], args[1]);
    }

    private static void processInput(String thresholdAmount, String fileName) {
        TransactionsParser parser = TransactionsParserFactory.getFileTransactionsParser(ParserType.FILE_PARSER);
        List<CardTransaction> allTransactions = parser.parseFromSource(fileName);
        FraudDetectionDTO fraudDetectionDTO = FraudDetectionDTO.builder()
                .amountThreshold(Double.parseDouble(thresholdAmount))
                .transactionList(allTransactions)
                .build();
        new FraudDetector().detectFraud(fraudDetectionDTO);
    }
}
