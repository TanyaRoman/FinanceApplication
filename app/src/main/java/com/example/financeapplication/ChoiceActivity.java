package com.example.financeapplication;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;
//import androidx.constraintlayout.widget.ConstraintLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.financeapplication.logic.formationOfAnInvestmentPortfolio.ApiService;
//import com.example.financeapplication.logic.coingecko.domain.Coins.CoinList;
import com.litesoftwares.coingecko.domain.Coins.CoinList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChoiceActivity extends AppCompatActivity {

    ListView lv;

    private ArrayList<String> criptoList = new ArrayList<>();
    private MyArrayAdapter myArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choice);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        lv = (ListView) findViewById(R.id.lv_choice);

        try {
            initList();
        } catch (IOException e) {
            e.printStackTrace();
        }

        myArrayAdapter = new MyArrayAdapter(this, R.layout.list_item_choice,
                android.R.id.text1, criptoList);

        lv.setAdapter(myArrayAdapter);
        lv.setOnItemClickListener(myOnItemClickListener);
    }

    public void onClick(View view) {
        String result = "";
        List<String> resultList = myArrayAdapter.getCheckedItems();
        for (int i = 0; i < resultList.size(); i++) {
            result += String.valueOf(resultList.get(i)) + "\n";
        }

        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG)
                .show();
    }

    private void initList() throws IOException {
        ApiService apiService = new ApiService();

        apiService.addData();
        List<CoinList> coins = apiService.getPopularCoins();

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

        private SparseBooleanArray mCheckedMap = new SparseBooleanArray();

        MyArrayAdapter(Context context, int resource,
                       int textViewResourceId, List<String> objects) {
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
                    (checkedItems).add(criptoList.get(i));
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
            checkedTextView.setText(criptoList.get(position));

            Boolean checked = mCheckedMap.get(position);
            if (checked != null) {
                checkedTextView.setChecked(checked);
            }

            return row;
        }
    }
}
