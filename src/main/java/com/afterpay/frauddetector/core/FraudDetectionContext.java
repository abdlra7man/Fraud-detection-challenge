package com.afterpay.frauddetector.core;

import com.afterpay.frauddetector.domain.dto.FraudDetectionDTO;
import java.util.List;

public class FraudDetectionContext {
    private FraudDetectionStrategy fraudDetectionStrategy;

    public FraudDetectionContext(FraudDetectionStrategy fraudDetectionStrategy){
        this.fraudDetectionStrategy = fraudDetectionStrategy;
    }

    public List<String> executeFraudDetection(FraudDetectionDTO fraudDetectionDTO){
        return fraudDetectionStrategy.detectFraud(fraudDetectionDTO);
    }
}
