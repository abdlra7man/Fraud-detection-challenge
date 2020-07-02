package com.afterpay.frauddetector.core.parsers.impl;

import com.afterpay.frauddetector.core.parsers.TransactionsParser;
import com.afterpay.frauddetector.domain.model.CardTransaction;
import com.afterpay.frauddetector.domain.model.TransactionDetails;
import com.univocity.parsers.csv.CsvFormat;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CSVFileTransactionsParser implements TransactionsParser {
    @Override
    public List<CardTransaction> parseFromSource(String src) {
        List<CardTransaction> transactions = new ArrayList<>();
        CsvParser parser = getCsvParser(',');
        try (FileInputStream fileInputStream = new FileInputStream(src)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
            reader.lines().forEach( line-> {
                final String[] data = parser.parseLine(line);
                TransactionDetails dateAndAmount = TransactionDetails.builder()
                        .amount(Double.parseDouble(data[2]))
                        .date(LocalDateTime.parse(data[1])).build();
                CardTransaction cardTransaction = CardTransaction.builder()
                        .hashCardNumber(data[0])
                        .transactionDetails(dateAndAmount).build();
                transactions.add(cardTransaction);
            });

        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found");
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return transactions;
    }
    private CsvParser getCsvParser(char separator) {
        CsvParserSettings settings = new CsvParserSettings();
        settings.setMaxCharsPerColumn(-1);
        CsvFormat format = new CsvFormat();
        format.setDelimiter(separator);
        settings.setFormat(format);
        return new CsvParser(settings);
    }
}
