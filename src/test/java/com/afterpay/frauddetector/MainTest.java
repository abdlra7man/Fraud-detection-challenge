package com.afterpay.frauddetector;

import com.afterpay.frauddetector.core.exception.ValidationException;
import com.google.common.collect.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MainTest {
    private static final String TEST_FILE_PATH = "src/test/resources/transactions_sample.csv";
    private static final String TEST_FILE_PATH_2 = "src/test/resources/transactions_sample2.csv";
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }
    @Test
    public void testDisplayOutputWithFraudulentCards(){
        Main.displayOutput(Lists.newArrayList("12345678abcd"));
        assertEquals("DETECTED FRAUDULENT CARDS :\n12345678abcd\n", outContent.toString());
    }

    @Test
    public void testDisplayOutputWithNoFraudulentCards(){
        Main.displayOutput(null);
        assertEquals("NO DETECTED FRAUDULENT CARDS\n", outContent.toString());
    }

    @Test
    public void testGetFraudulentCardsList(){
        List<String> fraudulentCards = Main.getFraudulentCardsList(new String[]{"60.0",TEST_FILE_PATH_2});
        assertEquals(2, fraudulentCards.size());
        assertTrue(fraudulentCards.contains("10d7ce2f43e35fa57d1bbf8b1e2"));
        assertTrue(fraudulentCards.contains("10d7ce2f43e35fa57d1bbf8b1e6"));
    }

    @Test
    public void testProcessInput(){
        List<String> fraudulentCards = Main.processInput("60.0",TEST_FILE_PATH);
        assertEquals(1,fraudulentCards.size());
        assertTrue(fraudulentCards.contains("10d7ce2f43e35fa57d1bbf8b1e2"));
        fraudulentCards = Main.processInput("50.0",TEST_FILE_PATH);
        assertEquals(3,fraudulentCards.size());
        fraudulentCards = Main.processInput("40.0",TEST_FILE_PATH);
        assertEquals(4,fraudulentCards.size());
    }

    @Test(expected = ValidationException.class)
    public void testValidateThresholdAmount(){
        Main.validateParams("abcd", TEST_FILE_PATH);
    }

    @Test(expected = ValidationException.class)
    public void testValidateFilePath(){
        Main.validateParams("150.0", "efgh");
    }
}
