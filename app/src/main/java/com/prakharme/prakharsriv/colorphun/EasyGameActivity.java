package com.prakharme.prakharsriv.colorphun;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.prakharme.prakharsriv.colorphun.util.BetterColor;

public class EasyGameActivity extends MainGameActivity {

    private Button topBtn, bottomBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_game);
        setupProgressView();

        POINT_INCREMENT = 2;
        TIMER_BUMP = 2;

        gameMode = GameMode.EASY;

        topBtn = (Button) findViewById(R.id.top_button);
        bottomBtn = (Button) findViewById(R.id.bottom_button);
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
        int color = Color.parseColor(BetterColor.getColor());
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);

        int alpha1, alpha2;
        if (Math.random() > 0.5) {
            alpha1 = 255;
            alpha2 = 185;
        } else {
            alpha1 = 185;
            alpha2 = 255;
        }

        topBtn.setBackgroundColor(Color.argb(alpha1, red, green, blue));
        bottomBtn.setBackgroundColor(Color.argb(alpha2, red, green, blue));
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
        } else { // incorrect guess
            endGame();
        }
    }

}
