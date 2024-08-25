package com.cardcost.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardCostResponse {

    private String countryCode;
    private Double clearingCost;
    private boolean isOthersUsed;
}
