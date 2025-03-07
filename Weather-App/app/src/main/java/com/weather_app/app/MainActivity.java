package com.weather_app.app;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class MainActivity extends AppCompatActivity {

    private EditText cityNameEditText;
    private MaterialButton searchButton;
    private TextView cityNameTextView;
    private TextView temperatureTextView;
    private TextView weatherConditionTextView;
    private TextView humidityTextView;
    private TextView windTextView;
    private ImageView weatherIcon;
    private MaterialCardView weatherInfoCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize views
        cityNameEditText = findViewById(R.id.cityName);
        searchButton = findViewById(R.id.search);
        cityNameTextView = findViewById(R.id.cityNameTextView);
        temperatureTextView = findViewById(R.id.temperatureTextView);
        weatherConditionTextView = findViewById(R.id.weatherConditionTextView);
        humidityTextView = findViewById(R.id.humidityTextView);
        windTextView = findViewById(R.id.windTextView);
        weatherIcon = findViewById(R.id.weatherIcon);
        weatherInfoCard = findViewById(R.id.weatherInfoCard);

        // Initially hide the weather info card until search is performed
        weatherInfoCard.setVisibility(View.GONE);

        // Setup edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            // Don't apply padding to the main container
            // This allows the background to extend behind the system bars
            return insets;
        });

        // Adjust the AppBar for proper insets
        ViewCompat.setOnApplyWindowInsetsListener(toolbar, (v, insets) -> {
            v.setPadding(
                    v.getPaddingLeft(),
                    insets.getInsets(WindowInsetsCompat.Type.statusBars()).top,
                    v.getPaddingRight(),
                    v.getPaddingBottom()
            );
            return insets;
        });

        // Set up search button click listener
        searchButton.setOnClickListener(v -> {
            String cityName = cityNameEditText.getText().toString().trim();
            if (!cityName.isEmpty()) {
                fetchWeatherData(cityName);
            } else {
                Toast.makeText(MainActivity.this, "Please enter a city name", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Fetch weather data for the specified city
     * You'll need to implement this with your weather API
     */
    private void fetchWeatherData(String cityName) {
        // This is where you would make an API call to get weather data
        // For now, we'll just simulate a successful response

        // Show the weather info card
        weatherInfoCard.setVisibility(View.VISIBLE);

        // For demonstration, update with placeholder data
        cityNameTextView.setText(cityName);
        temperatureTextView.setText("24°C");
        weatherConditionTextView.setText("Partly Cloudy");
        humidityTextView.setText("65%");
        windTextView.setText("5 km/h");

        // Example of setting weather icon (you would use the appropriate icon based on conditions)
        // weatherIcon.setImageResource(R.drawable.ic_partly_cloudy);

        // Add animation if desired
        weatherInfoCard.setAlpha(0f);
        weatherInfoCard.animate().alpha(1f).setDuration(500).start();
    }
}