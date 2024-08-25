package com.cardcost.api.controller;

import com.cardcost.api.dto.CardCostResponse;
import com.cardcost.api.model.ClearingCost;
import com.cardcost.api.service.CardCostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class CardCostControllerTest {

    @Mock
    private CardCostService cardCostService;

    @InjectMocks
    private CardCostController cardCostController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllClearingCosts() {

        ClearingCost cost1 = new ClearingCost(1L, "US", 5.0);
        ClearingCost cost2 = new ClearingCost(2L, "GR", 15.0);
        List<ClearingCost> clearingCosts = Arrays.asList(cost1, cost2);

        when(cardCostService.getAllClearingCosts()).thenReturn(clearingCosts);

        ResponseEntity<List<ClearingCost>> response = cardCostController.getAllClearingCosts();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(cardCostService, times(1)).getAllClearingCosts();
    }

    @Test
    void testGetClearingCostById() {

        ClearingCost cost = new ClearingCost(1L, "US", 5.0);

        when(cardCostService.getClearingCostById(1L)).thenReturn(cost);

        ResponseEntity<ClearingCost> response = cardCostController.getClearingCostById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        assertEquals("US", response.getBody().getCountryCode());
        verify(cardCostService, times(1)).getClearingCostById(1L);
    }

    @Test
    void testCreateClearingCost() {

        ClearingCost cost = new ClearingCost(null, "US", 5.0);
        ClearingCost createdCost = new ClearingCost(1L, "US", 5.0);

        when(cardCostService.createClearingCost(any(ClearingCost.class))).thenReturn(createdCost);

        ResponseEntity<ClearingCost> response = cardCostController.createClearingCost(cost);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        verify(cardCostService, times(1)).createClearingCost(any(ClearingCost.class));
    }

    @Test
    void testUpdateClearingCost() {

        ClearingCost updatedCost = new ClearingCost(1L, "US", 10.0);

        when(cardCostService.updateClearingCost(anyLong(), any(ClearingCost.class))).thenReturn(updatedCost);

        ResponseEntity<ClearingCost> response = cardCostController.updateClearingCost(1L, updatedCost);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(10.0, response.getBody().getClearingCost());
        verify(cardCostService, times(1)).updateClearingCost(anyLong(), any(ClearingCost.class));
    }

    @Test
    void testDeleteClearingCost() {

        doNothing().when(cardCostService).deleteClearingCost(1L);

        cardCostController.deleteClearingCost(1L);
        verify(cardCostService, times(1)).deleteClearingCost(1L);
    }

    @Test
    void testGetClearingCostByCardNumber() {

        CardCostResponse mockResponse = new CardCostResponse("US", 5.0, false);

        when(cardCostService.getClearingCostByCardNumber(anyString())).thenReturn(mockResponse);

        ResponseEntity<CardCostResponse> response = cardCostController.getClearingCostByCardNumber("4111111111111111");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("US", response.getBody().getCountryCode());
        assertEquals(5.0, response.getBody().getClearingCost());
        assertFalse(response.getBody().isOthersUsed());

        verify(cardCostService, times(1)).getClearingCostByCardNumber(anyString());
    }

}
