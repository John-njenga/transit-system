package com.example.nots;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class BusAdapter extends FirebaseRecyclerAdapter<Bus,BusAdapter.myViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public BusAdapter(@NonNull FirebaseRecyclerOptions<Bus> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, final int position, @NonNull Bus model) {
        holder.CarRegNo.setText(model.getCarRegNo());
        holder.CarRoute.setText(model.getCarRoute());
        holder.Destination.setText(model.getDestination());
        holder.Departure.setText(model.getDeparture());
        holder.Price.setText(model.getPrice());

        holder.btnbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the activity here
                Context context = holder.itemView.getContext();
                Intent intent = new Intent(context, SeatActivity.class); // Replace YourActivity with the actual name of your activity

                // Get the data from the current item
                String CarRegNo = getItem(position).getCarRegNo();
                String CarRoute = getItem(position).getCarRoute();
                String Destination = getItem(position).getDestination();
                String Price = getItem(position).getPrice();

                // Put the data as extras in the intent
                intent.putExtra("CarRegNo", CarRegNo);
                intent.putExtra("CarRoute", CarRoute);
                intent.putExtra("Destination", Destination);
                intent.putExtra("Price", Price);
                // Start the activity
                context.startActivity(intent);
            }
        });

    }



    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bus,parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        TextView CarRegNo,CarRoute,Destination,Departure,Price;
        Button btnbook;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            CarRegNo=(TextView) itemView.findViewById(R.id.CarRegtxt);
            CarRoute=(TextView) itemView.findViewById(R.id.CarRoutetxt);
            Destination=(TextView) itemView.findViewById(R.id.Destinationtxt);
            Departure=(TextView) itemView.findViewById(R.id.Departuretxt);
            Price=(TextView) itemView.findViewById(R.id.Pricetxt);

            btnbook=(Button) itemView.findViewById(R.id.btnbook);

        }
    }
}
