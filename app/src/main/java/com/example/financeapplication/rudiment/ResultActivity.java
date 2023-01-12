//package com.example.financeapplication;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.StrictMode;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.financeapplication.logic.formationOfAnInvestmentPortfolio.ApiService;
//import com.example.financeapplication.logic.formationOfAnInvestmentPortfolio.persistence.entity.vue.Pers;
//import com.example.financeapplication.logic.formationOfAnInvestmentPortfolio.persistence.entity.vue.Portfolios;
//import com.example.financeapplication.screen.singin.LoginCreateActivity;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class ResultActivity extends AppCompatActivity implements View.OnClickListener{
//
//    ListView lv;
//    Button btn_back;
//    private List<Portfolios> portfoliosList;
//    private MyArrayAdapterPortfolio myArrayAdapter;
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.result);
//
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
//
//        Bundle arguments = getIntent().getExtras();              // what is it..?
//
//        if ((!arguments.equals(null)) || (portfoliosList.equals(null))) {
//            String data = arguments.get("criptoList").toString();
//            ApiService apiService = new ApiService();            // осуждаю
//
//            ArrayList<String> criptoList = null;
//            try {
//                criptoList = new ObjectMapper().readValue(data, ArrayList.class);
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
//
//            System.out.println(criptoList);
//            portfoliosList = new ArrayList<>();
//            try {
//                initList(apiService, criptoList);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            System.out.println("portfoliosList");
//            System.out.println(portfoliosList.size());
//        }
//
//        // it doesn't work. i tried to do great code...
////        String apiServ = arguments.get("apiService").toString();
////        try {
////            apiService = new ObjectMapper().readValue(apiServ, ApiService.class);
////        } catch (JsonProcessingException e) {
////            e.printStackTrace();
////        }
//
//
//
//        lv = (ListView) findViewById(R.id.lv_portfolios);
//
//        btn_back = (Button) findViewById(R.id.btn_back);
//        btn_back.setOnClickListener(this);
//
//        myArrayAdapter = new ResultActivity.MyArrayAdapterPortfolio(this, R.layout.list_item_portfolio,
//                android.R.id.text1, portfoliosList);
//
//        lv.setAdapter(myArrayAdapter);
//        lv.setOnItemClickListener(myOnItemClickListener);
//    }
//
//    public void onClick(View view) {
//
//        switch (view.getId()){
//            case R.id.btn_back:
////                Intent i = new Intent(this, ChoiceActivity.class);
//                Intent i = new Intent(this, LoginCreateActivity.class);
//                startActivity(i);
//                break;
//            case R.id.btn_detail:
//                System.out.println("i'm detail portfolio");
//                System.out.println();
//        }
//    }
//
//    private void initList(ApiService apiService, ArrayList<String> criptoList) throws IOException {
//
//        //it will change, when i set id
//        apiService.addData();
////        List<String> s = new ArrayList<>();
////        s.add("algorand");
////        s.add("bitcoin");
////        s.add("cardano");
//
////        Pers result = apiService.getPortfolios(s);
//        Pers result = apiService.getPortfolios(criptoList);
//        for (Portfolios p: result.getPortfolios()) {
//            portfoliosList.add(p);
//        }
//    }
//
//    AdapterView.OnItemClickListener myOnItemClickListener = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////            onClick(view);
////            getCheckedItems(position);
//            portfoliosList.get(position);
//        }
//    };
//
//
//    private class MyArrayAdapterPortfolio extends ArrayAdapter<Portfolios> {
//
//        Portfolios portfolios = new Portfolios();
//
//        MyArrayAdapterPortfolio(Context context, int resource, int textViewResourceId, List<Portfolios> objects) {
//            super(context, resource, textViewResourceId, objects);
//        }
//
////        Portfolios getCheckedItems(int position) {
////            return checkedItems;
////        }
//
////        void toggleChecked(int position) {
////            if (mCheckedMap.get(position)) {
////                mCheckedMap.put(position, false);
////            } else {
////                mCheckedMap.put(position, true);
////            }
////
////            notifyDataSetChanged();
////        }
//
//        @Override
//        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
//            View row = convertView;
//
//            if (row == null) {
//                LayoutInflater inflater = getLayoutInflater();
//                row = inflater.inflate(R.layout.list_item_portfolio_2, parent, false);
//            }
//
////            System.out.println("my portfolio's number");
////            System.out.println(position);
////            System.out.println(Integer.toString(position + 1));
//
//            TextView tv_portf_numb = (TextView) row.findViewById(R.id.tv_portf_numb);
//            tv_portf_numb.setText(Integer.toString(position + 1));
//
//            TextView tv_prof = (TextView) row.findViewById(R.id.tv_prof);
////            System.out.println("/////////////");
////            System.out.println(String.format("%.2f", portfoliosList.get(position).getExpectedReturn()));
////            System.out.println((String.format("%.2f", portfoliosList.get(position).getExpectedReturn())).getClass().getName());
////            System.out.println((Double.toString(portfoliosList.get(position).getExpectedReturn())).getClass().getName());
//            tv_prof.setText(String.format("%.2f", portfoliosList.get(position).getExpectedReturn()));
////            tv_prof.setText(Double.toString(portfoliosList.get(position).getExpectedReturn()));
//
//            TextView tv_risk = (TextView) row.findViewById(R.id.tv_risk);
//            tv_risk.setText(String.format("%.2f", portfoliosList.get(position).getRisk()));
////            tv_risk.setText(Double.toString(portfoliosList.get(position).getRisk()));
//
//            return row;
//        }
//    }
//}
