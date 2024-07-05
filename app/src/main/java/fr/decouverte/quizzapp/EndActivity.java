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
import java.util.List;
import java.util.Set;

public class EndActivity extends AppCompatActivity {

    private TextView textViewScore;
    private TextView textViewTime;
    private ListView listViewHighScores;
    private ListView listViewResults;
    private Button buttonRestart;
    private Button buttonMainMenu;
    private int score;
    private long time;
    private ArrayList<Integer> playerAnswers;
    private ArrayList<Integer> questionOrder;
    private String playerName;

    private String[] questions = {
            "Quelle est la capitale de la France ?",
            "Quelle est la capitale de l'Allemagne ?",
            "Quelle est la capitale du Japon ?",
            "Quelle est la capitale du Canada ?",
            "Quelle est la capitale du Brésil ?",
            "Quelle est la capitale de l'Italie ?",
            "Quelle est la capitale de l'Espagne ?",
            "Quelle est la capitale de la Chine ?",
            "Quelle est la capitale de l'Inde ?",
            "Quelle est la capitale de la Russie ?",
            "Quelle est la capitale de l'Australie ?",
            "Quelle est la capitale du Mexique ?",
            "Quelle est la capitale de l'Afrique du Sud ?",
            "Quelle est la capitale de l'Argentine ?",
            "Quelle est la capitale de l'Égypte ?",
            "Quelle est la capitale de la Turquie ?",
            "Quelle est la capitale de la Grèce ?",
            "Quelle est la capitale de la Suède ?",
            "Quelle est la capitale de la Norvège ?",
            "Quelle est la capitale de la Corée du Sud ?"
    };

    private String[][] answers = {
            {"Lyon", "Marseille", "Toulouse", "Paris"},
            {"Munich", "Francfort", "Berlin", "Hambourg"},
            {"Osaka", "Tokyo", "Nagoya", "Kyoto"},
            {"Toronto", "Ottawa", "Vancouver", "Montréal"},
            {"Rio de Janeiro", "Brasilia", "São Paulo", "Salvador"},
            {"Rome", "Milan", "Venise", "Florence"},
            {"Barcelone", "Madrid", "Valence", "Séville"},
            {"Shanghai", "Beijing", "Guangzhou", "Shenzhen"},
            {"Mumbai", "New Delhi", "Bangalore", "Hyderabad"},
            {"Saint-Pétersbourg", "Moscou", "Kazan", "Novossibirsk"},
            {"Sydney", "Melbourne", "Canberra", "Brisbane"},
            {"Cancún", "Guadalajara", "Mexico", "Monterrey"},
            {"Le Cap", "Johannesburg", "Pretoria", "Durban"},
            {"Buenos Aires", "Córdoba", "Rosario", "Mendoza"},
            {"Alexandrie", "Le Caire", "Gizeh", "Louqsor"},
            {"Istanbul", "Ankara", "Izmir", "Bursa"},
            {"Athènes", "Thessalonique", "Patras", "Héraklion"},
            {"Stockholm", "Göteborg", "Malmö", "Uppsala"},
            {"Oslo", "Bergen", "Stavanger", "Trondheim"},
            {"Busan", "Séoul", "Incheon", "Daegu"}
    };

    // Indices des réponses correctes
    private int[] correctAnswers = {3, 2, 1, 1, 1, 0, 1, 1, 1, 1, 2, 2, 2, 0, 1, 1, 0, 0, 0, 1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        textViewScore = findViewById(R.id.textViewScore);
        textViewTime = findViewById(R.id.textViewTime);
        listViewHighScores = findViewById(R.id.listViewHighScores);
        listViewResults = findViewById(R.id.listViewResults);
        buttonRestart = findViewById(R.id.buttonRestart);
        buttonMainMenu = findViewById(R.id.buttonMainMenu);

        // Récupérer le score, le temps et les réponses du joueur
        score = getIntent().getIntExtra("score", 0);
        time = getIntent().getLongExtra("time", 0);
        playerAnswers = getIntent().getIntegerArrayListExtra("playerAnswers");
        questionOrder = getIntent().getIntegerArrayListExtra("questionOrder");
        playerName = getIntent().getStringExtra("playerName");

        textViewScore.setText("Votre score : " + score);
        textViewTime.setText("Temps : " + formatTime(time));

        // Jouer le son de fin
        MediaPlayer mediaPlayer = MediaPlayer.create(EndActivity.this, R.raw.end_sound);
        mediaPlayer.start();

        // Afficher les meilleurs scores
        displayHighScores();

        // Afficher les résultats détaillés
        displayResults();

        buttonRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Recommencer une partie en redirigeant vers DifficultyActivity
                Intent intent = new Intent(EndActivity.this, DifficultyActivity.class);
                intent.putExtra("playerName", playerName);
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
        highScoresList.add(playerName + " - " + score + " - " + formatTime(time));

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

    private void displayResults() {
        List<String> results = new ArrayList<>();
        for (int i = 0; i < playerAnswers.size(); i++) {
            int questionIndex = questionOrder.get(i);
            int playerAnswerIndex = playerAnswers.get(i);
            String question = questions[questionIndex];
            String correctAnswer = answers[questionIndex][correctAnswers[questionIndex]];
            String playerAnswer = playerAnswerIndex == -1 ? "Non répondu" : answers[questionIndex][playerAnswerIndex];
            boolean isCorrect = playerAnswer.equals(correctAnswer);

            String result = question + "\nVotre réponse : " + playerAnswer + "\nBonne réponse : " + correctAnswer;
            if (isCorrect) {
                result += "\nRésultat : Correct";
            } else {
                result += "\nRésultat : Incorrect";
            }
            results.add(result);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, results);
        listViewResults.setAdapter(adapter);
    }
}
