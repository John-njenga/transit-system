package com.example.nots;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class BusActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    BusAdapter busAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Bus> options =
                new FirebaseRecyclerOptions.Builder<Bus>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Buses"), Bus.class)
                        .build();
        busAdapter = new BusAdapter(options);
        recyclerView.setAdapter(busAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        busAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        busAdapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search,menu);
        MenuItem item= menu.findItem(R.id.search);
        SearchView searchView=(SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                txtsearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                txtsearch(query);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    private void txtsearch(String str){

        FirebaseRecyclerOptions<Bus> options =
                new FirebaseRecyclerOptions.Builder<Bus>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Buses").orderByChild("Destination").startAt(str).endAt(str+"~"), Bus.class)
                        .build();

        busAdapter=new BusAdapter(options);
        busAdapter.startListening();
        recyclerView.setAdapter(busAdapter);
    }
}
