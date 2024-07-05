package fr.decouverte.quizzapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class EasterEggActivity extends AppCompatActivity {

    private TextView textViewEasterEggMessage;
    private Button buttonMainMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easter_egg);

        textViewEasterEggMessage = findViewById(R.id.textViewEasterEggMessage);
        buttonMainMenu = findViewById(R.id.buttonMainMenu);

        buttonMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EasterEggActivity.this, StartActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
