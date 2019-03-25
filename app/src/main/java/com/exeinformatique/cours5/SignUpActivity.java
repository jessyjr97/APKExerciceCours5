package com.exeinformatique.cours5;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        auth = FirebaseAuth.getInstance();
        setListener();
    }

    private void setListener(){
        findViewById(R.id.btn_gotoSignup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpUser();
            }
        });
    }

    private void signUpUser(){
        EditText userEmail = findViewById(R.id.txt_loginEmail);
        EditText password = findViewById(R.id.txt_loginPassword);
        EditText passwordConfirm = findViewById(R.id.txt_signupPasswordConfirm);
        if (password.getText().toString() != passwordConfirm.getText().toString()){
            Pain("Password doesn't match.");
            return;
        }
        auth.createUserWithEmailAndPassword(
                userEmail.getText().toString(),
                password.getText().toString()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    sendUserToMainActivity();
                } else {
                    Pain("Failed to sign up.");
                }
            }
        });
    }

    private void Pain(String text){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    private void sendUserToMainActivity(){
        Intent sendToMainActivity = new Intent(this, MainActivity.class);
        startActivity(sendToMainActivity);
    }
}
