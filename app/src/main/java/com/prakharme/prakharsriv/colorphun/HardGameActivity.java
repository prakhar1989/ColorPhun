package com.prakharme.prakharsriv.colorphun;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.prakharme.prakharsriv.colorphun.util.BetterColor;
import java.util.ArrayList;
import java.util.Random;


public class HardGameActivity extends MainGameActivity {

    private ArrayList<Button> buttonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hard_game);
        setupProgressView();

        POINT_INCREMENT = 4;
        TIMER_BUMP = 2;

        // buttons
        Button button_1 = (Button) findViewById(R.id.button_1);
        Button button_2 = (Button) findViewById(R.id.button_2);
        Button button_3 = (Button) findViewById(R.id.button_3);
        Button button_4 = (Button) findViewById(R.id.button_4);

        button_1.setOnClickListener(this);
        button_2.setOnClickListener(this);
        button_3.setOnClickListener(this);
        button_4.setOnClickListener(this);

        buttonList = new ArrayList<Button>();
        buttonList.add(button_1);
        buttonList.add(button_2);
        buttonList.add(button_3);
        buttonList.add(button_4);

        // bootstrap game
        resetGame();
        setupGameLoop();
        startGame();
    }

    @Override
    protected void setColorsOnButtons() {
        int color  = Color.parseColor(BetterColor.getColor());
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        Random rand = new Random();

        for (Button button : buttonList) {
            int alpha = 20 + rand.nextInt(235);
            button.setBackgroundColor(Color.argb(alpha, red, green, blue));
        }
    }

    @Override
    protected void calculatePoints(View clickedView) {
        ColorDrawable clickedColor = (ColorDrawable) clickedView.getBackground();
        int clickedAlpha = Color.alpha(clickedColor.getColor());

        int lightestColor = clickedAlpha;
        for (Button button : buttonList) {
            ColorDrawable color = (ColorDrawable) button.getBackground();
            int alpha = Color.alpha(color.getColor());
            if (alpha < lightestColor) {
                lightestColor = alpha;
            }
        }

        // correct guess
        if (lightestColor == clickedAlpha) {
            updatePoints();
        } else {
            endGame();
        }
    }

    @Override
    public void onClick(View view) {
        if (!gameStart) return;
        calculatePoints(view);
        setColorsOnButtons();
    }
}
