package com.example.financeapplication.screen.portfolio;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.financeapplication.R;
import com.example.financeapplication.logic.formationOfAnInvestmentPortfolio.ApiService;
import com.example.financeapplication.logic.formationOfAnInvestmentPortfolio.persistence.entity.vue.Coins;
import com.example.financeapplication.logic.formationOfAnInvestmentPortfolio.persistence.entity.vue.Pers;
import com.example.financeapplication.screen.singin.LoginCreateActivity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CoinsDetailingActivity extends AppCompatActivity implements View.OnClickListener{

    ListView lv;
    Button btn_back;
    private List<Coins> coinsList;
    private MyArrayAdapterCoins myArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.coins_detailing);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Bundle arguments = getIntent().getExtras();              // what is it..?
        String data = arguments.get("criptoList").toString();

        // it doesn't work. i tried to do great code...
//        String apiServ = arguments.get("apiService").toString();
//        try {
//            apiService = new ObjectMapper().readValue(apiServ, ApiService.class);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
        ApiService apiService = new ApiService();

        ArrayList<String> criptoList = null;
        try {
            criptoList = new ObjectMapper().readValue(data, ArrayList.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println(criptoList);
        System.out.println("criptoList");

        lv = (ListView) findViewById(R.id.lv_coin_det);
        coinsList = new ArrayList<>();

        btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);

        try {
            initList(apiService, criptoList);
        } catch (IOException e) {
            e.printStackTrace();
        }

        myArrayAdapter = new CoinsDetailingActivity.MyArrayAdapterCoins(this, R.layout.list_item_portfolio,
                android.R.id.text1, coinsList);
        lv.setAdapter(myArrayAdapter);
    }

    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_calculate:
//                Intent i = new Intent(this, ResultActivity.class);
                Intent i = new Intent(this, LoginCreateActivity.class);
                startActivity(i);
                break;
        }
    }

    private void initList(ApiService apiService, ArrayList<String> criptoList) throws IOException {

        //it will change, when i set id
        apiService.addData();
        List<String> s = new ArrayList<>();
        s.add("algorand");
        s.add("bitcoin");
        s.add("cardano");

        System.out.println("--------------------");
        Pers result = apiService.getPortfolios(s);
        System.out.println();
        System.out.println(result.getCoins().size());
        System.out.println(result.getPortfolios().size());
        for (Coins c: result.getCoins()) {
            System.out.println("**");
            coinsList.add(c);
        }
    }


    private class MyArrayAdapterCoins extends ArrayAdapter<Coins> {

        MyArrayAdapterCoins(Context context, int resource, int textViewResourceId, List<Coins> objects) {
            super(context, resource, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View row = convertView;
            System.out.println("position");
            System.out.println(position);

            if (row == null) {
                LayoutInflater inflater = getLayoutInflater();
                row = inflater.inflate(R.layout.list_item_cripto, parent, false);
            }

            TextView tv_portf_numb = (TextView) row.findViewById(R.id.tv_cripto_name);
            tv_portf_numb.setText(coinsList.get(position).getId());
            System.out.println("coinsList.get(position).getId()");
            System.out.println(coinsList.get(position).getId());

            TextView tv_prof = (TextView) row.findViewById(R.id.tv_prof);
            tv_prof.setText(Double.toString(coinsList.get(position).getExpectedReturn()));
            System.out.println("Double.toString(coinsList.get(position).getExpectedReturn())");
            System.out.println(Double.toString(coinsList.get(position).getExpectedReturn()));

            TextView tv_risk = (TextView) row.findViewById(R.id.tv_risk);
            tv_risk.setText(Double.toString(coinsList.get(position).getRisk()));
            System.out.println("Double.toString(coinsList.get(position).getRisk())");
            System.out.println(Double.toString(coinsList.get(position).getRisk()));

            return row;
        }
    }
}
