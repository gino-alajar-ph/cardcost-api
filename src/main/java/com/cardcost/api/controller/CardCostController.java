package com.cardcost.api.controller;

import com.cardcost.api.dto.CardCostResponse;
import com.cardcost.api.model.ClearingCost;
import com.cardcost.api.service.CardCostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/card-cost")
public class CardCostController {

    @Autowired
    private CardCostService cardCostService;

    @Operation(summary = "Get all clearing costs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    })
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<ClearingCost>> getAllClearingCosts() {

        List<ClearingCost> clearingCosts = cardCostService.getAllClearingCosts();
        return new ResponseEntity<>(clearingCosts, HttpStatus.OK);
    }

    @Operation(summary = "Get a clearing cost by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the clearing cost"),
            @ApiResponse(responseCode = "404", description = "ClearingCost not found")
    })
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ClearingCost> getClearingCostById(@PathVariable Long id) {

        ClearingCost clearingCost = cardCostService.getClearingCostById(id);
        return new ResponseEntity<>(clearingCost, HttpStatus.OK);
    }

    @Operation(summary = "Get a clearing cost by country code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the clearing cost"),
            @ApiResponse(responseCode = "404", description = "ClearingCost not found")
    })
    @GetMapping("/country/{countryCode}")
    @ResponseBody
    public ResponseEntity<ClearingCost> getClearingCostByCountryCode(@PathVariable String countryCode) {

        ClearingCost clearingCost = cardCostService.getClearingCostByCountryCode(countryCode);
        return new ResponseEntity<>(clearingCost, HttpStatus.OK);
    }

    @Operation(summary = "Create a new clearing cost")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created the clearing cost")
    })
    @PostMapping
    @ResponseBody
    public ResponseEntity<ClearingCost> createClearingCost(@RequestBody ClearingCost clearingCost) {

        ClearingCost createdClearingCost = cardCostService.createClearingCost(clearingCost);
        return new ResponseEntity<>(createdClearingCost, HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing clearing cost")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the clearing cost"),
            @ApiResponse(responseCode = "404", description = "ClearingCost not found")
    })
    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ClearingCost> updateClearingCost(@PathVariable Long id, @RequestBody ClearingCost clearingCost) {

        ClearingCost updatedClearingCost = cardCostService.updateClearingCost(id, clearingCost);
        return new ResponseEntity<>(updatedClearingCost, HttpStatus.OK);
    }

    @Operation(summary = "Delete a clearing cost")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted the clearing cost"),
            @ApiResponse(responseCode = "404", description = "ClearingCost not found")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClearingCost(@PathVariable Long id) {

        cardCostService.deleteClearingCost(id);
    }

    @Operation(summary = "Get clearing cost by card number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the clearing cost"),
            @ApiResponse(responseCode = "404", description = "ClearingCost not found for the given card number")
    })
    @PostMapping("/payment-cards-cost")
    @ResponseBody
    public ResponseEntity<CardCostResponse> getClearingCostByCardNumber(@RequestBody String cardNumber) {

        CardCostResponse response = cardCostService.getClearingCostByCardNumber(cardNumber);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
