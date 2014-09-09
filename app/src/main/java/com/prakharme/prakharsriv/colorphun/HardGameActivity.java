package com.prakharme.prakharsriv.colorphun;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


public class HardGameActivity extends MainGameActivity {

    private AnimatorSet pointAnim, levelAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hard_game);

        timerProgress = (ProgressBar) findViewById(R.id.progress_bar);
        pointsTextView = (TextView) findViewById(R.id.points_value);
        levelTextView = (TextView) findViewById(R.id.level_value);
        TextView pointsLabel = (TextView) findViewById(R.id.points_label);
        TextView levelsLabel = (TextView) findViewById(R.id.level_label);

        // setting up fonts
        Typeface avenir_black = Typeface.createFromAsset(getAssets(), "fonts/avenir_black.ttf");
        Typeface avenir_book = Typeface.createFromAsset(getAssets(), "fonts/avenir_book.ttf");
        pointsTextView.setTypeface(avenir_black);
        levelTextView.setTypeface(avenir_black);
        pointsLabel.setTypeface(avenir_book);
        levelsLabel.setTypeface(avenir_book);

        // setting up animations
        pointAnim = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.points_animations);
        pointAnim.setTarget(pointsTextView);
        levelAnim = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.level_animations);
        levelAnim.setTarget(levelTextView);
    }

    @Override
    protected void setColorsOnButtons() {

    }

    @Override
    protected void calculatePoints(View view) {

    }

    @Override
    public void onClick(View view) {
        if (!gameStart) return;
        calculatePoints(view);
        setColorsOnButtons();
    }
}
