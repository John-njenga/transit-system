package com.example.nots;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    private DatabaseReference reference;
    private EditText editTextDestination, editTextDeparture;
    private Button angry_btn;
    private List<Bus> busList;
    private BusAdapter busAdapter;
    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home, location, detail, cancel, customer, about, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("trips");
        FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();


        drawerLayout = findViewById(R.id.drawerlayout);
        menu = findViewById(R.id.menu);
        home = findViewById(R.id.bill);
        location = findViewById(R.id.location);
        detail = findViewById(R.id.details);
        cancel = findViewById(R.id.cancel);
        customer = findViewById(R.id.customer);
        about = findViewById(R.id.about);
        logout = findViewById(R.id.logout);

        editTextDestination = findViewById(R.id.txt1);
        editTextDeparture = findViewById(R.id.txt2);

        busList = new ArrayList<>();


        progressDialog = new ProgressDialog(this);
        angry_btn = findViewById(R.id.angry_btn);

        editTextDeparture = findViewById(R.id.txt2); // Assuming textView1 is the ID of your TextView
        String[] items1 = new String[]{"FROM", "Kitengela", "Juja", "Nairobi", "Kahawa west", "Kikuyu", "Thika"};
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setItems(items1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedItem = items1[which];
                editTextDeparture.setText(selectedItem); // Set selected item to TextView
            }
        });
        editTextDeparture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder1.show(); // Show dropdown list when TextView is clicked
            }
        });
        editTextDestination = findViewById(R.id.txt1); // Assuming textView1 is the ID of your TextView
        String[] items2 = new String[]{"To", "Kitengela", "Juja", "Nairobi", "Kahawa west", "Kikuyu", "Thika"};
        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        builder2.setItems(items2, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedItem = items2[which];
                editTextDestination.setText(selectedItem); // Set selected item to TextView
            }
        });
        editTextDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder2.show(); // Show dropdown list when TextView is clicked
            }
        });


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
                recreate();
            }
        });
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(Home.this, EventActivity.class);

            }
        });
        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(Home.this, ticketdetails.class);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(Home.this, CancelActivity.class);

            }
        });
        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(Home.this, ContactActivity.class);

            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(Home.this, aboutus.class);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                finish();
                redirectActivity(Home.this, Notslogin.class);
            }
        });
        angry_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTrip();

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

    public static void redirectActivity(Activity activity, Class secondActivity) {
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }


    private void saveTrip() {
        String Destination = editTextDestination.getText().toString().trim();
        String departure = editTextDeparture.getText().toString().trim();

        if (!Destination.isEmpty() && !departure.isEmpty()) {
            String tripId = databaseReference.push().getKey();
            Model trip = new Model(tripId, Destination, departure);
            databaseReference.child(tripId).setValue(trip);
            Toast.makeText(this, "Trip saved successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Home.this, BusActivity.class);
            intent.putExtra("Departure", departure);
            intent.putExtra("Destination", Destination);
            startActivity(intent);

        } else {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
    }
}
