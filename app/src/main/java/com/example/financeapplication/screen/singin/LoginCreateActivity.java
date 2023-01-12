package com.example.financeapplication.screen.singin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.financeapplication.R;

public class LoginCreateActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_login;
    Button btn_sing_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.login_create);

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_sing_in = (Button) findViewById(R.id.btn_create);

        btn_login.setOnClickListener(this);
        btn_sing_in.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent i;
        switch (view.getId()){
            case R.id.btn_login:
                i = new Intent(this, LoginActivity.class);
                startActivity(i);
                break;
            case R.id.btn_create:
                i = new Intent(this, CreateActivity.class);
                startActivity(i);
                break;
        }
    }
}