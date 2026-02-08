<<<<<<<< HEAD:src/main/java/com/example/intelliwealth/fynix/application/dto/KeyMetrics.java
package com.example.intelliwealth.fynix.api.dto;
========
package com.example.intelliwealth.fynix.domain.model;
>>>>>>>> main:src/main/java/com/example/intelliwealth/fynix/domain/model/KeyMetrics.java

import lombok.Data;
import java.math.BigDecimal;

@Data
public class KeyMetrics {

    private BigDecimal totalMonthlyIncome;

    private BigDecimal totalMonthlySpend;

    private BigDecimal monthlySubscriptionCost;

    private BigDecimal savingsRate;
}
