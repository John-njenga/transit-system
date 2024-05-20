package com.example.nots;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class notssignup extends AppCompatActivity {
    EditText name, email, pass, confpass;
    TextView log;
    Button btnsignup;

    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notssignup);

        name = findViewById(R.id.edtnamesign);
        email = findViewById(R.id.edemailsign);
        pass = findViewById(R.id.edtpasssign);
        confpass = findViewById(R.id.edtconpass);
        log = findViewById(R.id.logtxt);
        btnsignup = findViewById(R.id.btnsign);

        auth = FirebaseAuth.getInstance();

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
            }
        });
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(notssignup.this, Notslogin.class));
            }
        });
    }
    private void createUser(){

        String nameTxt = name.getText().toString();
        String emailTxt = email.getText().toString();
        String passTxt = pass.getText().toString();
        String confpassTxt = confpass.getText().toString();

        if (TextUtils.isEmpty(emailTxt)) {
            email.setError("Email field is empty");
            email.requestFocus();
        } else if (TextUtils.isEmpty(nameTxt)) {
            name.setError("Name field is empty");
            name.requestFocus();
        } else if (TextUtils.isEmpty(passTxt)) {
            pass.setError("Password field is empty");
            pass.requestFocus();
        } else if (TextUtils.isEmpty(confpassTxt)) {
            confpass.setError("Field is empty");
            confpass.requestFocus();
        } else if (!passTxt.equals(confpassTxt)) {
            Toast.makeText(notssignup.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
        } else {
            auth.createUserWithEmailAndPassword(emailTxt, passTxt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(notssignup.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(notssignup.this, Notslogin.class));
                    } else {
                        Toast.makeText(notssignup.this, "Registration Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}


