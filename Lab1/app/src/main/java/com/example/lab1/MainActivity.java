package com.example.lab1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.Objects;

public class MainActivity extends Activity {

    String[] colors = new String[] { "green", "blue", "red" };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("WORKING", "starting main activity");

        Button ok_btn = findViewById(R.id.to_colored);
        EditText editText = findViewById(R.id.input_field);
        TextView textView = findViewById(R.id.info_text);
        textView.setText(getResources().getString(R.string.choose_color, "green", "blue", "red"));

        ok_btn.setOnClickListener(view -> {
            String color = String.valueOf(editText.getText()).toLowerCase().trim();

            Log.d("WORKING", "get color: " + color);

            if (Objects.equals("", color)) {
                Log.d("WORKING", "get empty field");
                Toast.makeText(getApplicationContext(), R.string.empty_field, Toast.LENGTH_SHORT).show();
            } else if (Arrays.stream(colors).noneMatch(i -> Objects.equals(i, color))) {
                Log.d("WORKING", "wrong color: " + color);
                Toast.makeText(getApplicationContext(), R.string.unsupported_color, Toast.LENGTH_SHORT).show();
            } else {
                Log.d("WORKING", "starting colored activity");
                Intent intent = new Intent(getApplicationContext(), ColoredActivity.class);
                intent.putExtra("COLOR", color);
                startActivity(intent);
            }

        });

    }
}
