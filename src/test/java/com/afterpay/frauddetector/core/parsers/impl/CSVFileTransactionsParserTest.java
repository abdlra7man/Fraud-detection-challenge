package com.afterpay.frauddetector.core.parsers.impl;

import com.afterpay.frauddetector.core.exception.ValidationException;
import com.afterpay.frauddetector.domain.model.CardTransaction;
import com.univocity.parsers.common.Context;
import com.univocity.parsers.common.processor.core.Processor;
import com.univocity.parsers.csv.CsvParser;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.FileReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;

@RunWith(MockitoJUnitRunner.class)
public class CSVFileTransactionsParserTest {
    private static final String TEST_FILE_PATH = "src/test/resources/transactions_sample.csv";
    private static final String MALFORMATED_FILE_PATH = "src/test/resources/transactions_malformated.csv";

    @Mock
    Processor rowProcessor;

    @Spy
    CSVFileTransactionsParser csvFileTransactionsParser = new CSVFileTransactionsParser();

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();


    @Test
    public void testParseFromSource(){
        List<CardTransaction> transactions = csvFileTransactionsParser.parseFromSource(TEST_FILE_PATH);
        assertEquals(35, transactions.size());
        Mockito.verify(csvFileTransactionsParser).getCsvParser(any(Processor.class));
    }

    @Test
    public void testParseMalformated(){
        exceptionRule.expect(ValidationException.class);
        exceptionRule.expectMessage("Incorrect file format, please verify data is formatted correctly");
        CSVFileTransactionsParser csvFileTransactionsParser = new CSVFileTransactionsParser();
        csvFileTransactionsParser.parseFromSource(MALFORMATED_FILE_PATH);
    }

    @Test
    public void testParsingNonExistingFile(){
        exceptionRule.expect(ValidationException.class);
        exceptionRule.expectMessage("File not found");
        csvFileTransactionsParser.parseFromSource("incorrect_path.csv");
    }

    @Test
    public void testRowProcessorIsUsedByCsvParser(){
        getParserWithMockedProcessor();
        Mockito.verify(rowProcessor).processStarted(Mockito.any(Context.class));
    }

    @Test
    public void testGetCsvParserSettings(){
        CsvParser csvParser = getParserWithMockedProcessor();
        assertEquals("CardNumber", csvParser.getRecordMetadata().headers()[0]);
        assertEquals("Date", csvParser.getRecordMetadata().headers()[1]);
        assertEquals("Value", csvParser.getRecordMetadata().headers()[2]);
    }

    private CsvParser getParserWithMockedProcessor() {
        CsvParser csvParser = csvFileTransactionsParser.getCsvParser(rowProcessor);
        try (FileReader csvFile = new FileReader(TEST_FILE_PATH)) {
            csvParser.parse(csvFile);
        } catch (Exception e) {
            throw new ValidationException(e.getMessage());
        }
        return csvParser;
    }
}
