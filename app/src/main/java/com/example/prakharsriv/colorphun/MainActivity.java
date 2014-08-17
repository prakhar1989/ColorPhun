package com.example.prakharsriv.colorphun;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {

    private Button topBtn, bottomBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        topBtn = (Button) findViewById(R.id.top_button);
        bottomBtn = (Button) findViewById(R.id.bottom_button);

        topBtn.setOnClickListener(this);
        bottomBtn.setOnClickListener(this);

        setColorsOnButtons();
    }

    private void setColorsOnButtons() {
        Pair<Integer, Integer> colorPair = getRandomColor();
        topBtn.setBackgroundColor(colorPair.first);
        bottomBtn.setBackgroundColor(colorPair.second);
    }

    @Override
    public void onClick(View view) {
        setColorsOnButtons();
    }

    private Pair<Integer, Integer> getRandomColor() {
        int red = (int)(Math.random() * 255);
        int green = (int)(Math.random() * 255);
        int blue = (int)(Math.random() * 255);
        int color1 = Color.argb(255, red, green, blue);
        int color2 = Color.argb(235, red, green, blue);
        Pair<Integer, Integer> colorPair = new Pair(color1, color2);
        return colorPair;
    }
}
