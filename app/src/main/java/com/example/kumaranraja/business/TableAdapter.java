package com.example.kumaranraja.business;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHolder> {

    private List<TableRowData> dataList;

    public TableAdapter(List<TableRowData> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_table_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TableRowData rowData = dataList.get(position);

        // Bind data to views
        holder.textViewColumn1.setText(rowData.getColumn1Data());
        holder.textViewColumn2.setText(rowData.getColumn2Data());
        holder.textViewColumn3.setText(rowData.getColumn3Data());
        holder.textViewColumn4.setText(rowData.getColumn4Data());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewColumn1;
        TextView textViewColumn2;
        TextView textViewColumn3;
        TextView textViewColumn4;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewColumn1 = itemView.findViewById(R.id.textViewColumn1);
            textViewColumn2 = itemView.findViewById(R.id.textViewColumn2);
            textViewColumn3 = itemView.findViewById(R.id.textViewColumn3);
            textViewColumn4 = itemView.findViewById(R.id.textViewColumn4);
        }
    }
}