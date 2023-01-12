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
import com.example.financeapplication.logic.formationOfAnInvestmentPortfolio.persistence.entity.vue.Share;
import com.example.financeapplication.screen.profile.MainPageActivity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class PortfolioDetailingActivity extends AppCompatActivity implements View.OnClickListener{

    ListView lv;
    Button btn_save_p;
    TextView exp;
    TextView risk;
    private List<Share> coinsList;
    private MyArrayAdapterCoins myArrayAdapterCoins;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.portfolio_detailing);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Bundle arguments = getIntent().getExtras();

        if (!arguments.equals(null)) {
            String data = arguments.get("criptoList").toString();
            try {
                coinsList = new ObjectMapper().readValue(data, new TypeReference<List<Share>>() {});
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            exp = (TextView) findViewById(R.id.tv_ex);
            exp.setText(arguments.get("exp").toString());

            risk = (TextView) findViewById(R.id.tv_risk);
            risk.setText(arguments.get("risk").toString());
        }

        lv = (ListView) findViewById(R.id.lv_coin_det);

        btn_save_p = (Button) findViewById(R.id.btn_save_p);
        btn_save_p.setOnClickListener(this);

        myArrayAdapterCoins = new PortfolioDetailingActivity.MyArrayAdapterCoins(this, R.layout.list_item_part,
                android.R.id.text1, coinsList);
        lv.setAdapter(myArrayAdapterCoins);
    }

    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_save_p:
                Intent i = new Intent(this, MainPageActivity.class);
                startActivity(i);
                break;
        }
    }


    private class MyArrayAdapterCoins extends ArrayAdapter<Share> {

        MyArrayAdapterCoins(Context context, int resource, int textViewResourceId, List<Share> objects) {
            super(context, resource, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View row = convertView;

            if (row == null) {
                LayoutInflater inflater = getLayoutInflater();
                row = inflater.inflate(R.layout.list_item_part, parent, false);
            }

            TextView tv_cripto_name = (TextView) row.findViewById(R.id.tv_cripto_name);
            tv_cripto_name.setText(coinsList.get(position).getId());

            TextView tv_prof = (TextView) row.findViewById(R.id.tv_prof);
            tv_prof.setText(String.format("%.2f", coinsList.get(position).getCount()));

            return row;
        }
    }
}
