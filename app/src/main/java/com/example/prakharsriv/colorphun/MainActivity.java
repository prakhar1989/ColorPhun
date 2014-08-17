package com.example.prakharsriv.colorphun;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {

    private Button topBtn, bottomBtn;
    private int level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        topBtn = (Button) findViewById(R.id.top_button);
        bottomBtn = (Button) findViewById(R.id.bottom_button);

        topBtn.setOnClickListener(this);
        bottomBtn.setOnClickListener(this);

        // initialize level
        level = 1;
        setColorsOnButtons();
    }

    private void setColorsOnButtons() {
        Pair<Integer, Integer> colorPair = getRandomColor(level);
        topBtn.setBackgroundColor(colorPair.first);
        bottomBtn.setBackgroundColor(colorPair.second);
    }

    @Override
    public void onClick(View view) {
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
        int alpha2 = alpha1 > 227 ? alpha1 - delta : alpha1 + delta;

        int color1 = Color.argb(alpha1, red, green, blue);
        int color2 = Color.argb(alpha2, red, green, blue);

        Pair<Integer, Integer> colorPair = new Pair(color1, color2);
        return colorPair;
    }
}
