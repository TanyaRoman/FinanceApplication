package com.example.financeapplication.logic.formationOfAnInvestmentPortfolio.persistence.entity;

public class FuzzyNumber {

    private Double ra;
    private Double rb;
    private Double rc;

    public FuzzyNumber(Double ra, Double rb, Double rc) {
        this.ra = ra;
        this.rb = rb;
        this.rc = rc;
    }

    public Double getRa() {
        return ra;
    }

    public void setRa(Double ra) {
        this.ra = ra;
    }

    public Double getRb() {
        return rb;
    }

    public void setRb(Double rb) {
        this.rb = rb;
    }

    public Double getRc() {
        return rc;
    }

    public void setRc(Double rc) {
        this.rc = rc;
    }
}
