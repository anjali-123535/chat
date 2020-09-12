package com.example.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat.R;

public class AdminMessageAdapter extends RecyclerView.Adapter<AdminMessageAdapter.ViewHolder> {
    Context context;
    public AdminMessageAdapter(Context context) {
    this.context=context;
    }

    @NonNull
    @Override
    public AdminMessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminMessageAdapter.ViewHolder holder, int position) {
//holder.name.setText();
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,number;
        View view;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            name=itemView.findViewById(R.id.txt_name);
            number=itemView.findViewById(R.id.txt_number);
            view=itemView.findViewById(R.id.card_item);
        }


    }
}
