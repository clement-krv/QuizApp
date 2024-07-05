package fr.decouverte.quizzapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.Locale;

public class StartActivity extends AppCompatActivity {

    private EditText editTextName;
    private Button buttonStart;
    private Button buttonViewHighScores;
    private Button buttonClearPrefs;
    private Switch switchDarkMode;
    private Spinner spinnerLanguage;
    private SharedPreferences prefs;
    private boolean isRestarting = false; // Ajoutez cette variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefs = getSharedPreferences("quizApp", MODE_PRIVATE);

        // Vérifier si c'est le premier lancement
        boolean isFirstLaunch = prefs.getBoolean("isFirstLaunch", true);
        if (isFirstLaunch) {
            // Définir la langue par défaut sur le français
            setLocale("fr");
            // Mettre à jour la préférence pour indiquer que ce n'est plus le premier lancement
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isFirstLaunch", false);
            editor.apply();
        }

        // Appliquer le thème avant de définir le contenu
        boolean isDarkMode = prefs.getBoolean("darkMode", false);
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);

        if (isRestarting) { // Ajoutez cette condition
            isRestarting = false;
            return;
        }

        setContentView(R.layout.activity_start);

        editTextName = findViewById(R.id.editTextName);
        buttonStart = findViewById(R.id.buttonStart);
        buttonViewHighScores = findViewById(R.id.buttonViewHighScores);
        buttonClearPrefs = findViewById(R.id.buttonClearPrefs);
        switchDarkMode = findViewById(R.id.switchDarkMode);
        spinnerLanguage = findViewById(R.id.spinnerLanguage);

        // Retrieve saved name and dark mode preference
        String savedName = prefs.getString("playerName", "");
        editTextName.setText(savedName);
        switchDarkMode.setChecked(isDarkMode);

        // Set up the language spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.languages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguage.setAdapter(adapter);

        // Set the spinner to the current language
        String currentLanguage = prefs.getString("language", "fr");
        if (currentLanguage.equals("en")) {
            spinnerLanguage.setSelection(1);
        } else {
            spinnerLanguage.setSelection(0);
        }

        spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLanguage = (position == 0) ? "fr" : "en";
                if (!selectedLanguage.equals(currentLanguage)) {
                    setLocale(selectedLanguage);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

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

            // Redémarrer l'activité pour appliquer le changement de thème
            isRestarting = true;
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        });

        buttonViewHighScores.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, HighScoresActivity.class);
            startActivity(intent);
        });

        buttonClearPrefs.setOnClickListener(v -> {
            // Effacer toutes les préférences
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();

            // Redémarrer l'activité pour appliquer les changements
            isRestarting = true;
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        });
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        // Save the selected language
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("language", lang);
        editor.apply();

        // Restart activity to apply the language change
        isRestarting = true;
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
