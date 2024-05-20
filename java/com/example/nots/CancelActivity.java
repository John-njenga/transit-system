package com.example.nots;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.auth.FirebaseAuth;

public class CancelActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home, location, detail, cancel, customer, about, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel);

        firebaseAuth= FirebaseAuth.getInstance();

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
                redirectActivity(CancelActivity.this,Home.class);

            }
        });
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(CancelActivity.this,EventActivity.class);

            }
        });
        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(CancelActivity.this,ticketdetails.class);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();

            }
        });
        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(CancelActivity.this,ContactActivity.class);

            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(CancelActivity.this,aboutus.class);

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                finish();
                redirectActivity(CancelActivity.this,Notslogin.class);
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

}