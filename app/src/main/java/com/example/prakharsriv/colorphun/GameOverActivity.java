package com.example.prakharsriv.colorphun;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

public class GameOverActivity extends Activity {

    private int points, best;
    private TextView gameOverText, pointsBox, bestBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        gameOverText = (TextView) findViewById(R.id.game_over);
        TextView levelIndicator = (TextView) findViewById(R.id.level_indicator);
        pointsBox = (TextView) findViewById(R.id.points_box);
        TextView bestLabel = (TextView) findViewById(R.id.best_label);
        bestBox = (TextView) findViewById(R.id.best_box);
        TextView highScoreText = (TextView) findViewById(R.id.highscore_txt);
        Button replayBtn = (Button) findViewById(R.id.replay_btn);

        // setting up typeface
        Typeface avenir_black = Typeface.createFromAsset(getAssets(), "fonts/avenir_black.ttf");
        Typeface avenir_book = Typeface.createFromAsset(getAssets(), "fonts/avenir_book.ttf");

        gameOverText.setTypeface(avenir_black);
        levelIndicator.setTypeface(avenir_book);
        pointsBox.setTypeface(avenir_black);
        bestBox.setTypeface(avenir_black);
        bestLabel.setTypeface(avenir_book);
        replayBtn.setTypeface(avenir_book);
        highScoreText.setTypeface(avenir_black);

        // fetching and setting data
        Bundle bundle = getIntent().getExtras();
        points = bundle.getInt("points");
        int level = bundle.getInt("level");
        best = bundle.getInt("best");
        pointsBox.setText(String.format("%03d", points));
        bestBox.setText(String.format("%03d", best));
        levelIndicator.setText("Level " + Integer.toString(level));

        // show high score if the highscore matches
        if (best == points) {
            highScoreText.setVisibility(View.VISIBLE);
        } else {
            highScoreText.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            ValueAnimator pointsAnim = getCounterAnimator(pointsBox, points);
            pointsAnim.setDuration(1200);

            ObjectAnimator anim = getHeightAnimator();
            anim.setTarget(gameOverText);
            anim.setInterpolator(new BounceInterpolator());
            anim.setDuration(600);

            anim.start();
            pointsAnim.start();
        }
    }

    ValueAnimator getCounterAnimator(final TextView view, final int maxValue) {
        ValueAnimator anim = ValueAnimator.ofInt(0, 1);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (int) (maxValue * valueAnimator.getAnimatedFraction());
                view.setText(String.format("%03d", val));
            }
        });
        return anim;
    }

    ObjectAnimator getHeightAnimator() {
        return ObjectAnimator.ofFloat(this, "Y", 0, 130);
    }

    public void playGame(View view) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
