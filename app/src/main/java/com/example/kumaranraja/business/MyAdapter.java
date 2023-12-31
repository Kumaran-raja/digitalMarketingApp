package com.example.kumaranraja.business;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<Payout> dataList;
    private Context context;

    public MyAdapter(Context context, List<Payout> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payout_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Payout data = dataList.get(position);
        holder.bindData(data);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView snoTextView;
        private TextView taskDateTextView;
        private TextView amountFromTextView;
        private TextView amountTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            snoTextView = itemView.findViewById(R.id.sno);
            taskDateTextView = itemView.findViewById(R.id.date);
            amountFromTextView = itemView.findViewById(R.id.description);
            amountTextView = itemView.findViewById(R.id.amount);
        }

        public void bindData(Payout data) {
            snoTextView.setText(String.valueOf(data.getSno()));
            taskDateTextView.setText(data.getTaskDate());
            amountFromTextView.setText(data.getAmountFrom());
            amountTextView.setText(String.valueOf(data.getAmount()));
        }
    }
}
