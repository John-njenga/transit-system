package com.example.nots;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Notslogin extends AppCompatActivity {
    EditText etemail,etpass;
    TextView sign;
    Button btnlogin;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notslogin);


        etemail=findViewById(R.id.edtlogemail);
        etpass=findViewById(R.id.edtlogpass);
        sign=findViewById(R.id.signup);
        btnlogin=findViewById(R.id.btnlog);

        auth=FirebaseAuth.getInstance();

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();

            }
        });
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Notslogin.this,notssignup.class));
            }
        });
    }
    public void loginUser() {

        String email = etemail.getText().toString();
        String pass = etpass.getText().toString();

        if (TextUtils.isEmpty(email)) {
            etemail.setError("Email field is empty");
            etemail.requestFocus();
        } else if (TextUtils.isEmpty(pass)) {
            etpass.setError("Password field is empty");
            etpass.requestFocus();
        } else {
            auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(Notslogin.this, "User Login successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Notslogin.this,Home.class));
                    } else {
                        Toast.makeText(Notslogin.this, "Login Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}