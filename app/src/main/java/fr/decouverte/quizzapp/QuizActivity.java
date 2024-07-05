// QuizActivity.java
package fr.decouverte.quizzapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class QuizActivity extends AppCompatActivity {

    private TextView textViewQuestion;
    private RadioGroup radioGroupAnswers;
    private Button buttonSubmit;
    private TextView textViewProgress;

    private String[] questions = {
            "Quelle est la capitale de la France ?",
            "Quelle est la capitale de l'Allemagne ?",
            "Quelle est la capitale du Japon ?",
            "Quelle est la capitale du Canada ?",
            "Quelle est la capitale du Brésil ?"
    };

    private String[][] answers = {
            {"Lyon", "Marseille", "Toulouse", "Paris"},
            {"Munich", "Francfort", "Berlin", "Hambourg"},
            {"Osaka", "Tokyo", "Nagoya", "Kyoto"},
            {"Toronto", "Ottawa", "Vancouver", "Montréal"},
            {"Rio de Janeiro", "Brasilia", "São Paulo", "Salvador"}
    };

    // Indices des réponses correctes
    private int[] correctAnswers = {3, 2, 1, 1, 1};

    private int currentQuestion = 0;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        textViewQuestion = findViewById(R.id.textViewQuestion);
        radioGroupAnswers = findViewById(R.id.radioGroupAnswers);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        textViewProgress = findViewById(R.id.textViewProgress);

        loadQuestion();

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroupAnswers.getCheckedRadioButtonId();
                if (selectedId != -1) {
                    RadioButton selectedRadioButton = findViewById(selectedId);
                    int selectedIndex = radioGroupAnswers.indexOfChild(selectedRadioButton);

                    if (selectedIndex == correctAnswers[currentQuestion]) {
                        score++;
                    }

                    currentQuestion++;
                    if (currentQuestion < questions.length) {
                        loadQuestion();
                    } else {
                        // Fin du quiz, navigation vers EndActivity avec le score
                        Intent intent = new Intent(QuizActivity.this, EndActivity.class);
                        intent.putExtra("score", score);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }

    private void loadQuestion() {
        textViewQuestion.setText(questions[currentQuestion]);
        radioGroupAnswers.clearCheck();  // Réinitialise la sélection du RadioGroup
        for (int i = 0; i < 4; i++) {
            ((RadioButton) radioGroupAnswers.getChildAt(i)).setText(answers[currentQuestion][i]);
        }
        textViewProgress.setText("Question " + (currentQuestion + 1) + "/5");
    }
}
