package com.example.renner;

import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class sacola extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sacola);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button button = findViewById(R.id.button12);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(sacola.this, MainActivity.class);
            startActivity(intent);
        });
        Button button2 = findViewById(R.id.button13);
        button2.setOnClickListener(v -> {
            Intent intent = new Intent(sacola.this, MainActivity.class);
            startActivity(intent);
        });
        Button button3 = findViewById(R.id.radioButton);
        button3.setOnClickListener(v -> {
            Intent intent = new Intent(sacola.this, MainActivity4.class);
            startActivity(intent);
        });



    }
}