package com.example.financeapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    Button btn_change_dt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        btn_change_dt = (Button) findViewById(R.id.btn_change_dt);

        btn_change_dt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent i;
        switch (view.getId()){
            case R.id.btn_change_dt:
                i = new Intent(this, ChangeProfileActivity.class);
                startActivity(i);
                break;
        }
    }
}
