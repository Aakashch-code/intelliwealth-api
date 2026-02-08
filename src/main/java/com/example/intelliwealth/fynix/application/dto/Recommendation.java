<<<<<<<< HEAD:src/main/java/com/example/intelliwealth/fynix/application/dto/Recommendation.java
package com.example.intelliwealth.fynix.api.dto;
========
package com.example.intelliwealth.fynix.domain.model;
>>>>>>>> main:src/main/java/com/example/intelliwealth/fynix/domain/model/Recommendation.java

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class Recommendation {
    private String id;
    private String title;
    private String description;
    private String priority;
    private BigDecimal estimatedMonthlyImpact;
    private Double confidence;
    private List<String> actions;
}
