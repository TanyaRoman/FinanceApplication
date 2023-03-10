package com.example.financeapplication.screen.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.financeapplication.screen.portfolio.ChoiceActivity;
import com.example.financeapplication.R;
import com.example.financeapplication.screen.singin.LoginCreateActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainPageActivity extends AppCompatActivity implements View.OnClickListener{

    Button btn_profile;
    Button btn_portfolio;
    Button btn_exit;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        mAuth = FirebaseAuth.getInstance();

        btn_profile = (Button) findViewById(R.id.btn_profile);
        btn_portfolio = (Button) findViewById(R.id.btn_portfolio);
        btn_exit = (Button) findViewById(R.id.btn_exit);

        btn_profile.setOnClickListener(this);
        btn_portfolio.setOnClickListener(this);
        btn_exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent i;
        switch (view.getId()){
            case R.id.btn_profile:
                i = new Intent(this, ProfileActivity.class);
                startActivity(i);
                break;
            case R.id.btn_portfolio:
                i = new Intent(this, ChoiceActivity.class);
                startActivity(i);
                break;
            case R.id.btn_exit:
                i = new Intent(this, LoginCreateActivity.class);
                startActivity(i);
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            System.out.println("ups");
        }
    }

}
