package com.example.lab1;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import java.util.Objects;

public class ColoredActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colored);

        Button back_btn = findViewById(R.id.back_button);

        Intent intent = getIntent();
        String color = intent.getStringExtra("COLOR");
        Log.d("WORKING", "receive color: " + color);

        ConstraintLayout layout = findViewById(R.id.colored_layout);

        if (Objects.equals(color, "green")) {
            layout.setBackgroundResource(R.color.green);             // set background color
            back_btn.setBackgroundResource(R.drawable.green_button); // set button color

            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.light_green));
            getWindow().setNavigationBarColor(ContextCompat.getColor(this,R.color.green));
        } else if (Objects.equals(color, "blue")) {
            layout.setBackgroundResource(R.color.blue);             // set background color
            back_btn.setBackgroundResource(R.drawable.blue_button); // set button color

            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.light_blue));
            getWindow().setNavigationBarColor(ContextCompat.getColor(this,R.color.blue));
        } else if (Objects.equals(color, "red")) {
            layout.setBackgroundResource(R.color.red);              // set background color
            back_btn.setBackgroundResource(R.drawable.red_button);  // set button color

            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.light_red));
            getWindow().setNavigationBarColor(ContextCompat.getColor(this,R.color.red));
        }

        Log.d("WORKING", "successfully set color: " + color);

        back_btn.setOnClickListener(view -> {
            Intent back_intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(back_intent);
        });
    }
}
