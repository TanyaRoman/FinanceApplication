package com.example.financeapplication.logic.formationOfAnInvestmentPortfolio.persistence.entity.vue;

import java.util.List;

public class Pers {
    List<Coins> coins;
    List<Portfolios> portfolios;

    public Pers() {
    }

    public Pers(List<Coins> coins, List<Portfolios> portfolios) {
        this.coins = coins;
        this.portfolios = portfolios;
    }

    public List<Coins> getCoins() {
        return coins;
    }

    public void setCoins(List<Coins> coins) {
        this.coins = coins;
    }

    public List<Portfolios> getPortfolios() {
        return portfolios;
    }

    public void setPortfolios(List<Portfolios> portfolios) {
        this.portfolios = portfolios;
    }
}
