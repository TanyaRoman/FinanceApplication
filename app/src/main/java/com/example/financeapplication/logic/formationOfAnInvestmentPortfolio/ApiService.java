package com.example.financeapplication.logic.formationOfAnInvestmentPortfolio;

import android.os.AsyncTask;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.litesoftwares.coingecko.CoinGeckoApiClient;
import com.litesoftwares.coingecko.domain.Coins.CoinList;
import com.litesoftwares.coingecko.domain.Coins.MarketChart;
import com.litesoftwares.coingecko.impl.CoinGeckoApiClientImpl;
import com.example.financeapplication.logic.formationOfAnInvestmentPortfolio.service.optimization.*;
import com.example.financeapplication.logic.formationOfAnInvestmentPortfolio.persistence.entity.*;
import com.example.financeapplication.logic.formationOfAnInvestmentPortfolio.persistence.entity.vue.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ApiService { //extends AsyncTask<String, String, String> {

    private CoinGeckoApiClient client = new CoinGeckoApiClientImpl();
    private Optimization optimization;

    private List<CoinList> allCoins = new ArrayList<>();
    private Portfolio portfolio;

//    private File file = new File("src/main/resources/popularCoins.txt");
//    private File file = new File("app/src/main/java/com/example/financeapplication/logic/formationOfAnInvestmentPortfolio/popularCoinsName.txt");
    private List<String> symbolsPopularCoins = new ArrayList<>();
    private List<CoinList> popularCoins = new ArrayList<>();

    private List<CoinList> selectedCoins = new ArrayList<>();
    private Integer period = 1;

    private List<Coin> coins = new ArrayList<>();
    private List<Portfolio> portfolioList = new ArrayList<>();

//    @Override
//    protected String doInBackground(String... urls) {
//        client = new CoinGeckoApiClientImpl();
//        return null;
//    }

    public ApiService(){

    }

    public void addData() throws IOException {
        selectCoins(); // обращается ко всем данным, считывает файл, заполняет popularCoins
//        System.out.println(popularCoins);
    }



    public void selectCoins() throws IOException {
//        System.out.println("---------------------------------------------------------");
        if (allCoins.size() == 0) {
//            System.out.println("allData_1");
            allCoins();
//            System.out.println("allData_2");
        }

        if (symbolsPopularCoins.size() == 0) {
//            FileReader fr = new FileReader(file);
//            BufferedReader reader = new BufferedReader(fr);
//            String line = reader.readLine();
//            while (line != null){
//                symbolsPopularCoins.add(line);
//                line = reader.readLine();
//            }
            symbolsPopularCoins.add("Bitcoin");
            symbolsPopularCoins.add("Ethereum");
            symbolsPopularCoins.add("Tether");
            symbolsPopularCoins.add("USD Coin");
            symbolsPopularCoins.add("BNB");
            symbolsPopularCoins.add("Cardano");
            symbolsPopularCoins.add("XRP");
            symbolsPopularCoins.add("Solana");
            symbolsPopularCoins.add("Dogecoin");
            symbolsPopularCoins.add("Polkadot");
            symbolsPopularCoins.add("TRON");
            symbolsPopularCoins.add("Dai");
            symbolsPopularCoins.add("Shiba Inu");
        }

        if (popularCoins.size() == 0) {
            for (CoinList coin : allCoins) {
                for (String symbol : symbolsPopularCoins) {
                    if (coin.getName().equals(symbol)){
                        popularCoins.add(coin);
                    }
//                    if (coin.getSymbol().equals(symbol)){
//                        popularCoins.add(coin);
//                    }
                }
            }
        }
    }

    public void allCoins(){
        allCoins = client.getCoinList();
//        System.out.println("client.getCoinList()");
//        System.out.println(client.getCoinList());
    }

    public List<CoinList> getPopularCoins() throws IOException {
//        selectCoins();
        addData();
        return popularCoins;
    }

    public void setSelectedCoins(List<String> listId) {
//        List<CoinList> list = new ArrayList<>();
        for (CoinList coin: popularCoins) {
            for (String id: listId) {
                if (coin.getId().equals(id)){
                    selectedCoins.add(coin);
                }
            }
        }
//
//        list.add(popularCoins.get(0));
//        list.add(popularCoins.get(4));
//        list.add(popularCoins.get(5));
//        this.selectedCoins = list;
    }

    public void setHistory() {
        for (CoinList coinList : selectedCoins) {
            Coin coin = new Coin(coinList);
            coin.setPriceList(client.getCoinMarketChartById(coinList.getId(), "rub", 365));
            coins.add(coin);
        }
    }

    public void setPortfolio(){
        portfolio = new Portfolio(coins);
    }

    public List<List<Double>> doOptimization(){
        optimization = new SPEA(getExpReturnCoins(), getCov());

        return optimization.count();
    }

//    public String getResult(List<String> listId) throws JsonProcessingException {
//        return new ObjectMapper().writeValueAsString(getPortfolios(listId));
//    }

    public Pers getPortfolios(List<String> listId){
//        System.out.println("hi-hi-hi");
        selectedCoins.clear();
        portfolioList.clear();
        coins.clear();
        setSelectedCoins(listId); // заполняет selectedCoins отобранными криптами
//        System.out.println("selectedCoins");
//        System.out.println(selectedCoins);

        setHistory(); // заполняет coin историей
//        System.out.println("setHistory");
//        System.out.println(coins);

        setPortfolio(); // заполняет portfolio
//        System.out.println("portfolio");
//        System.out.println(portfolio);

        setPortfolioList();
//        System.out.println("TT");

//        return portfolioList;

        return setPrPr();
    }

    public void setPortfolioList(){
//        System.out.println("setPortfolioList() - check");
        if (portfolioList.size() == 0) {
            List<List<Double>> optim = doOptimization();
            for (List<Double> list : optim) {
                Portfolio p = new Portfolio(coins);
                p.countParams(list);
                portfolioList.add(p);
//            break;
            }
        }
    }

    public Pers setPrPr(){
        List<Coins> coins = new ArrayList<>();
        int count =0;
        for (Coin coin:portfolio.getAssets()) {
            if (count< selectedCoins.size()) {
                coins.add(new Coins(coin.getCoinList().getId(), coin.getRisk(), coin.getExpectedReturn()));
                count++;
            }
        }
//        for (Coin coin:portfolio.getAssets()) {
//            coins.add(new Coins(coin.getCoinList().getId(), coin.getRisk(),coin.getExpectedReturn()));
//        }
//        List<Pers> persList = new ArrayList<>();
        List<Portfolios> portfoliosList = new ArrayList<>();
        for (Portfolio portfolio: portfolioList) {
            List<Share> share = new ArrayList<>();
            for (Shares shares: portfolio.getShare()) {
                share.add(new Share(shares.getCoin().getCoinList().getId(),shares.getCount()));
            }
            Portfolios portfolios = new Portfolios(portfolio.getRisk(), portfolio.getExpectedReturn(), share);
            portfoliosList.add(portfolios);
        }
        Pers persList = new Pers(coins, portfoliosList);
        return persList;
    }


    public List<Portfolio> getPortfolioList(){
        setPortfolioList();
        return portfolioList;
    }

    public List<CoinList> getSelectedCoins() {
        return selectedCoins;
    }

    public List<MarketChart> getHistory(){
        List<MarketChart> list = new ArrayList<>();
        for (Coin coin: coins) {
            list.add(coin.getPriceList());
        }
        return list;
    }

    public List<Double> getRisk(){
        List<Double> list = new ArrayList<>();
        for (Coin coin: coins){
            list.add(coin.getRisk());
        }
        return list;
    }

    public List<FuzzyNumber> getAssetReturn(){
        List<FuzzyNumber> list = new ArrayList<>();
        for (Coin coin: coins) {
            list.add(coin.getAssetReturn());
        }
        return list;
    }

    public Portfolio getPortfolio(){
        return portfolio;
    }

    public List<List<Double>> getCov(){
        return portfolio.getCov();
    }

    public List<Double> getExpReturnCoins() {
        return portfolio.getExpReturnCoins();
    }


}
