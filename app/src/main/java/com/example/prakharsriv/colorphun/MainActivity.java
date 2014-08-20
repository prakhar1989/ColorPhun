package com.example.prakharsriv.colorphun;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Swapnil on 8/20/14.
 */
public class MainActivity extends Activity implements View.OnClickListener {

    private Button topBtn, bottomBtn, startBtn;
    private TextView pointsTextView, levelTextView;
    private ProgressBar timerProgress;

    private int level, points;
    private boolean gameStart = false;
    private Runnable runnable;
    private int timer;

    private static final int POINT_INCREMENT = 2;
    private static int TIMER_DELTA = -1;
    private static final int START_TIMER = 100;
    private static final int FPS = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        topBtn = (Button) findViewById(R.id.top_button);
        bottomBtn = (Button) findViewById(R.id.bottom_button);
        startBtn = (Button) findViewById(R.id.start_button);

        pointsTextView = (TextView) findViewById(R.id.points_value);
        levelTextView = (TextView) findViewById(R.id.level_value);

        timerProgress = (ProgressBar) findViewById(R.id.progress_bar);

        final Handler handler = new Handler();

        // initialize the crew
        resetGame();

        // set the stage
        topBtn.setOnClickListener(this);
        bottomBtn.setOnClickListener(this);

        startBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                startGame();
            }
        });

        runnable = new Runnable() {
            @Override
            public void run() {
                while (timer > 0 && gameStart) {
                    synchronized (this) {
                        try {
                            wait(FPS);
                        } catch (InterruptedException e) {
                            Log.i("THREAD ERROR", e.getMessage());
                        }
                        timer = timer + TIMER_DELTA;
                        if (TIMER_DELTA > 0) {
                            TIMER_DELTA = -TIMER_DELTA;
                        }
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            timerProgress.setProgress(timer);
                        }
                    });
                }
                if (gameStart) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            endGame();
                        }
                    });
                }
            }
        };
    }


    private void setColorsOnButtons() {
        Pair<Integer, Integer> colorPair = getRandomColor(level);
        topBtn.setBackgroundColor(colorPair.first);
        bottomBtn.setBackgroundColor(colorPair.second);
    }

    private void resetGame() {
        gameStart = false;
        level = 1;
        points = 0;
        pointsTextView.setText(Integer.toString(points));
        levelTextView.setText(Integer.toString(level));
        startBtn.setVisibility(View.VISIBLE);
        timerProgress.setProgress(0);
    }

    private void startGame() {
        gameStart = true;
        startBtn.setVisibility(View.INVISIBLE);
        setColorsOnButtons();
        timer = START_TIMER;
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void endGame() {
        gameStart = false;
        GameOverPopup.Builder popup = new GameOverPopup.Builder(this);
        popup.setTitle("Game Over!");
        popup.show();
        resetGame();
    }

    @Override
    public void onClick(View view) {
        if (!gameStart) return;
        calculatePoints(view);
        setColorsOnButtons();
    }

    // updates points. Takes the view clicked as a parameter
    private void calculatePoints(View clickedView) {
        Drawable clickedBtnBg = clickedView.getBackground();
        Drawable unclickedBtnBg = clickedView == topBtn ? bottomBtn.getBackground()
                                                        : topBtn.getBackground();

        int alpha1 = clickedBtnBg.getAlpha();
        int alpha2 = unclickedBtnBg.getAlpha();

        // correct guess
        if (alpha1 > alpha2) {
            points = points + POINT_INCREMENT;
            TIMER_DELTA = -TIMER_DELTA;
            pointsTextView.setText(Integer.toString(points));

            // set the level
            if (points > level * 50) {
                level += 1;
                TIMER_DELTA = level;
                levelTextView.setText(Integer.toString(level));
            }
        } else {
            endGame();
        }
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

        return new Pair(color1, color2);
    }
}