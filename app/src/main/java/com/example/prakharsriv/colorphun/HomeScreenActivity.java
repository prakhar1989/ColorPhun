package com.example.prakharsriv.colorphun;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomeScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
    }

    public void playGame(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void viewScoreBoard(View view) {
        startActivity(new Intent(this, ScoreboardActivity.class));
    }
}
