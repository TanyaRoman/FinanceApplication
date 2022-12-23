package com.example.financeapplication.logic.formationOfAnInvestmentPortfolio.persistence.entity.vue;

public class Coins {
    private String id;
    private Double risk;
    private Double expectedReturn;

    public Coins(String id, Double risk, Double expectedReturn) {
        this.id = id;
        this.risk = risk;
        this.expectedReturn = expectedReturn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getRisk() {
        return risk;
    }

    public void setRisk(Double risk) {
        this.risk = risk;
    }

    public Double getExpectedReturn() {
        return expectedReturn;
    }

    public void setExpectedReturn(Double expectedReturn) {
        this.expectedReturn = expectedReturn;
    }
}
