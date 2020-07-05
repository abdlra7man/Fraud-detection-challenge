package com.afterpay.frauddetector.core;

import com.afterpay.frauddetector.domain.dto.FraudDetectionDTO;
import java.util.List;

public interface FraudDetectionStrategy {
    List<String> detectFraud(FraudDetectionDTO fraudDetectionDTO);
}
