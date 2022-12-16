package com.example.financeapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tv;
    Button btn_input;
    Button btn_create_go;
    EditText et_email;
    EditText et_password;
    CheckBox cb_remember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        tv = (TextView) findViewById(R.id.text_header);
        btn_input = (Button) findViewById(R.id.btn_input);
        btn_create_go = (Button) findViewById(R.id.btn_create_go);
        et_email = (EditText) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);
        cb_remember = (CheckBox) findViewById(R.id.cb_remember);

        tv.setText(R.string.log_in);
        btn_input.setText(R.string.input);
        btn_create_go.setText(R.string.havent_profile);

        btn_input.setOnClickListener(this);
        btn_create_go.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent i;
        switch (view.getId()){
            case R.id.btn_input:
                i = new Intent(this, MainPageActivity.class);
                startActivity(i);
                break;
            case R.id.btn_create_go:
                i = new Intent(this, CreateActivity.class);
                startActivity(i);
                break;
        }
    }
}
