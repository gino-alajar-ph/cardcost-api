package com.cardcost.api.service;

import com.cardcost.api.dto.CardCostResponse;
import com.cardcost.api.model.ClearingCost;

import java.util.List;

public interface CardCostService {

    List<ClearingCost> getAllClearingCosts();

    ClearingCost getClearingCostById(Long id);

    ClearingCost getClearingCostByCountryCode(String countryCode);

    ClearingCost createClearingCost(ClearingCost clearingCost);

    ClearingCost updateClearingCost(Long id, ClearingCost clearingCost);

    void deleteClearingCost(Long id);

    CardCostResponse getClearingCostByCardNumber(String cardNumber);
}
