// DifficultyActivity.java
package fr.decouverte.quizzapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class DifficultyActivity extends AppCompatActivity {

    private Button buttonEasy;
    private Button buttonNormal;
    private Button buttonHard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty);

        buttonEasy = findViewById(R.id.buttonEasy);
        buttonNormal = findViewById(R.id.buttonNormal);
        buttonHard = findViewById(R.id.buttonHard);

        buttonEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuiz(5); // Facile : 5 questions
            }
        });

        buttonNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuiz(10); // Normal : 10 questions
            }
        });

        buttonHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuiz(20); // Difficile : 20 questions
            }
        });
    }

    private void startQuiz(int numberOfQuestions) {
        Intent intent = new Intent(DifficultyActivity.this, QuizActivity.class);
        intent.putExtra("numberOfQuestions", numberOfQuestions);
        startActivity(intent);
        finish();
    }
}
