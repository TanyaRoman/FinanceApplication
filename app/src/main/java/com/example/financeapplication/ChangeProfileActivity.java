package com.example.financeapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ChangeProfileActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_profile);

        btn_save = (Button) findViewById(R.id.btn_save);

        btn_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent i;
        switch (view.getId()) {
            case R.id.btn_save:
                i = new Intent(this, MainPageActivity.class);
                startActivity(i);
                break;
        }
    }
}
