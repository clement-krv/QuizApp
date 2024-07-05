// HighScoresActivity.java
package fr.decouverte.quizzapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class HighScoresActivity extends AppCompatActivity {

    private ListView listViewHighScores;
    private Button buttonBackToMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        listViewHighScores = findViewById(R.id.listViewHighScores);
        buttonBackToMenu = findViewById(R.id.buttonBackToMenu);

        // Afficher les meilleurs scores
        displayHighScores();

        buttonBackToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HighScoresActivity.this, StartActivity.class);
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

        // Afficher les scores dans la ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, highScoresList);
        listViewHighScores.setAdapter(adapter);
    }
}
