package com.example.kumaranraja.business;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DownlineAdapter extends RecyclerView.Adapter<DownlineAdapter.ViewHolder> {

    private List<downlineList> downlines;

    public DownlineAdapter(List<downlineList> downlines) {
        this.downlines = downlines;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        downlineList downline = downlines.get(position);
        holder.bind(downline);
    }

    @Override
    public int getItemCount() {
        return downlines.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }

        public void bind(downlineList downline) {
            textView.setText(downline.getProfileId());
        }
    }
}