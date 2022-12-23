package com.example.financeapplication.logic.formationOfAnInvestmentPortfolio.persistence.entity.vue;

import java.util.List;

public class Portfolios {
    Double risk;
    Double expectedReturn;
    List<Share> share;

    public Portfolios(Double risk, Double expectedReturn, List<Share> share) {
        this.risk = risk;
        this.expectedReturn = expectedReturn;
        this.share = share;
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

    public List<Share> getShare() {
        return share;
    }

    public void setShare(List<Share> share) {
        this.share = share;
    }
}
