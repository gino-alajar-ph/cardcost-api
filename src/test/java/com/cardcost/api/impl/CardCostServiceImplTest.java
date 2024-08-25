package com.cardcost.api.impl;

import com.cardcost.api.dto.CardCostResponse;
import com.cardcost.api.exception.ResourceNotFoundException;
import com.cardcost.api.model.BinInfo;
import com.cardcost.api.model.ClearingCost;
import com.cardcost.api.repository.ClearingCostRepository;
import com.cardcost.api.service.impl.CardCostServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CardCostServiceImplTest {

    @Mock
    private ClearingCostRepository clearingCostRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CardCostServiceImpl cardCostService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllClearingCosts() {

        ClearingCost cost1 = new ClearingCost(1L, "US", 5.0);
        ClearingCost cost2 = new ClearingCost(2L, "GR", 15.0);
        List<ClearingCost> clearingCosts = Arrays.asList(cost1, cost2);

        when(clearingCostRepository.findAll()).thenReturn(clearingCosts);

        List<ClearingCost> result = cardCostService.getAllClearingCosts();
        assertEquals(2, result.size());
        verify(clearingCostRepository, times(1)).findAll();
    }

    @Test
    void testGetClearingCostById() {

        ClearingCost cost = new ClearingCost(1L, "US", 5.0);

        when(clearingCostRepository.findById(1L)).thenReturn(Optional.of(cost));

        ClearingCost result = cardCostService.getClearingCostById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(clearingCostRepository, times(1)).findById(1L);
    }

    @Test
    void testGetClearingCostByIdNotFound() {

        when(clearingCostRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            cardCostService.getClearingCostById(1L);
        });

        verify(clearingCostRepository, times(1)).findById(1L);
    }

    @Test
    void testGetClearingCostByCountryCode() {

        ClearingCost cost = new ClearingCost(1L, "US", 5.0);

        when(clearingCostRepository.findByCountryCode("US")).thenReturn(Optional.of(cost));

        ClearingCost result = cardCostService.getClearingCostByCountryCode("US");
        assertNotNull(result);
        assertEquals("US", result.getCountryCode());
        verify(clearingCostRepository, times(1)).findByCountryCode("US");
    }

    @Test
    void testCreateClearingCost() {

        ClearingCost cost = new ClearingCost(null, "US", 5.0);
        ClearingCost createdCost = new ClearingCost(1L, "US", 5.0);

        when(clearingCostRepository.save(any(ClearingCost.class))).thenReturn(createdCost);

        ClearingCost result = cardCostService.createClearingCost(cost);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(clearingCostRepository, times(1)).save(any(ClearingCost.class));
    }

    @Test
    void testUpdateClearingCost() {

        ClearingCost existingCost = new ClearingCost(1L, "US", 5.0);
        ClearingCost updatedCost = new ClearingCost(1L, "US", 10.0);

        when(clearingCostRepository.existsById(1L)).thenReturn(true);
        when(clearingCostRepository.save(any(ClearingCost.class))).thenReturn(updatedCost);

        ClearingCost result = cardCostService.updateClearingCost(1L, updatedCost);
        assertNotNull(result);
        assertEquals(10.0, result.getClearingCost());
        verify(clearingCostRepository, times(1)).existsById(1L);
        verify(clearingCostRepository, times(1)).save(any(ClearingCost.class));
    }

    @Test
    void testDeleteClearingCost() {

        when(clearingCostRepository.existsById(1L)).thenReturn(true);
        doNothing().when(clearingCostRepository).deleteById(1L);

        cardCostService.deleteClearingCost(1L);
        verify(clearingCostRepository, times(1)).existsById(1L);
        verify(clearingCostRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetClearingCostByCardNumberWithValidBin() {

        BinInfo.Country country = new BinInfo.Country();
        country.setAlpha2("US");

        BinInfo binInfo = new BinInfo();
        binInfo.setCountry(country);

        when(restTemplate.getForObject(anyString(), eq(BinInfo.class))).thenReturn(binInfo);

        ClearingCost clearingCost = new ClearingCost(1L, "US", 5.0);
        when(clearingCostRepository.findByCountryCode("US")).thenReturn(Optional.of(clearingCost));

        CardCostResponse response = cardCostService.getClearingCostByCardNumber("4111111111111111");

        assertEquals("US", response.getCountryCode());
        assertEquals(5.0, response.getClearingCost());
        assertFalse(response.isOthersUsed());

        verify(restTemplate, times(1)).getForObject(anyString(), eq(BinInfo.class));
        verify(clearingCostRepository, times(1)).findByCountryCode("US");
    }

    @Test
    void testGetClearingCostByCardNumberWithFallbackToOthers() {

        BinInfo.Country country = new BinInfo.Country();
        country.setAlpha2("PL");

        BinInfo binInfo = new BinInfo();
        binInfo.setCountry(country);

        when(restTemplate.getForObject(anyString(), eq(BinInfo.class))).thenReturn(binInfo);

        when(clearingCostRepository.findByCountryCode("PL")).thenReturn(Optional.empty());
        ClearingCost clearingCost = new ClearingCost(2L, "Others", 10.0);
        when(clearingCostRepository.findByCountryCode("Others")).thenReturn(Optional.of(clearingCost));

        CardCostResponse response = cardCostService.getClearingCostByCardNumber("45717360");

        assertEquals("Others", response.getCountryCode());
        assertEquals(10.0, response.getClearingCost());
        assertTrue(response.isOthersUsed());

        verify(restTemplate, times(1)).getForObject(anyString(), eq(BinInfo.class));
        verify(clearingCostRepository, times(1)).findByCountryCode("PL");
        verify(clearingCostRepository, times(1)).findByCountryCode("Others");
    }

    @Test
    void testGetClearingCostByCardNumberWithInvalidCardNumber() {

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                cardCostService.getClearingCostByCardNumber("123"));

        assertEquals("Card number must be at least 6 digits long.", exception.getMessage());
    }

    @Test
    void testGetClearingCostByCardNumberWithMissingBinInfo() {

        when(restTemplate.getForObject(anyString(), eq(BinInfo.class))).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                cardCostService.getClearingCostByCardNumber("411111"));

        assertEquals("BIN information not found or incomplete for BIN: 411111", exception.getMessage());
    }
}
