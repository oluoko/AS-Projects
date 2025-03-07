package com.weather_app.app;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // This is the key change: we don't apply the insets padding to the main container
        // which allows the ImageView to extend fully
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            // Don't apply padding to the main container
            // This allows the background to extend behind the system bars
            return insets;
        });

        // For the TextView, we might want to ensure it doesn't overlap with the status bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.textView), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            ((ViewGroup.MarginLayoutParams) v.getLayoutParams()).topMargin =
                    systemBars.top + (int) getResources().getDimension(R.dimen.text_margin_top);
            v.requestLayout();
            return insets;
        });
    }
}