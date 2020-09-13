package com.example.coviduniversity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class List_adapter extends RecyclerView.Adapter<List_adapter.ListViewHolder> {
    private ArrayList<Brody_table_list> BList;
    private OnItemClickListener Listener;

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.brody_table_list_row, parent, false);
        ListViewHolder vh = new ListViewHolder(v, Listener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        Brody_table_list currentItem = BList.get(holder.getAdapterPosition());
        Log.d("list", Integer.toString(position));
        holder.table.setText(currentItem.getName() + " " + currentItem.getStatus());
    }

    @Override
    public int getItemCount() {
        Log.d("list", Integer.toString(BList.size()));
        return BList.size();

    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void SetOnItemClickListener(OnItemClickListener listener) {
        Listener = listener;
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder{
        public TextView table;
        public ListViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            table = itemView.findViewById(R.id.single_table);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public List_adapter(ArrayList<Brody_table_list> BList) {
        this.BList = BList;
    }
}
