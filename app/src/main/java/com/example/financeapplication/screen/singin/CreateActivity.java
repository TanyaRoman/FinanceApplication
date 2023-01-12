package com.example.financeapplication.screen.singin;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.financeapplication.screen.profile.MainPageActivity;
import com.example.financeapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CreateActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tv;
    Button btn_input;
    Button btn_create_go;
    EditText et_email;
    EditText et_password;
    CheckBox cb_remember;

    private FirebaseAuth mAuth;
    private static final String TAG = "EmailPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mAuth = FirebaseAuth.getInstance();

        tv = (TextView) findViewById(R.id.text_header);
        btn_input = (Button) findViewById(R.id.btn_input);
        btn_create_go = (Button) findViewById(R.id.btn_create_go);
        et_email = (EditText) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);
        cb_remember = (CheckBox) findViewById(R.id.cb_remember);

        tv.setText(R.string.create);
        btn_input.setText(R.string.create_profile);
        btn_create_go.setText(R.string.log_in_profile);

        btn_input.setOnClickListener(this);
        btn_create_go.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent i;
        switch (view.getId()){
            case R.id.btn_input:
                createAcc();
//                if (createAcc()){
//                    System.out.println("create acc");
//                    i = new Intent(this, MainPageActivity.class);
//                    startActivity(i);
//                    break;
//                }
            case R.id.btn_create_go:
                i = new Intent(this, LoginActivity.class);
                startActivity(i);
                break;
        }
    }

    private void createAcc (){
//        final boolean[] status = {false};
//        FirebaseAuth.getInstance()
//                .createUserWithEmailAndPassword(et_email.getText().toString(), et_password.getText().toString())
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful()) {
//                            status[0] = true;
//                        }
//                    }
//                });

        mAuth.createUserWithEmailAndPassword(et_email.getText().toString(), et_password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent i = new Intent(CreateActivity.this, MainPageActivity.class);
                            startActivity(i);
//                            status[0] = true;
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CreateActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

//        for (boolean s: status) {
//            if (!s)
//                return false;
//        }
//        return true;
    }

}
