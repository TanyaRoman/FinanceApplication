package com.example.financeapplication.logic.formationOfAnInvestmentPortfolio.persistence.entity;

import java.util.ArrayList;
import java.util.List;

public class Portfolio {

    private List<Coin> assets;
//    private List<Cov> covList;

    private List<List<Double>> cov = new ArrayList<>();
    private List<Double> expReturnCoins = new ArrayList<>();

    private Double risk;
    private Double expectedReturn;
    private List<Shares> share = new ArrayList<>();
//    private List<Coin> finalAssets = new ArrayList<>();

    public Portfolio(List<Coin> coinList) {
        this.assets = coinList;
//        countCovList();
        countCov();
//        countExpectReturn();
//        countRisk();
        for (Coin coin:assets) {
            expReturnCoins.add(coin.getExpectedReturn());
        }
    }

    private void countCov(){
//        int i = 0;
        for (Coin coin1: assets) {
            List<Double> list = new ArrayList<>();
            for (Coin coin2: assets) {
                Cov c = new Cov(coin1, coin2);
                list.add(c.getCov());
            }
//            i++;
            cov.add(list);
        }
    }

    public void countParams(List<Double> listCount){
        int i = 0;
        for (Coin coin: assets) {
            this.share.add(new Shares(coin, listCount.get(i)));
            i++;
        }
        countRisk();
        countExpectReturn();
    }

//
    public void countExpectReturn(){
        double count = 0;
        for (Shares coinCount: share) {
            count += coinCount.getCoin().getExpectedReturn()*coinCount.getCount();
        }
        this.expectedReturn = count;
    }

    public void countRisk(){
        double count = 0;
        for (int i = 0; i < share.size(); i++) {
            for (int j = 0; j < share.size(); j++) {
                count += cov.get(i).get(j)*share.get(i).getCount()*share.get(j).getCount();
            }
        }
        this.risk = count;
    }

    public List<Coin> getAssets() {
        return assets;
    }

    public void setAssets(List<Coin> assets) {
        this.assets = assets;
    }

    public List<List<Double>> getCov() {
        return cov;
    }

    public void setCov(List<List<Double>> cov) {
        this.cov = cov;
    }

    public List<Double> getExpReturnCoins() {
        return expReturnCoins;
    }

    public void setExpReturnCoins(List<Double> expReturnCoins) {
        this.expReturnCoins = expReturnCoins;
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

    public List<Shares> getShare() {
        return share;
    }

    public void setShare(List<Shares> share) {
        this.share = share;
    }
}
