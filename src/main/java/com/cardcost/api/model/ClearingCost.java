package com.cardcost.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Entity
@Table(name = "clearing_cost")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClearingCost implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "country_code", length = 10)
    private String countryCode;

    @Column(name = "clearing_cost")
    private Double clearingCost;
}