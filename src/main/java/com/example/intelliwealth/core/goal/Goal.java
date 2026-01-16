package com.example.intelliwealth.core.goal;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Hidden
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Goal {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;
    private UUID userId;
    private BigDecimal currentAmount;
    private String name;
//    @JsonSerialize(using = CurrencySerializer.class)
    private BigDecimal targetAmount;
//    @JsonSerialize(using = CurrencySerializer.class)
    private BigDecimal spentAmount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date targetDate;
    private String priority;

}
