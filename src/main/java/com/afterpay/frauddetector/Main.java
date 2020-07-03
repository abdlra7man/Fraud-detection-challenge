package com.afterpay.frauddetector;

import com.afterpay.frauddetector.core.FraudDetector;
import com.afterpay.frauddetector.core.exception.ValidationException;
import com.afterpay.frauddetector.core.parsers.ParserType;
import com.afterpay.frauddetector.core.parsers.TransactionsParser;
import com.afterpay.frauddetector.core.parsers.TransactionsParserFactory;
import com.afterpay.frauddetector.domain.dto.FraudDetectionDTO;
import com.afterpay.frauddetector.domain.model.CardTransaction;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String [] args){
        validateNumberOfArgs(args);
        List<String> fraudulentCards = getFraudulentCardsList(args);
        displayOutput(fraudulentCards);
    }

    private static List<String> getFraudulentCardsList(String[] args) {
        List<String> fraudulentCards = new ArrayList<>();
        try{
            validateParams(args[0], args[1]);
            fraudulentCards = processInput(args[0], args[1]);
        } catch(ValidationException validationException){
            System.out.println(validationException.getMessage());
            System.exit(1);
        }
        return fraudulentCards;
    }

    static List<String> processInput(String thresholdAmount, String fileName) {
        TransactionsParser parser = TransactionsParserFactory.getFileTransactionsParser(ParserType.FILE_PARSER);
        List<CardTransaction> allTransactions = parser.parseFromSource(fileName);
        FraudDetectionDTO fraudDetectionDTO = FraudDetectionDTO.builder()
                .amountThreshold(Double.parseDouble(thresholdAmount))
                .transactionList(allTransactions)
                .build();
        return new FraudDetector().detectFraud(fraudDetectionDTO);
    }

    private static void displayOutput(List<String> fraudulentCards) {
        if(fraudulentCards.size() > 0){
            System.out.println("DETECTED FRAUDULENT CARDS :");
            fraudulentCards.forEach(System.out::println);
        } else {
            System.out.println("NO DETECTED FRAUDULENT CARDS ");
        }
    }

    private static void validateNumberOfArgs(String[] args) {
        if (args.length != 2) {
            System.err.println("Invalid number of arguments. usage java -jar fraud-detector-0.1.0.jar threshold filename.csv ");
            System.exit(1);
        }
    }

    static void validateParams(String thresholdAmount, String fileName){
        try {
            Double.parseDouble(thresholdAmount);
        } catch (NumberFormatException e) {
            System.err.println("bad amount argument : expected XX.XX");
            throw new ValidationException("Threshold amount is not correct");
        }
        try {
            FileReader transactionsFile = new FileReader(fileName);
        } catch (FileNotFoundException e) {
            System.err.println("Incorrect file path, file does not exist");
            throw new ValidationException("Invalid file");
        }
    }
}
