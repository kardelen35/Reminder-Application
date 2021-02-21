package com.example.reminderapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Integer> idArray;
    private ArrayList<String> titleArray;
    CardView cardView;
    Cursor cursor;
    private Listener listener;



    interface  Listener{
        void onClick(int position, Activity activity);
    }

    public void setListener(Listener listener){
        this.listener=listener;
    }

    public CustomAdapter(Context context, ArrayList idArray, ArrayList titleArray) {
        this.context = context;
        this.idArray = idArray;
        this.titleArray = titleArray;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        cardView=(CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new MyViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.text_id.setText(String.valueOf(idArray.get(position)));
        holder.text_title.setText(String.valueOf(titleArray.get(position)));

       holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("id", String.valueOf(idArray.get(position)));
                }
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                int id=idArray.get(position);
                intent.putExtra("id",id);;

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {

        return idArray.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView text_id, text_title;
        private CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.cardView=(CardView)itemView;
            text_id = itemView.findViewById(R.id.lblId);
            text_title = itemView.findViewById(R.id.lblTitle);

        }
    }
}
