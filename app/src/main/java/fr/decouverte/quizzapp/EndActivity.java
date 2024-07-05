// EndActivity.java
package fr.decouverte.quizzapp;

import android.content.Intent;
import android.content.SharedPreferences;
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
    private ListView listViewHighScores;
    private Button buttonRestart;
    private Button buttonMainMenu;
    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        textViewScore = findViewById(R.id.textViewScore);
        listViewHighScores = findViewById(R.id.listViewHighScores);
        buttonRestart = findViewById(R.id.buttonRestart);
        buttonMainMenu = findViewById(R.id.buttonMainMenu);

        // Récupérer le score du joueur
        score = getIntent().getIntExtra("score", 0);
        textViewScore.setText("Votre score : " + score);

        // Afficher les meilleurs scores
        displayHighScores();

        buttonRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EndActivity.this, QuizActivity.class);
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

    private void displayHighScores() {
        SharedPreferences prefs = getSharedPreferences("quizApp", MODE_PRIVATE);
        Set<String> highScoresSet = prefs.getStringSet("highScores", new HashSet<String>());
        ArrayList<String> highScoresList = new ArrayList<>(highScoresSet);
        Collections.sort(highScoresList, Collections.reverseOrder());

        // Ajouter le score actuel à la liste
        highScoresList.add(String.valueOf(score));

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
