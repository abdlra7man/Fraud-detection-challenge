package com.afterpay.frauddetector.core.parsers.impl;

import com.afterpay.frauddetector.core.parsers.TransactionsParser;
import com.afterpay.frauddetector.domain.model.CardTransaction;
import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.common.processor.core.Processor;
import com.univocity.parsers.csv.CsvFormat;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import java.io.*;
import java.util.List;

public class CSVFileTransactionsParser implements TransactionsParser {
    @Override
    public List<CardTransaction> parseFromSource(String src) {
        List<CardTransaction> transactions;
        BeanListProcessor<CardTransaction> rowProcessor = new BeanListProcessor<>(CardTransaction.class);
        CsvParser parser = getCsvParser(rowProcessor);
        try(FileReader csvFile = new FileReader(src)){
            parser.parse(csvFile);
            transactions = rowProcessor.getBeans();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found");
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return transactions;
    }
    private CsvParser getCsvParser(Processor processor) {
        CsvParserSettings settings = new CsvParserSettings();
        settings.setMaxCharsPerColumn(-1);
        CsvFormat format = new CsvFormat();
        settings.setProcessor(processor);
        format.setDelimiter(',');
        settings.setNumberOfRowsToSkip(0);
        settings.setHeaders("CardNumber", "Date", "Value");
        settings.setFormat(format);
        return new CsvParser(settings);
    }
}
