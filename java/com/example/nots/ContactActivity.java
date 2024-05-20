package com.example.nots;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.auth.FirebaseAuth;

public class ContactActivity extends AppCompatActivity {
    private EditText editTextName, editTextEmail, editTextSubject, editTextMessage;
    private Button buttonSend;
    FirebaseAuth firebaseAuth;
    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home, location, detail, cancel, customer, about, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        firebaseAuth= FirebaseAuth.getInstance();

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextSubject = findViewById(R.id.editTextSubject);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonsend);

        drawerLayout=findViewById(R.id.drawerlayout);
        menu=findViewById(R.id.menu);
        home=findViewById(R.id.bill);
        location=findViewById(R.id.location);
        detail=findViewById(R.id.details);
        cancel=findViewById(R.id.cancel);
        customer=findViewById(R.id.customer);
        about=findViewById(R.id.about);
        logout=findViewById(R.id.logout);

        firebaseAuth = FirebaseAuth.getInstance();

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });



        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, Notslogin.class));
        }

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(ContactActivity.this,Home.class);

            }
        });
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(ContactActivity.this,EventActivity.class);

            }
        });
        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(ContactActivity.this,ticketdetails.class);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(ContactActivity.this,CancelActivity.class);

            }
        });
        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();

            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(ContactActivity.this,aboutus.class);

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                finish();
                redirectActivity(ContactActivity.this,Notslogin.class);
            }
        });
    }
    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }

    }
    public static void redirectActivity(Activity activity, Class secondActivity){
        Intent intent= new Intent(activity,secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }
    protected void onPause(){
        super.onPause();
        closeDrawer(drawerLayout);
    }
    private void sendEmail() {
        String recipient = "info.nairobitransit47@gmail.com";
        String subject = editTextSubject.getText().toString();
        String message = "Name: " + editTextName.getText().toString() + "\n" +
                "Email: " + editTextEmail.getText().toString() + "\n" +
                "Message: " + editTextMessage.getText().toString();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{recipient});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(intent, "Choose an Email client"));

            clearTextFields();

        } else {
            // Handle case where no email client is available
            Toast.makeText(this, "No email client installed", Toast.LENGTH_SHORT).show();
        }
    }
    private void clearTextFields() {
        editTextName.setText("");
        editTextEmail.setText("");
        editTextSubject.setText("");
        editTextMessage.setText("");
    }

}