package com.example.prakharsriv.colorphun;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {

    private Button topBtn, bottomBtn;
    private TextView pointsTextView, levelTextView;
    private int level;
    private int points;
    private final int POINT_INCREMENT = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        topBtn = (Button) findViewById(R.id.top_button);
        bottomBtn = (Button) findViewById(R.id.bottom_button);

        pointsTextView = (TextView) findViewById(R.id.points_value);
        levelTextView = (TextView) findViewById(R.id.level_value);

        // initialize the crew
        resetGame();

        // set the stage
        topBtn.setOnClickListener(this);
        bottomBtn.setOnClickListener(this);

        // begin the show
        setColorsOnButtons();
    }

    private void setColorsOnButtons() {
        Pair<Integer, Integer> colorPair = getRandomColor(level);
        topBtn.setBackgroundColor(colorPair.first);
        bottomBtn.setBackgroundColor(colorPair.second);
    }

    private void resetGame() {
        level = 1;
        points = 0;
        pointsTextView.setText(Integer.toString(points));
        levelTextView.setText(Integer.toString(level));
    }

    @Override
    public void onClick(View view) {

        Drawable clickedBtnBg = view.getBackground();
        Drawable unclickedBtnBg;
        if (view == topBtn) {
            unclickedBtnBg = bottomBtn.getBackground();
        } else {
            unclickedBtnBg = topBtn.getBackground();
        }

        int alpha1 = clickedBtnBg.getAlpha();
        int alpha2 = unclickedBtnBg.getAlpha();

        if (alpha1 > alpha2) {
            points = points + POINT_INCREMENT;
            pointsTextView.setText(Integer.toString(points));
        } else {
            Toast.makeText(this, "Game over!", Toast.LENGTH_SHORT).show();
            resetGame();
        }

        setColorsOnButtons();
    }

    // generates a pair of colors separated by alpha controlled by a level
    private Pair<Integer, Integer> getRandomColor(int level) {
        int red = (int)(Math.random() * 255);
        int green = (int)(Math.random() * 255);
        int blue = (int)(Math.random() * 255);

        // TODO: Improve the formula for alphas
        int alpha1 = 200 + (int)(Math.random() * 55);
        int delta = (10 - level) * 2;
        int alpha2 = alpha1 + delta * (alpha1 > 227 ? -1 : 1);

        int color1 = Color.argb(alpha1, red, green, blue);
        int color2 = Color.argb(alpha2, red, green, blue);

        Pair<Integer, Integer> colorPair = new Pair(color1, color2);
        return colorPair;
    }
}
