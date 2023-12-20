package com.example.kumaranraja.business;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class AmountAddedHistory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amount_added_history);


        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        List<TableRowData> dataList = new ArrayList<>();

// Add instances of your row data to the list (add as many as needed)
        for (int i = 1; i <= 10; i++) {
            dataList.add(new TableRowData("S.No" + i, "Amount" + i, "Description" + i, "Date" + i));
        }

        TableAdapter adapter = new TableAdapter(dataList);
        recyclerView.setAdapter(adapter);
    }
}