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

public class UpcomingMassageAdapter extends RecyclerView.Adapter<UpcomingMassageAdapter.MyViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    private Context mContext;
    private List<MassageModelClass> massageData;

    public UpcomingMassageAdapter(Context mContext, List<MassageModelClass> orderData, RecyclerViewInterface recyclerViewInterface) {
        this.mContext = mContext;
        this.massageData = orderData;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.massage_list_item, parent, false);

        return new MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String status = "Accepted";
        if (massageData.get(position).getStatus().equals("pending")) {
            status = "Pending";
        }
        if (massageData.get(position).getStatus().equals("cancelled")) {
            status = "Cancelled";
        }

        holder.massageName.setText(massageData.get(position).getTitle());
        holder.massageDate.setText(massageData.get(position).getDate());
        holder.massageTime.setText(massageData.get(position).getTime());
        holder.massageStatus.setText(status);
    }

    @Override
    public int getItemCount() {
        return massageData.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView massageName, massageDate, massageTime, massageStatus;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            massageName = itemView.findViewById(R.id.massage_name);
            massageDate = itemView.findViewById(R.id.massage_date);
            massageTime = itemView.findViewById(R.id.massage_time);
            massageStatus = itemView.findViewById(R.id.massage_status);

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
