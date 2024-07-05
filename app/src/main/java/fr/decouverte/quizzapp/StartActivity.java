// StartActivity.java
package fr.decouverte.quizzapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class StartActivity extends AppCompatActivity {

    private EditText editTextName;
    private Button buttonStart;
    private Button buttonViewHighScores;
    private Switch switchDarkMode;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Appliquer le thème avant de définir le contenu
        prefs = getSharedPreferences("quizApp", MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean("darkMode", false);
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        editTextName = findViewById(R.id.editTextName);
        buttonStart = findViewById(R.id.buttonStart);
        buttonViewHighScores = findViewById(R.id.buttonViewHighScores);
        switchDarkMode = findViewById(R.id.switchDarkMode);

        // Retrieve saved name and dark mode preference
        String savedName = prefs.getString("playerName", "");
        editTextName.setText(savedName);
        switchDarkMode.setChecked(isDarkMode);

        buttonStart.setOnClickListener(v -> {
            String playerName = editTextName.getText().toString();
            // Save player name
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("playerName", playerName);
            editor.apply();

            // Play start sound
            MediaPlayer mediaPlayer = MediaPlayer.create(StartActivity.this, R.raw.start_sound);
            mediaPlayer.start();

            // Start QuizActivity
            Intent intent = new Intent(StartActivity.this, DifficultyActivity.class);
            intent.putExtra("playerName", playerName);
            startActivity(intent);
        });

        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("darkMode", isChecked);
            editor.apply();

            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        buttonViewHighScores.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, HighScoresActivity.class);
            startActivity(intent);
        });
    }
}
