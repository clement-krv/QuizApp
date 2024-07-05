// EndActivity.java
package fr.decouverte.quizzapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class EndActivity extends AppCompatActivity {

    private TextView textViewScore;
    private TextView textViewTime;
    private ListView listViewHighScores;
    private Button buttonRestart;
    private Button buttonMainMenu;
    private int score;
    private long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        textViewScore = findViewById(R.id.textViewScore);
        textViewTime = findViewById(R.id.textViewTime);
        listViewHighScores = findViewById(R.id.listViewHighScores);
        buttonRestart = findViewById(R.id.buttonRestart);
        buttonMainMenu = findViewById(R.id.buttonMainMenu);

        // Récupérer le score et le temps du joueur
        score = getIntent().getIntExtra("score", 0);
        time = getIntent().getLongExtra("time", 0);
        textViewScore.setText("Votre score : " + score);
        textViewTime.setText("Temps : " + formatTime(time));

        // Jouer le son de fin
        MediaPlayer mediaPlayer = MediaPlayer.create(EndActivity.this, R.raw.end_sound);
        mediaPlayer.start();

        // Afficher les meilleurs scores
        displayHighScores();

        buttonRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Recommencer une partie en redirigeant vers DifficultyActivity
                Intent intent = new Intent(EndActivity.this, DifficultyActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EndActivity.this, StartActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private String formatTime(long time) {
        int seconds = (int) (time / 1000) % 60;
        int minutes = (int) (time / (1000 * 60)) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void displayHighScores() {
        SharedPreferences prefs = getSharedPreferences("quizApp", MODE_PRIVATE);
        Set<String> highScoresSet = prefs.getStringSet("highScores", new HashSet<String>());
        ArrayList<String> highScoresList = new ArrayList<>(highScoresSet);
        Collections.sort(highScoresList, Collections.reverseOrder());

        // Ajouter le score actuel à la liste
        highScoresList.add(score + " - " + formatTime(time));

        // Garder seulement les 10 meilleurs scores
        if (highScoresList.size() > 10) {
            highScoresList = new ArrayList<>(highScoresList.subList(0, 10));
        }

        // Sauvegarder les scores mis à jour
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet("highScores", new HashSet<>(highScoresList));
        editor.apply();

        // Afficher les scores dans la ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, highScoresList);
        listViewHighScores.setAdapter(adapter);
    }
}
