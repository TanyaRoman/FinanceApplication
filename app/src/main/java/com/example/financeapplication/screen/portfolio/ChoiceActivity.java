package com.example.financeapplication.screen.portfolio;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
//import androidx.constraintlayout.widget.ConstraintLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.financeapplication.R;
import com.example.financeapplication.logic.formationOfAnInvestmentPortfolio.ApiService;
//import com.example.financeapplication.logic.coingecko.domain.Coins.CoinList;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.litesoftwares.coingecko.domain.Coins.CoinList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChoiceActivity extends AppCompatActivity implements View.OnClickListener{

    ListView lv;
    Button btn_calculate;
    EditText et_find;

    private ArrayList<String> criptoList = new ArrayList<>();
    private List<CoinList> coins;
    private MyArrayAdapter myArrayAdapter;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choice);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        lv = (ListView) findViewById(R.id.lv_choice);
        btn_calculate = (Button) findViewById(R.id.btn_calculate);
        et_find = (EditText) findViewById(R.id.et_find);

        btn_calculate.setOnClickListener(this);
        apiService = new ApiService();
        coins = new ArrayList<>();

        try {
            initList(apiService);
        } catch (IOException e) {
            e.printStackTrace();
        }

        myArrayAdapter = new MyArrayAdapter(this, R.layout.list_item_choice,
                android.R.id.text1, criptoList);
//        myArrayAdapter = new MyArrayAdapter(this, R.layout.list_item_choice,
//                android.R.id.text1, coins);

        lv.setAdapter(myArrayAdapter);
        lv.setOnItemClickListener(myOnItemClickListener);

        et_find.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                 ChoiceActivity.this.myArrayAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().equals("")){
                    // reset listview
                    for (CoinList cl: coins) {
                        criptoList.add(cl.getName());
                    }
                    lv.setAdapter(myArrayAdapter);
                } else {
                    // perform search
                    System.out.println("search");
                    System.out.println(charSequence.toString());
                    System.out.println(criptoList);
                    searchItem(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void onClick(View view) {
//        String result = "";
//        List<String> resultList = myArrayAdapter.getCheckedItems();
//        for (int i = 0; i < resultList.size(); i++) {
//            result += String.valueOf(resultList.get(i)) + "\n";
//        }
//
//        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG)
//                .show();

//        String result = "";
//        List<Integer> resultPosList = myArrayAdapter.getCheckedItemPositions();
//        List<String> idsList = new ArrayList<>();
//        for (Integer i: resultPosList) {
//            idsList.add(coins.get(i).)
//        }
//
//        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG)
//                .show();

        switch (view.getId()){
            case R.id.btn_calculate:
//                Intent i = new Intent(this, ResultActivity.class);
//                Intent i = new Intent(this, CoinsDetailingActivity.class);
                Intent i = new Intent(this, Result2Activity.class);
                String data = "";
                String apiServ = "";
//                Pers result = apiService.getPortfolios(myArrayAdapter.getCheckedItems());
                try {
                    data = new ObjectMapper().writeValueAsString(myArrayAdapter.getCheckedItems());
                    apiServ = new ObjectMapper().writeValueAsString(apiService);

                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
//                System.out.println("--+++++++***");
//                System.out.println(apiService);
//                System.out.println(apiService.toString());
                i.putExtra("criptoList", data);
                i.putExtra("apiServ", apiServ);
//                i.putExtra("apiServ", apiService.toString());
                startActivity(i);
                break;
        }
    }

    public void searchItem(String textToSearch){
        System.out.println("**************");
        System.out.println(criptoList);
        for(CoinList item: coins){
            if(!item.getName().contains(textToSearch)){
                System.out.println("--");
                System.out.println(item.getName());
                System.out.println(textToSearch);
                criptoList.remove(item.getName());
                System.out.println(criptoList);
            }
        }
        System.out.println(criptoList);
//        ArrayList<String> c = criptoList;
//        criptoList.clear();
//        criptoList.addAll(c);
//        this.notifyDataSetChanged();

        myArrayAdapter.notifyDataSetChanged();
//        lv.invalidate();
    }

    private void initList(ApiService apiService) throws IOException {

        apiService.addData();
        coins = apiService.getPopularCoins();
//        List<CoinList> coins = apiService.getPopularCoins();
//
        for (CoinList cl: coins) {
            criptoList.add(cl.getName());
        }
    }

    AdapterView.OnItemClickListener myOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            myArrayAdapter.toggleChecked(position);
        }
    };

    private class MyArrayAdapter extends ArrayAdapter<String> {
//    private class MyArrayAdapter extends ArrayAdapter<CoinList> {

        private SparseBooleanArray mCheckedMap = new SparseBooleanArray();

        MyArrayAdapter(Context context, int resource,
                       int textViewResourceId, List<String> objects) {
//            MyArrayAdapter(Context context, int resource, int textViewResourceId, List<CoinList> objects) {
            super(context, resource, textViewResourceId, objects);

            for (int i = 0; i < objects.size(); i++) {
                mCheckedMap.put(i, false);
            }
        }

        void toggleChecked(int position) {
            if (mCheckedMap.get(position)) {
                mCheckedMap.put(position, false);
            } else {
                mCheckedMap.put(position, true);
            }

            notifyDataSetChanged();
        }

        public List<Integer> getCheckedItemPositions() {
            List<Integer> checkedItemPositions = new ArrayList<>();

            for (int i = 0; i < mCheckedMap.size(); i++) {
                if (mCheckedMap.get(i)) {
                    (checkedItemPositions).add(i);
                }
            }

            return checkedItemPositions;
        }

        List<String> getCheckedItems() {
            List<String> checkedItems = new ArrayList<>();

            for (int i = 0; i < mCheckedMap.size(); i++) {
                if (mCheckedMap.get(i)) {
//                    (checkedItems).add(criptoList.get(i));
                    (checkedItems).add(coins.get(i).getId());
                }
            }

            return checkedItems;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View row = convertView;

            if (row == null) {
                LayoutInflater inflater = getLayoutInflater();
                row = inflater.inflate(R.layout.list_item_choice, parent, false);
            }

            CheckBox checkedTextView = (CheckBox) row.findViewById(R.id.cb_cripto);
//            checkedTextView.setText(criptoList.get(position));
            checkedTextView.setText(coins.get(position).getName());

            Boolean checked = mCheckedMap.get(position);
            if (checked != null) {
                checkedTextView.setChecked(checked);
            }

            return row;
        }
    }
}
