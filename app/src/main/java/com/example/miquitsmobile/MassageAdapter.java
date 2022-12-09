package com.example.miquitsmobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.util.List;

public class MassageAdapter extends RecyclerView.Adapter<MassageAdapter.MyViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    private Context mContext;
    private List<MassageModelClass> massageData;

    public MassageAdapter(Context mContext, List<MassageModelClass> orderData, RecyclerViewInterface recyclerViewInterface) {
        this.mContext = mContext;
        this.massageData = orderData;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.massage_card, parent, false);

        return new MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String seatType = "Seated";
        if (massageData.get(position).getSeatType().equals("lying")) {
            seatType = "Lying";
        }
        holder.massageName.setText(massageData.get(position).getTitle());
        holder.massageDescription.setText(massageData.get(position).getDescription());
        holder.massagePrice.setText("₱" + massageData.get(position).getPrice());
        holder.massageDuration.setText(massageData.get(position).getDuration() + " minutes");
        holder.massageSeatType.setText(seatType);
    }

    @Override
    public int getItemCount() {
        return massageData.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView massageName, massageDescription, massagePrice, massageDuration, massageSeatType;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            massageName = itemView.findViewById(R.id.massage_name);
            massageDescription = itemView.findViewById(R.id.massage_description);
            massagePrice = itemView.findViewById(R.id.massage_price);
            massageDuration = itemView.findViewById(R.id.massage_duration);
            massageSeatType = itemView.findViewById(R.id.seat_type);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null) {
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION) {
                            try {
                                recyclerViewInterface.onItemClick(pos);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
        }
    }
}
