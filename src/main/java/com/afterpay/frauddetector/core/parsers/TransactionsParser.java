package com.afterpay.frauddetector.core.parsers;

import com.afterpay.frauddetector.domain.model.CardTransaction;

import java.util.List;

public interface TransactionsParser {
    List<CardTransaction> parseFromSource(String src);
}
