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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.financeapplication.R;
import com.example.financeapplication.logic.formationOfAnInvestmentPortfolio.ApiService;
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

    private ArrayList<String> filterList = new ArrayList<>();
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

        lv.setAdapter(myArrayAdapter);
        lv.setOnItemClickListener(myOnItemClickListener);

        et_find.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                ChoiceActivity.this.myArrayAdapter.getFilter().filter(editable.toString());
            }
        });
    }

    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_calculate:
                Intent i = new Intent(this, Result2Activity.class);
                String data = "";
                String apiServ = "";
                try {
                    data = new ObjectMapper().writeValueAsString(myArrayAdapter.getCheckedItems());
                    apiServ = new ObjectMapper().writeValueAsString(apiService);

                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                i.putExtra("criptoList", data);
                i.putExtra("apiServ", apiServ);
                startActivity(i);
                break;
        }
    }

    private void initList(ApiService apiService) throws IOException {

        apiService.addData();
        coins = apiService.getPopularCoins();
        for (CoinList cl: coins) {
            criptoList.add(cl.getName());
        }
        filterList.addAll(criptoList);
    }




    AdapterView.OnItemClickListener myOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            myArrayAdapter.toggleChecked(position);
        }
    };

    private class MyArrayAdapter extends ArrayAdapter<String> implements Filterable {

        private SparseBooleanArray mCheckedMap = new SparseBooleanArray();
        private ModelFilter filter;
        private List<String> allModelItemsArray;
        private List<String> filteredModelItemsArray;

        MyArrayAdapter(Context context, int resource,
                       int textViewResourceId, List<String> objects) {
            super(context, resource, textViewResourceId, objects);

            this.allModelItemsArray = new ArrayList<String>();
            allModelItemsArray.addAll(objects);
            this.filteredModelItemsArray = new ArrayList<String>();
            filteredModelItemsArray.addAll(objects);
            getFilter();
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
            checkedTextView.setText(filteredModelItemsArray.get(position));

            Boolean checked = mCheckedMap.get(position);
            if (checked != null) {
                checkedTextView.setChecked(checked);
            }

            return row;
        }

        @Override
        public Filter getFilter() {
            if (filter == null){
                filter  = new ModelFilter();
            }
            return filter;
        }
        class ViewHolder {
            protected TextView text;
        }

        private class ModelFilter extends Filter {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                System.out.println("filter");
                System.out.println(constraint);
                constraint = constraint.toString().toLowerCase();
                FilterResults result = new FilterResults();
                if (constraint.toString().length() > 0) {
                    ArrayList<String> filteredItems = new ArrayList<String>();

                    System.out.println(allModelItemsArray);
                    for (int i = 0, l = allModelItemsArray.size(); i < l; i++) {
                        String m = allModelItemsArray.get(i);
                        if (m.toLowerCase().contains(constraint))
                            filteredItems.add(m);
                    }
                    System.out.println(filteredItems);
                    result.count = filteredItems.size();
                    result.values = filteredItems;
                } else {
                    synchronized (this) {
                        result.values = allModelItemsArray;
                        result.count = allModelItemsArray.size();
                    }
                }
                return result;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                filteredModelItemsArray = (ArrayList<String>) results.values;
                notifyDataSetChanged();
                clear();
                for (int i = 0, l = filteredModelItemsArray.size(); i < l; i++)
                    add(filteredModelItemsArray.get(i));
                notifyDataSetInvalidated();
            }
        }
    }
}
