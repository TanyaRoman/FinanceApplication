package com.example.financeapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.financeapplication.logic.formationOfAnInvestmentPortfolio.ApiService;
import com.example.financeapplication.logic.formationOfAnInvestmentPortfolio.persistence.entity.vue.Coins;
import com.example.financeapplication.logic.formationOfAnInvestmentPortfolio.persistence.entity.vue.Pers;
import com.example.financeapplication.logic.formationOfAnInvestmentPortfolio.persistence.entity.vue.Portfolios;
import com.example.financeapplication.logic.formationOfAnInvestmentPortfolio.persistence.entity.vue.Share;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Result2Activity extends AppCompatActivity implements View.OnClickListener{

    ListView lv;
    ListView lv_c;
    Button btn_save_c;
    private List<Portfolios> portfoliosList;
    private List<Coins> coinsList;
//    private ApiService apiService;
    private MyArrayAdapterPortfolio myArrayAdapter;
    private MyArrayAdapterCoins myArrayAdapterCoins;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_2);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Bundle arguments = getIntent().getExtras();

//        ApiService apiService;
        if ((!arguments.equals(null)) || (portfoliosList.equals(null))) {
            String data = arguments.get("criptoList").toString();
//            String apiS = arguments.get("apiServ").toString();
//            System.out.println("************");
//            System.out.println(arguments.get(ApiService.class.getSimpleName()));
//            apiService = (ApiService) arguments.get(ApiService.class.getSimpleName());
            ApiService apiService = new ApiService();            // осуждаю

            ArrayList<String> criptoList = null;
            try {
                criptoList = new ObjectMapper().readValue(data, ArrayList.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

//            try {
////                System.out.println("---------------------");
////                System.out.println(apiS);
//                apiService = new ObjectMapper().readValue(apiS,  new TypeReference<ApiService>(){});
////                System.out.println(apiService);
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }

//            System.out.println(apiS);
//            apiService = (ApiService) getReferrer(apiS);
//            System.out.println(apiService);
            portfoliosList = new ArrayList<>();
            coinsList = new ArrayList<>();
            try {
                initList(apiService, criptoList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // it doesn't work. i tried to do great code...
//        String apiServ = arguments.get("apiService").toString();
//        try {
//            apiService = new ObjectMapper().readValue(apiServ, ApiService.class);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }



        lv = (ListView) findViewById(R.id.lv_portfolios);
        lv_c = (ListView) findViewById(R.id.lv_coin_det);


        btn_save_c = (Button) findViewById(R.id.btn_save_c);
        btn_save_c.setOnClickListener(this);

        myArrayAdapterCoins = new Result2Activity.MyArrayAdapterCoins(this, R.layout.list_item_portfolio,
                android.R.id.text1, coinsList);
        lv_c.setAdapter(myArrayAdapterCoins);

        myArrayAdapter = new Result2Activity.MyArrayAdapterPortfolio(this, R.layout.list_item_portfolio,
                android.R.id.text1, portfoliosList);

        lv.setAdapter(myArrayAdapter);
        lv.setOnItemClickListener(myOnItemClickListener);
    }

    public void onClick(View view) {
        Intent i;
        switch (view.getId()){
            case R.id.btn_save_c:
//                Intent i = new Intent(this, ChoiceActivity.class);
                i = new Intent(this, LoginCreateActivity.class);
                startActivity(i);
                break;
//            case R.id.lay:
//                i = new Intent(this, PortfolioDetailingActivity.class);
//                startActivity(i);
//                break;
        }
    }

    private void initList(ApiService apiService, ArrayList<String> criptoList) throws IOException {
        apiService.addData();
        Pers result = apiService.getPortfolios(criptoList);
        for (Portfolios p: result.getPortfolios()) {
            portfoliosList.add(p);
        }
        for (Coins c: result.getCoins()) {
            coinsList.add(c);
        }
    }

    AdapterView.OnItemClickListener myOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            System.out.println("click 1");
//            System.out.println(portfoliosList.get(position));
//            System.out.println(view);
//            System.out.println(view.getId());
            Intent i = new Intent(Result2Activity.this, PortfolioDetailingActivity.class);

            String data = "";
            String risk = "";
            String exp = "";
            try {
//                data = new Gson().toJson(portfoliosList.get(position).getShare());
                data = new ObjectMapper().writeValueAsString(portfoliosList.get(position).getShare());
                risk = new ObjectMapper().writeValueAsString(portfoliosList.get(position).getRisk());
                exp = new ObjectMapper().writeValueAsString(portfoliosList.get(position).getExpectedReturn());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            i.putExtra("criptoList", data);
            i.putExtra("risk", risk);
            i.putExtra("exp", exp);

            startActivity(i);
        }
    };


    private class MyArrayAdapterCoins extends ArrayAdapter<Coins> {

        MyArrayAdapterCoins(Context context, int resource, int textViewResourceId, List<Coins> objects) {
            super(context, resource, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View row = convertView;

            if (row == null) {
                LayoutInflater inflater = getLayoutInflater();
                row = inflater.inflate(R.layout.list_item_cripto, parent, false);
            }

            TextView tv_portf_numb = (TextView) row.findViewById(R.id.tv_cripto_name);
            tv_portf_numb.setText(coinsList.get(position).getId());

            TextView tv_prof = (TextView) row.findViewById(R.id.tv_prof);
            tv_prof.setText(String.format("%.2f", coinsList.get(position).getExpectedReturn()));
//            tv_prof.setText(Double.toString(coinsList.get(position).getExpectedReturn()));

            TextView tv_risk = (TextView) row.findViewById(R.id.tv_risk);
            tv_risk.setText(String.format("%.2f", coinsList.get(position).getRisk()));
//            tv_risk.setText(Double.toString(coinsList.get(position).getRisk()));

            return row;
        }
    }


    private class MyArrayAdapterPortfolio extends ArrayAdapter<Portfolios> {

        Portfolios portfolios = new Portfolios();

        MyArrayAdapterPortfolio(Context context, int resource, int textViewResourceId, List<Portfolios> objects) {
            super(context, resource, textViewResourceId, objects);
        }

//        Portfolios getCheckedItems(int position) {
//            return checkedItems;
//        }

//        void checked(int position) {
//
//        }
//            if (mCheckedMap.get(position)) {
//                mCheckedMap.put(position, false);
//            } else {
//                mCheckedMap.put(position, true);
//            }
//
//            notifyDataSetChanged();
//        }

        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View row = convertView;

            if (row == null) {
                LayoutInflater inflater = getLayoutInflater();
                row = inflater.inflate(R.layout.list_item_portfolio_2, parent, false);
            }

            TextView tv_portf_numb = (TextView) row.findViewById(R.id.tv_portf_numb);
            tv_portf_numb.setText(Integer.toString(position + 1));

            TextView tv_prof = (TextView) row.findViewById(R.id.tv_prof);
            tv_prof.setText(String.format("%.2f", portfoliosList.get(position).getExpectedReturn()));
//            tv_prof.setText(Double.toString(portfoliosList.get(position).getExpectedReturn()));

            TextView tv_risk = (TextView) row.findViewById(R.id.tv_risk);
            tv_risk.setText(String.format("%.2f", portfoliosList.get(position).getRisk()));
//            tv_risk.setText(Double.toString(portfoliosList.get(position).getRisk()));

            return row;
        }


    }
}
