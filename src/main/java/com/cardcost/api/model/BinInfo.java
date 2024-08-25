package com.cardcost.api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BinInfo {

    private NumberInfo number;
    private String scheme;
    private String type;
    private String brand;
    private boolean prepaid;
    private Country country;
    private Bank bank;

    @Getter
    @Setter
    public static class NumberInfo {
        private int length;
        private boolean luhn;
    }

    @Getter
    @Setter
    public static class Country {
        private String numeric;
        private String alpha2;
        private String name;
        private String emoji;
        private String currency;
        private int latitude;
        private int longitude;
    }

    @Getter
    @Setter
    public static class Bank {
        private String name;
        private String url;
        private String phone;
        private String city;
    }
}
