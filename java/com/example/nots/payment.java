package com.example.nots;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.Manifest;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class payment extends AppCompatActivity {

    // Views for displaying details and user input
    private TextView totalPriceTextView;
    private TextView totalSeatsTextView;
    private TextView carRegNoTextView;
    private TextView carRouteTextView;
    private TextView destinationTextView;
    private EditText nameEditText;
    private EditText phoneNumberEditText;
    private EditText addressEditText;
    private Button payButton, buttonB;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private String totalCost;

    private String totalSeats;

    // Other variables for data passing
    private String CarRegNo;
    private String CarRoute;
    private String Destination;

    private static final int REQUEST_CALL_PHONE_PERMISSION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize DatabaseReference
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Initialize views
        totalPriceTextView = findViewById(R.id.total_price_text_view);
        totalSeatsTextView = findViewById(R.id.total_seats_text_view);
        carRegNoTextView = findViewById(R.id.car_reg_no_text_view);
        carRouteTextView = findViewById(R.id.car_route_text_view);
        destinationTextView = findViewById(R.id.destination_text_view);
        nameEditText = findViewById(R.id.name_edit_text);
        phoneNumberEditText = findViewById(R.id.phone_number_edit_text);
        addressEditText = findViewById(R.id.address_edit_text);
        payButton = findViewById(R.id.pay_button);
        buttonB = findViewById(R.id.buttonB);


        CarRegNo = getIntent().getStringExtra("CarRegNo");
        CarRoute = getIntent().getStringExtra("CarRoute");
        Destination = getIntent().getStringExtra("Destination");
        totalCost = getIntent().getStringExtra("TOTALCOST");
        totalSeats = getIntent().getStringExtra("TOTALSEAT");


        // Retrieve data passed from previous activity
        String totalCost = getIntent().getStringExtra("TOTALCOST");
        String totalSeats = getIntent().getStringExtra("TOTALSEAT");
        String carRegNo = getIntent().getStringExtra("CarRegNo");
        String carRoute = getIntent().getStringExtra("CarRoute");
        String destination = getIntent().getStringExtra("Destination");

        // Display details
        totalPriceTextView.setText("Total Cost: " + totalCost);
        totalSeatsTextView.setText("Seat Number(s): " + totalSeats);
        carRegNoTextView.setText("Car Registration No: " + carRegNo);
        carRouteTextView.setText("Car Route: " + carRoute);
        destinationTextView.setText("Destination: " + destination);

        // Set click listener for pay button
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call method to handle payment
                launchMpesaSTK();
                // Enable Button B
                buttonB.setEnabled(true);

            }
        });
        buttonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processPayment();
            }
        });
    }
    private void launchMpesaSTK() {
        // Check if permission is not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // Request the permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE_PERMISSION);
        } else {
            // Permission already granted, launch M-Pesa STK
            launchUSSDCode();
        }
    }

    private void launchUSSDCode() {
        String ussdCode = "tel: *334%23 ";
        Uri uri = Uri.parse("tel:" + Uri.encode(ussdCode + ","));
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(ussdCode));
        try {
            startActivity(intent);
        } catch (SecurityException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to launch USSD code", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PHONE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, launch M-Pesa STK
                launchUSSDCode();
            } else {
                // Permission denied
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    // Method to handle payment processing
    private void processPayment() {
        // Retrieve user input
        String name = nameEditText.getText().toString().trim();
        String phoneNumber = phoneNumberEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();

        // Validate user input
        if (TextUtils.isEmpty(name)) {
            nameEditText.setError("Please enter your name");
            nameEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(phoneNumber)) {
            phoneNumberEditText.setError("Please enter your phone number");
            phoneNumberEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(address)) {
            addressEditText.setError("Please enter your address");
            addressEditText.requestFocus();
            return;
        }

        // Process payment (you can add your payment gateway integration logic here)

        // Display success message
        Toast.makeText(this, "Payment Successful!", Toast.LENGTH_SHORT).show();

        Ticket ticket= new Ticket (String.valueOf(totalCost),String.valueOf(totalSeats),String.valueOf(CarRegNo),String.valueOf(CarRoute),String.valueOf(Destination));

        FirebaseUser user = firebaseAuth.getCurrentUser();
        // Set value in Firebase database under user's node
        databaseReference.child(user.getUid()).child("SeatDetails").setValue(ticket);
        // Redirect to ticket activity or any other desired action
        Intent intent = new Intent(payment.this, ticketdetails.class);

// Pass data to ticket details activity
        intent.putExtra("TOTALCOST", String.valueOf(totalCost));
        intent.putExtra("TOTALSEAT", String.valueOf(totalSeats));
        intent.putExtra("CarRegNo", String.valueOf(CarRegNo));
        intent.putExtra("CarRoute", String.valueOf(CarRoute));
        intent.putExtra("Destination", String.valueOf(Destination));
        intent.putExtra("Name", name);
        intent.putExtra("PhoneNumber", phoneNumber);
        intent.putExtra("Address", address);

// Start ticket details activity
        startActivity(intent);
        finish(); // Finish current activity to prevent user from coming back to this payment screen
    }
}
