package com.tekdevisal.skype_clone;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText email, password;
    String email_string, pass_string;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        findViewById(R.id.submit).setOnClickListener(v -> {

            firebaseAuth = FirebaseAuth.getInstance();

            email_string = email.getText().toString().trim();
            pass_string = password.getText().toString().trim();

            firebaseAuth.signInWithEmailAndPassword(email_string,pass_string).addOnCompleteListener(Login.this, task -> {
                if(!task.isSuccessful()){

                }else{
                    Intent gotohome = new Intent(Login.this,Home.class);
                    startActivity(gotohome);
                }
            });

        });

    }
}
