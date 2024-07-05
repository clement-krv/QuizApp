// QuizActivity.java
package fr.decouverte.quizzapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private TextView textViewQuestion;
    private RadioGroup radioGroupAnswers;
    private Button buttonSubmit;
    private TextView textViewProgress;
    private Chronometer chronometer;

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

    private int currentQuestion = 0;
    private int score = 0;
    private int numberOfQuestions = 5; // Par défaut, facile

    private List<Integer> questionOrder; // Pour stocker l'ordre des questions
    private ArrayList<Integer> playerAnswers; // Pour stocker les réponses du joueur
    private String playerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        textViewQuestion = findViewById(R.id.textViewQuestion);
        radioGroupAnswers = findViewById(R.id.radioGroupAnswers);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        textViewProgress = findViewById(R.id.textViewProgress);
        chronometer = findViewById(R.id.chronometer);

        // Récupérer le nombre de questions depuis l'intent
        numberOfQuestions = getIntent().getIntExtra("numberOfQuestions", 5);
        playerName = getIntent().getStringExtra("playerName");

        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();

        // Mélanger les questions
        generateQuestionOrder();

        // Initialiser la liste des réponses du joueur
        playerAnswers = new ArrayList<>(Collections.nCopies(numberOfQuestions, -1));

        loadQuestion();

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroupAnswers.getCheckedRadioButtonId();
                if (selectedId != -1) {
                    RadioButton selectedRadioButton = findViewById(selectedId);
                    int selectedIndex = radioGroupAnswers.indexOfChild(selectedRadioButton);

                    int actualQuestionIndex = questionOrder.get(currentQuestion);
                    playerAnswers.set(currentQuestion, selectedIndex); // Sauvegarder la réponse du joueur
                    if (selectedIndex == correctAnswers[actualQuestionIndex]) {
                        score++;
                    }

                    currentQuestion++;
                    if (currentQuestion < numberOfQuestions) {
                        loadQuestion();
                    } else {
                        chronometer.stop();
                        long elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();

                        // Fin du quiz, navigation vers EndActivity avec le score, le temps et les réponses du joueur
                        Intent intent = new Intent(QuizActivity.this, EndActivity.class);
                        intent.putExtra("score", score);
                        intent.putExtra("time", elapsedMillis);
                        intent.putExtra("playerName", playerName);
                        intent.putIntegerArrayListExtra("playerAnswers", playerAnswers);
                        intent.putIntegerArrayListExtra("questionOrder", new ArrayList<>(questionOrder));
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }

    private void generateQuestionOrder() {
        questionOrder = new ArrayList<>();
        for (int i = 0; i < questions.length; i++) {
            questionOrder.add(i);
        }
        Collections.shuffle(questionOrder);
    }

    private void loadQuestion() {
        int questionIndex = questionOrder.get(currentQuestion);
        textViewQuestion.setText(questions[questionIndex]);
        radioGroupAnswers.clearCheck();  // Réinitialise la sélection du RadioGroup
        for (int i = 0; i < 4; i++) {
            ((RadioButton) radioGroupAnswers.getChildAt(i)).setText(answers[questionIndex][i]);
        }
        textViewProgress.setText("Question " + (currentQuestion + 1) + "/" + numberOfQuestions);
    }
}
