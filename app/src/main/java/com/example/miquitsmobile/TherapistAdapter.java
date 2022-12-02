package com.example.miquitsmobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TherapistAdapter extends RecyclerView.Adapter<TherapistAdapter.MyViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    private Context mContext;
    private List<TherapistModelClass> therapistData;

    public TherapistAdapter(Context mContext, List<TherapistModelClass> therapistData, RecyclerViewInterface recyclerViewInterface) {
        this.mContext = mContext;
        this.therapistData = therapistData;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.therapist_card, parent, false);

        return new MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.therapistName.setText(therapistData.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return therapistData.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView therapistName;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            therapistName = itemView.findViewById(R.id.therapist_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null) {
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
