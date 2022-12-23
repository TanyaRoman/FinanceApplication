package com.example.financeapplication.logic.formationOfAnInvestmentPortfolio.persistence.entity;

public class Cov {

    private Double cov;
    private Coin coin1;
    private Coin coin2;

    public Cov (Coin coin1, Coin coin2) {
        this.coin1 = coin1;
        this.coin2 = coin2;
        this.cov = countCov();
    }

    private Double countCov(){
        double ra1 = coin1.getAssetReturn().getRa();
        double rb1 = coin1.getAssetReturn().getRb();
        double rc1 = coin1.getAssetReturn().getRc();
        double ra2 = coin2.getAssetReturn().getRa();
        double rb2 = coin2.getAssetReturn().getRb();
        double rc2 = coin2.getAssetReturn().getRc();

        Double cov = ((rb1-ra1)*(rb2-ra2) + (rc1-rb1)*(rc2-rb2))/4
                + ((ra2*(rb1-ra1) + ra1*(rb2-ra2)) - (rc1*(rc2-rb2) + rc2*(rc1-rb1)))/3
                + (ra1*ra2 + rc1*rc2)/2 + (ra1+4*rb1+rc1)/6*(ra2+4*rb2+rc2)/6/2;

        return cov;
    }

    public Double getCov() {
        return cov;
    }

    public void setCov(Double cov) {
        this.cov = cov;
    }

    public Coin getCoin1() {
        return coin1;
    }

    public void setCoin1(Coin coin1) {
        this.coin1 = coin1;
    }

    public Coin getCoin2() {
        return coin2;
    }

    public void setCoin2(Coin coin2) {
        this.coin2 = coin2;
    }
}
