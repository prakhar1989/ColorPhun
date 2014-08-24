package com.example.prakharsriv.colorphun;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
    }
}
