package com.exeinformatique.cours5;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.exeinformatique.cours5.notification.NotificationService;
import com.exeinformatique.cours5.notification.model.MessageModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_main);
        setListener();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void startService(){
        Intent serviceIntent = new Intent(this, NotificationService.class);
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    private void setListener(){
        findViewById(R.id.btn_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
                sendUserToSignUpOrLoginActivity();
                sendMessage();
            }
        });
    }

    private void sendMessage(){
        EditText editTextMessage = findViewById(R.id.editTextMessage);
        MessageModel messageModel = new MessageModel(editTextMessage.getText().toString(),
                mAuth.getCurrentUser().getEmail());
        database.collection("Notification").add(messageModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()){
                    Toast.makeText(
                            getApplicationContext(),
                            "Message Sent",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });
    }

    private void updateUI(FirebaseUser currentUser){
        if (currentUser == null){
            sendUserToSignUpOrLoginActivity();
        } else {
            startService();
        }
    }

    private void sendUserToSignUpOrLoginActivity(){
        Intent sendToSignUpOrLogin = new Intent(this, SignUpOrLoginActivity.class);
        startActivity(sendToSignUpOrLogin);
    }

    private void logOut(){
        mAuth.signOut();
    }
}
