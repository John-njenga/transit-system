package com.example.nots;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ticketdetails extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home, location, detail, cancel, customer, about, logout;
    private TextView timeDateTextView,nameTextView, phoneTextView,addressTextView, carRegNoTextView,carRouteTextView, destinationTextView, totalCostTextView, totalSeatsTextView;

    private static final int SMS_PERMISSION_REQUEST_CODE = 101;

    private EditText phoneNumberEditText;
    private Button sendButton;

    private String ticketDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticketdetails);

        firebaseAuth= FirebaseAuth.getInstance();

        // Get the current time and date
        //Calendar calendar = Calendar.getInstance();

        // Get the current date and time
        Date currentDate = new Date();

        // Combine the formatted time and date
        String formattedDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        String formattedTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String timeDateText = "Date: " + formattedDate + "\n Time: " + formattedTime;

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

        nameTextView = findViewById(R.id.name_text_view);
        phoneTextView = findViewById(R.id.phone_text_view);
        addressTextView=findViewById(R.id.address_text_view);
        carRegNoTextView = findViewById(R.id.car_reg_no_text_view);
        carRouteTextView = findViewById(R.id.car_route_text_view);
        destinationTextView = findViewById(R.id.destination_text_view);
        totalCostTextView = findViewById(R.id.total_cost_text_view);
        totalSeatsTextView = findViewById(R.id.total_seats_text_view);

        // Find the TextView where you want to display the time and date
        timeDateTextView = findViewById(R.id.timeDateTextView);

        // Get data from previous activity
        Intent intent = getIntent();
        String name = intent.getStringExtra("Name");
        String phone = intent.getStringExtra("PhoneNumber");
        String address = intent.getStringExtra("Address");
        String carRegNo = intent.getStringExtra("CarRegNo");
        String carRoute = intent.getStringExtra("CarRoute");
        String destination = intent.getStringExtra("Destination");
        String totalCost = intent.getStringExtra("TOTALCOST");
        String totalSeats = intent.getStringExtra("TOTALSEAT");



        // Set ticket details to TextViews
        nameTextView.setText("Name: " + name);
        phoneTextView.setText("Phone: " + phone);
        addressTextView.setText("Address: " + address);
        carRegNoTextView.setText("Car Reg No: " + carRegNo);
        carRouteTextView.setText("Car Route: " + carRoute);
        destinationTextView.setText("Destination: " + destination);
        totalCostTextView.setText("Total Cost: " + totalCost);
        totalSeatsTextView.setText("Seat Number(s): " + totalSeats);
        timeDateTextView.setText(timeDateText);

        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        sendButton = findViewById(R.id.sendButton);

        // Construct ticket details string
        ticketDetail = "Name: " + name + "\n" +
                "Phone Number: " + phone + "\n" +
                "Address: " + address + "\n" +
                "CarRegNo: " + carRegNo + "\n" +
                "CarRoute: " + carRoute + "\n" +
                "Destination: " + destination + "\n" +
                "Total Cost: " + totalCost + "\n" +
                "Seat Number(s): " + totalSeats ;

        //ticketDetail += "\n" + timeDateText;

        // Set click listener for send button
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ticketDetail == null) {
                    Toast.makeText(ticketdetails.this, "Ticket details not available", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Check SMS permission
                if (ContextCompat.checkSelfPermission(ticketdetails.this, Manifest.permission.SEND_SMS)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission not granted, request it
                    ActivityCompat.requestPermissions(ticketdetails.this,
                            new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_REQUEST_CODE);
                } else {
                    // Permission already granted, send SMS
                    sendSMS();
                }
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
                redirectActivity(ticketdetails.this,Home.class);

            }
        });
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(ticketdetails.this,EventActivity.class);

            }
        });
        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(ticketdetails.this,CancelActivity.class);

            }
        });
        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(ticketdetails.this,ContactActivity.class);

            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(ticketdetails.this,aboutus.class);

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                finish();
                redirectActivity(ticketdetails.this,Notslogin.class);
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
    private void sendSMS() {
        String phoneNumber = phoneNumberEditText.getText().toString().trim();
        if (!phoneNumber.isEmpty()) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, ticketDetail, null, null);
            Toast.makeText(this, "Ticket details sent via SMS ", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, send SMS
                sendSMS();
            } else {
                Toast.makeText(this, "SMS permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

}