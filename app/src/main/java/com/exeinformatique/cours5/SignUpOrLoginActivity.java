package com.exeinformatique.cours5;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.common.oob.SignUp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpOrLoginActivity extends AppCompatActivity {
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_or_login);
        auth = FirebaseAuth.getInstance();
    }

    private void SetListener(){
        findViewById(R.id.btn_gotoSignup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToLoginActivity();
            }
        });
        findViewById(R.id.btn_gotoLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToSignupActivity();
            }
        });
    }

    private void sendToSignupActivity(){
        Intent sendToSignup = new Intent(this, SignUp.class);
        startActivity(sendToSignup);
    }

    private void sendToLoginActivity(){
        Intent sendToLogin = new Intent(this, SignUp.class);
        startActivity(sendToLogin);
    }
}
