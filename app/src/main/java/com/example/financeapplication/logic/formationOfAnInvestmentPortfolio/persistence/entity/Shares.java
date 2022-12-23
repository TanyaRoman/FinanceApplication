package com.example.financeapplication.logic.formationOfAnInvestmentPortfolio.persistence.entity;

public class Shares {

    private Coin coin;
    private Double count;

    public Shares(Coin coin, Double count) {
        this.coin = coin;
        this.count = count;
    }

    public Coin getCoin() {
        return coin;
    }

    public void setCoin(Coin coin) {
        this.coin = coin;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }
}
