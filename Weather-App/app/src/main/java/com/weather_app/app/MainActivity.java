package com.weather_app.app;

import android.os.Bundle;
import android.os.*;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

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
        getSupportActionBar().setDisplayShowTitleEnabled(false); // Hide the default title

// Create a TextView for custom centered title
        TextView titleTextView = new TextView(this);
        titleTextView.setText("Weather App");
        titleTextView.setTextAppearance(this, R.style.Toolbar_TitleText);
        titleTextView.setTextColor(getResources().getColor(android.R.color.white));

// Center the title in the toolbar
        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT,
                Toolbar.LayoutParams.WRAP_CONTENT
        );
        layoutParams.gravity = android.view.Gravity.CENTER;
        titleTextView.setLayoutParams(layoutParams);

// Add the TextView to the Toolbar
        toolbar.addView(titleTextView);

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
                    insets.getInsets(WindowInsetsCompat.Type.statusBars ()).top,
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
        // Show a loading toast
        Toast.makeText(MainActivity.this, "Fetching weather data...", Toast.LENGTH_SHORT).show();

        // Create and execute the API request in a background thread
        new Thread(() -> {
            try {
                // Build the API URL
                String apiKey = "d3e1a6d4468a8819de7625a952e08b1c";
                String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" +
                        cityName + "&appid=" + apiKey + "&units=metric";
                URL url = new URL(urlString);

                // Open connection
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // Read the response
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                // Parse JSON response
                JSONObject jsonObject = new JSONObject(result.toString());

                // Extract weather data
                JSONObject main = jsonObject.getJSONObject("main");
                JSONObject wind = jsonObject.getJSONObject("wind");
                JSONObject weather = jsonObject.getJSONArray("weather").getJSONObject(0);

                // Format the data
                String temperature = Math.round(main.getDouble("temp")) + "°C";
                String humidity = main.getInt("humidity") + "%";
                String windSpeed = wind.getDouble("speed") + " m/s";
                String weatherDescription = weather.getString("description");
                String weatherIconCode = weather.getString("icon");

                // Update UI on the main thread
                runOnUiThread(() -> {
                    weatherInfoCard.setVisibility(View.VISIBLE);

                    // Update text views with weather data
                    cityNameTextView.setText(cityName);
                    temperatureTextView.setText(temperature);
                    weatherConditionTextView.setText(weatherDescription);
                    humidityTextView.setText(humidity);
                    windTextView.setText(windSpeed);

                    // Set weather icon based on icon code
                    setWeatherIcon(weatherIconCode);

                    // Animate the card appearance
                    weatherInfoCard.setAlpha(0f);
                    weatherInfoCard.animate().alpha(1f).setDuration(500).start();
                });

            } catch (Exception e) {
                e.printStackTrace();
                // Show error on main thread
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this,
                            "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        }).start();
    }

    // Helper method to set weather icon based on icon code from API
    private void setWeatherIcon(String iconCode) {
        // Using a placeholder for now
        weatherIcon.setImageResource(R.drawable.placeholder_icon);

        // You can add a comment for users to see what weather condition this is
        String description = "Weather icon: " + iconCode;
//        weatherIcon.setContentDescription(description);
//        // Map OpenWeatherMap icon codes to drawable resources
//        // You'll need to add these drawables to your project
//        int iconResource;
//
//        switch (iconCode) {
//            case "01d": // clear sky day
//                iconResource = R.drawable.clear_day;
//                break;
//            case "01n": // clear sky night
//                iconResource = R.drawable.clear_night;
//                break;
//            case "02d": // few clouds day
//            case "03d": // scattered clouds day
//            case "04d": // broken clouds day
//                iconResource = R.drawable.cloudy_day;
//                break;
//            case "02n": // few clouds night
//            case "03n": // scattered clouds night
//            case "04n": // broken clouds night
//                iconResource = R.drawable.cloudy_night;
//                break;
//            case "09d": // shower rain day
//            case "09n": // shower rain night
//            case "10d": // rain day
//            case "10n": // rain night
//                iconResource = R.drawable.rain;
//                break;
//            case "11d": // thunderstorm day
//            case "11n": // thunderstorm night
//                iconResource = R.drawable.thunderstorm;
//                break;
//            case "13d": // snow day
//            case "13n": // snow night
//                iconResource = R.drawable.snow;
//                break;
//            case "50d": // mist day
//            case "50n": // mist night
//                iconResource = R.drawable.mist;
//                break;
//            default:
//                iconResource = R.drawable.cloudy_day;
//                break;
//        }
//
//        weatherIcon.setImageResource(iconResource);
    }
}