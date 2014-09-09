package com.prakharme.prakharsriv.colorphun;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.os.Handler;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class EasyGameActivity extends MainGameActivity {

    private AnimatorSet pointAnim, levelAnim;
    private Button topBtn, bottomBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_game);

        handler = new Handler();

        topBtn = (Button) findViewById(R.id.top_button);
        bottomBtn = (Button) findViewById(R.id.bottom_button);

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

        topBtn.setOnClickListener(this);
        bottomBtn.setOnClickListener(this);

        // bootstrap game
        resetGame();
        setupGameLoop();
        startGame();
    }

    @Override
    public void onClick(View view) {
        if (!gameStart) return;
        calculatePoints(view);
        setColorsOnButtons();
    }

    protected void setColorsOnButtons() {
        int[] colorPair = getRandomColor();
        topBtn.setBackgroundColor(colorPair[0]);
        bottomBtn.setBackgroundColor(colorPair[1]);
    }

    protected void calculatePoints(View clickedView) {
        View unclickedView = clickedView == topBtn ? bottomBtn : topBtn;
        ColorDrawable clickedColor = (ColorDrawable) clickedView.getBackground();
        ColorDrawable unClickedColor = (ColorDrawable) unclickedView.getBackground();

        int alpha1 = Color.alpha(clickedColor.getColor());
        int alpha2 = Color.alpha(unClickedColor.getColor());

        // correct guess
        if (alpha1 < alpha2) {
            updatePoints();
            pointsTextView.setText(Integer.toString(points));
            pointAnim.start();

            // increment level
            if (points > level * LEVEL) {
                incrementLevel();
                levelTextView.setText(Integer.toString(level));
                levelAnim.start();
            }
        } else {
            // incorrect guess
            endGame();
        }
    }

}
