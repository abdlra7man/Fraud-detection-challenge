package com.afterpay.frauddetector.core.parsers.impl;

import com.afterpay.frauddetector.core.exception.ValidationException;
import com.afterpay.frauddetector.core.parsers.TransactionsParser;
import com.afterpay.frauddetector.domain.model.CardTransaction;
import com.univocity.parsers.common.DataProcessingException;
import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.common.processor.core.Processor;
import com.univocity.parsers.csv.CsvFormat;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class CSVFileTransactionsParser implements TransactionsParser {
    private static final String[] HEADERS = {"CardNumber", "Date", "Value"};

    @Override
    public List<CardTransaction> parseFromSource(String src) {
        List<CardTransaction> transactions;
        BeanListProcessor<CardTransaction> rowProcessor = new BeanListProcessor<>(CardTransaction.class);
        CsvParser parser = getCsvParser(rowProcessor);
        try(FileReader csvFile = new FileReader(src)){
            parser.parse(csvFile);
            transactions = rowProcessor.getBeans();
        } catch (FileNotFoundException e) {
            throw new ValidationException("File not found");
        } catch (IOException e) {
            throw new ValidationException(e.getMessage());
        } catch (DataProcessingException dpe){
            throw new ValidationException("Incorrect file format, please verify data is formatted correctly");
        }
        return transactions;
    }
    CsvParser getCsvParser(Processor processor) {
        CsvParserSettings settings = new CsvParserSettings();
        settings.setMaxCharsPerColumn(-1);
        CsvFormat format = new CsvFormat();
        settings.setProcessor(processor);
        format.setDelimiter(',');
        settings.setNumberOfRowsToSkip(0);
        settings.setHeaders(HEADERS);
        settings.setFormat(format);
        return new CsvParser(settings);
    }
}
