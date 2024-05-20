package com.example.nots;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SeatActivity extends AppCompatActivity {

    // GridLayout for the seat layout
    private GridLayout mainGrid;

    // TextViews for displaying total cost and selected seat numbers
    private TextView totalPrice;
    private TextView selectedSeats;

    // Button for booking seats
    private Button buttonBook;

    // FirebaseAuth instance for user authentication
    private FirebaseAuth firebaseAuth;

    // ProgressDialog for showing loading message
    private ProgressDialog progressDialog;

    // DatabaseReference for accessing Firebase Realtime Database
    private DatabaseReference databaseReference;

    // Double variable for seat price and total cost
    private Double seatPrice;
    private Double totalCost = 0.0;

    // List to keep track of selected seat numbers
    private List<Integer> selectedSeatNumbers = new ArrayList<>();

    // Other variables for data passing
    private String CarRegNo;
    private String CarRoute;
    private String Destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat);

        // Initialize FirebaseAuth instance
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize DatabaseReference
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Initialize ProgressDialog
        progressDialog = new ProgressDialog(this);

        // Initialize views
        mainGrid = findViewById(R.id.mainGrid);
        totalPrice = findViewById(R.id.total_cost);
        selectedSeats = findViewById(R.id.total_seats);
        buttonBook = findViewById(R.id.btnconfirm);

        // Retrieve data passed from previous activity
        CarRegNo = getIntent().getStringExtra("CarRegNo");
        CarRoute = getIntent().getStringExtra("CarRoute");
        Destination = getIntent().getStringExtra("Destination");
        seatPrice = getIntent().getDoubleExtra("Price", 80.0);

        // Set event listener for booking button
        buttonBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call method to handle booking
                bookSeats();
            }
        });

        // Call method to set toggle event for seat selection
        setToggleEvent(mainGrid);
        // Listen for changes in the seat status from the database
        listenForSeatStatusChanges();

    }

    // Method to handle booking of seats
    private void bookSeats() {
        // Create PaymentDetail object with total cost and selected seat numbers
        PaymentDetail paymentDetail = new PaymentDetail(String.valueOf(totalCost), selectedSeatNumbers.toString(), String.valueOf(CarRegNo), String.valueOf(CarRoute), String.valueOf(Destination));

        // Get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        // Set value in Firebase database under user's node
        databaseReference.child(user.getUid()).child("SeatDetails").setValue(paymentDetail);

        // Create intent for ticket details activity
        Intent intent = new Intent(SeatActivity.this, payment.class);
        // Pass data to ticket details activity
        intent.putExtra("TOTALCOST", String.valueOf(totalCost));
        intent.putExtra("TOTALSEAT", selectedSeatNumbers.toString());
        intent.putExtra("CarRegNo", String.valueOf(CarRegNo));
        intent.putExtra("CarRoute", String.valueOf(CarRoute));
        intent.putExtra("Destination", String.valueOf(Destination));

        // Start ticket details activity
        startActivity(intent);
        finish(); // Finish current activity to prevent user from coming back to this seat selection screen
    }

    // Method to set toggle event for seat selection
    private void setToggleEvent(GridLayout mainGrid) {
        //Loop all child item of Main Grid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            //You can see , all child item is CardView , so we just cast object to CardView
            final CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Check if the seat is already booked
                    if (isSeatBooked(finalI)) {
                        Toast.makeText(SeatActivity.this, "This seat is already booked", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (cardView.getCardBackgroundColor().getDefaultColor() == -1) {
                        //Change background color
                        cardView.setCardBackgroundColor(Color.parseColor("#FF9800"));
                        totalCost += seatPrice;
                        selectedSeatNumbers.add(finalI + 1); // Add selected seat number to the list
                        Toast.makeText(SeatActivity.this, "You Selected Seat Number :" + (finalI + 1), Toast.LENGTH_LONG).show();

                    } else {
                        //Change background color
                        cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        totalCost -= seatPrice;
                        selectedSeatNumbers.remove((Object)(finalI + 1)); // Remove unselected seat number from the list
                        Toast.makeText(SeatActivity.this, "You Unselected Seat Number :" + (finalI + 1), Toast.LENGTH_SHORT).show();
                    }
                    totalPrice.setText("" + totalCost + "0");
                    selectedSeats.setText(selectedSeatNumbers.toString().replaceAll("[\\[\\]]", "")); // Update selected seat numbers text view
                }
            });

        }

    }
    // Method to listen for changes in the seat status from the database
    private void listenForSeatStatusChanges() {
        DatabaseReference seatStatusRef = FirebaseDatabase.getInstance().getReference().child("SeatStatus");
        seatStatusRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Loop through each child node
                for (DataSnapshot seatSnapshot : dataSnapshot.getChildren()) {
                    int seatNumber = Integer.parseInt(seatSnapshot.getKey());
                    boolean isBooked = seatSnapshot.getValue(Boolean.class);
                    if (isBooked) {
                        // If the seat is booked, change its color to indicate booking status
                        CardView cardView = (CardView) mainGrid.getChildAt(seatNumber - 1);
                        cardView.setCardBackgroundColor(Color.parseColor("#E11818"));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
                Toast.makeText(SeatActivity.this, "Failed to listen for seat status changes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isSeatBooked(int seatNumber) {
        // Implement logic to check if the seat is already booked
        // You can retrieve the list of booked seats from the database and compare with the current seat number
        // Return true if the seat is already booked, false otherwise
        // For demonstration purposes, let's assume seat 5 is already booked
        return seatNumber == 4;
    }
}
