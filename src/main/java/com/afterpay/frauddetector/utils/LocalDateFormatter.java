package com.afterpay.frauddetector.utils;

import com.univocity.parsers.conversions.Conversion;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateFormatter implements Conversion<String, LocalDateTime> {

    private DateTimeFormatter formatter;

    public LocalDateFormatter(String... args) {
        if(args.length > 0){
            String pattern = args[0];
            this.formatter = DateTimeFormatter.ofPattern(pattern);
        } else {
            this.formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        }
    }

    @Override
    public LocalDateTime execute(String input) {
        return LocalDateTime.parse(input, formatter);
    }

    @Override
    public String revert(LocalDateTime input) {
        return formatter.format(input);
    }
}
