// StartActivity.java
package fr.decouverte.quizzapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    private EditText editTextName;
    private Button buttonStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        editTextName = findViewById(R.id.editTextName);
        buttonStart = findViewById(R.id.buttonStart);

        // Retrieve saved name
        SharedPreferences prefs = getSharedPreferences("quizApp", MODE_PRIVATE);
        String savedName = prefs.getString("playerName", "");
        editTextName.setText(savedName);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String playerName = editTextName.getText().toString();
                // Save player name
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("playerName", playerName);
                editor.apply();

                // Start QuizActivity
                Intent intent = new Intent(StartActivity.this, QuizActivity.class);
                startActivity(intent);
            }
        });
    }
}
