package com.example.kumaranraja.business;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Today_task extends AppCompatActivity {

    Button Task1,Task2,Task3,Task4,Task5,Task6,Task7,Task8,Task9,Task10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_task);
        Task1=findViewById(R.id.task1);
        Task2=findViewById(R.id.task2);
        Task3=findViewById(R.id.task3);
        Task4=findViewById(R.id.task4);
        Task5=findViewById(R.id.task5);
        Task6=findViewById(R.id.task6);
        Task7=findViewById(R.id.task7);
        Task8=findViewById(R.id.task8);
        Task9=findViewById(R.id.task9);
        Task10=findViewById(R.id.task10);

        ImageView backButton = findViewById(R.id.backactivity);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Today_task.this,allwork.class);
                startActivity(i);
            }
        });

        //task activity

        Task1.setOnClickListener(view -> {
            Intent i=new Intent(Today_task.this,Task1.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        });
        Task2.setOnClickListener(view -> {
            Intent i=new Intent(Today_task.this,Task2.class);
            startActivity(i);
        });
        Task3.setOnClickListener(view -> {
            Intent i=new Intent(Today_task.this,Task3.class);
            startActivity(i);
        });
        Task4.setOnClickListener(view -> {
            Intent i=new Intent(Today_task.this,Task4.class);

            startActivity(i);
        });
        Task5.setOnClickListener(view -> {
            Intent i=new Intent(Today_task.this,Task5.class);
            startActivity(i);
        });
        Task6.setOnClickListener(view -> {
            Intent i=new Intent(Today_task.this,Task6.class);
            startActivity(i);
        });
        Task7.setOnClickListener(view -> {
            Intent i=new Intent(Today_task.this,Task7.class);
            startActivity(i);
        });
        Task8.setOnClickListener(view -> {
            Intent i=new Intent(Today_task.this,Task8.class);
            startActivity(i);
        });
        Task9.setOnClickListener(view -> {
            Intent i=new Intent(Today_task.this,Task9.class);
            startActivity(i);
        });
        Task10.setOnClickListener(view -> {
            Intent i=new Intent(Today_task.this,Task10.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        });
    }

}