package com.cardcost.api.service.impl;

import com.cardcost.api.dto.CardCostResponse;
import com.cardcost.api.exception.ResourceNotFoundException;
import com.cardcost.api.model.BinInfo;
import com.cardcost.api.model.ClearingCost;
import com.cardcost.api.repository.ClearingCostRepository;
import com.cardcost.api.service.CardCostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class CardCostServiceImpl implements CardCostService {

    private final ClearingCostRepository repository;
    private final RestTemplate restTemplate;

    @Autowired
    public CardCostServiceImpl(ClearingCostRepository repository, RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<ClearingCost> getAllClearingCosts() {

        return repository.findAll();
    }

    @Override
    public ClearingCost getClearingCostById(Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ClearingCost not found for this id :: " + id));
    }

    @Override
    public ClearingCost getClearingCostByCountryCode(String countryCode) {

        return repository.findByCountryCode(countryCode)
                .orElseThrow(() -> new ResourceNotFoundException("ClearingCost not found for country code :: " + countryCode));
    }

    @Override
    public ClearingCost createClearingCost(ClearingCost clearingCost) {

        return repository.save(clearingCost);
    }

    @Override
    public ClearingCost updateClearingCost(Long id, ClearingCost clearingCost) {

        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("ClearingCost not found for this id :: " + id);
        }
        clearingCost.setId(id);
        return repository.save(clearingCost);
    }

    @Override
    public void deleteClearingCost(Long id) {

        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("ClearingCost not found for this id :: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public CardCostResponse getClearingCostByCardNumber(String cardNumber) {

        if (cardNumber.length() < 6) {
            throw new IllegalArgumentException("Card number must be at least 6 digits long.");
        }

        String bin = cardNumber.substring(0, Math.min(8, cardNumber.length()));
        BinInfo binInfo;

        try {
            binInfo = restTemplate.getForObject("https://lookup.binlist.net/" + bin, BinInfo.class);
        } catch (HttpClientErrorException e) {
            if (bin.length() == 8) {
                bin = cardNumber.substring(0, 6);
                binInfo = restTemplate.getForObject("https://lookup.binlist.net/" + bin, BinInfo.class);
            } else {
                throw new ResourceNotFoundException("BIN information could not be retrieved for card number: " + cardNumber);
            }
        }

        if (binInfo == null || binInfo.getCountry() == null || binInfo.getCountry().getAlpha2() == null) {
            throw new ResourceNotFoundException("BIN information not found or incomplete for BIN: " + bin);
        }

        String countryCode = binInfo.getCountry().getAlpha2();
        Optional<ClearingCost> optionalClearingCost = repository.findByCountryCode(countryCode);
        ClearingCost clearingCost = optionalClearingCost.orElseGet(() -> repository.findByCountryCode("Others").orElseThrow(
                () -> new ResourceNotFoundException("ClearingCost not found for default code: Others")));

        boolean isOthersUsed = !optionalClearingCost.isPresent();

        return new CardCostResponse(isOthersUsed ? "Others" : countryCode, clearingCost.getClearingCost(), isOthersUsed);
    }

}
