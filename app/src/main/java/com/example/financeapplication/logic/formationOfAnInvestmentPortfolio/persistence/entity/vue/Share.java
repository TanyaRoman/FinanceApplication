package com.example.financeapplication.logic.formationOfAnInvestmentPortfolio.persistence.entity.vue;

public class Share {
    private String id;
    private Double count;

    public Share(String id, Double count) {
        this.id = id;
        this.count = count;
    }

    public Share(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }
}
