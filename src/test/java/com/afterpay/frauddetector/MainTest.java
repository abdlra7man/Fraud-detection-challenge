package com.afterpay.frauddetector;

import com.afterpay.frauddetector.core.exception.ValidationException;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class MainTest {
    private static final String TEST_FILE_PATH = "src/test/resources/transactions_sample.csv";

    @Test
    public void testProcessInput(){
        List<String> fraudulentCards = Main.processInput("50.0",TEST_FILE_PATH);
        assertEquals(3,fraudulentCards.size());
        fraudulentCards = Main.processInput("40.0",TEST_FILE_PATH);
        assertEquals(4,fraudulentCards.size());
    }

    @Test(expected = ValidationException.class)
    public void testValidateThresholdAmount(){
        Main.validateParams("abcd", "src/target/reosurces/transactions_sample.csv");
    }

    @Test(expected = ValidationException.class)
    public void testValidateFilePath(){
        Main.validateParams("150.0", "efgh");
    }
}
