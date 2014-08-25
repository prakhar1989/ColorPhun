package com.example.prakharsriv.colorphun;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class GameOverActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        TextView gameOverText = (TextView) findViewById(R.id.game_over);
        TextView levelIndicator = (TextView) findViewById(R.id.level_indicator);
        TextView pointsBox = (TextView) findViewById(R.id.points_box);
        TextView bestLabel = (TextView) findViewById(R.id.best_label);
        TextView bestBox = (TextView) findViewById(R.id.best_box);
        Button replayBtn = (Button) findViewById(R.id.replay_btn);

        Typeface avenir_black = Typeface.createFromAsset(getAssets(), "fonts/avenir_black.ttf");
        Typeface avenir_book = Typeface.createFromAsset(getAssets(), "fonts/avenir_book.ttf");

        gameOverText.setTypeface(avenir_black);
        levelIndicator.setTypeface(avenir_book);
        pointsBox.setTypeface(avenir_black);
        bestBox.setTypeface(avenir_black);
        bestLabel.setTypeface(avenir_book);
        replayBtn.setTypeface(avenir_book);

        Bundle bundle = getIntent().getExtras();
        int points = bundle.getInt("points");
        int level = bundle.getInt("level");

        Log.i("LOGGER", "Points: " + points);

        pointsBox.setText(String.format("%03d", points));
        levelIndicator.setText("Level " + Integer.toString(level));
    }

    public void playGame(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }
}
