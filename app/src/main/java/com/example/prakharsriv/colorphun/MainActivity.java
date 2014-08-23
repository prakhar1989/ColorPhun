package com.example.prakharsriv.colorphun;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prakharsriv.colorphun.util.BetterColor;

import java.util.Random;

import scoreHandlers.CPScoreManager;


public class MainActivity extends Activity implements View.OnClickListener {

    private Button topBtn, bottomBtn;
    private TextView pointsTextView, levelTextView;
    private ProgressBar timerProgress;

    private int level, points;
    private boolean gameStart = false;
    private Runnable runnable;
    private int timer;

    private static final int POINT_INCREMENT = 2;
    private static int TIMER_DELTA = -1;
    private static final int START_TIMER = 400;
    private static final int FPS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        topBtn = (Button) findViewById(R.id.top_button);
        bottomBtn = (Button) findViewById(R.id.bottom_button);

        pointsTextView = (TextView) findViewById(R.id.points_value);
        levelTextView = (TextView) findViewById(R.id.level_value);

        timerProgress = (ProgressBar) findViewById(R.id.progress_bar);

        final Handler handler = new Handler();

        // initialize the crew
        resetGame();

        // set the stage
        topBtn.setOnClickListener(this);
        bottomBtn.setOnClickListener(this);

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

        startGame();
    }

    @Override
    protected void onPause(){
        super.onPause();
        gameStart = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameStart = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.finish();
    }

    private void setColorsOnButtons() {
        int[] colorPair = getRandomColor(level);
        topBtn.setBackgroundColor(colorPair[0]);
        bottomBtn.setBackgroundColor(colorPair[1]);
    }

    private void resetGame() {
        gameStart = false;
        level = 1;
        points = 0;

        // update view
        pointsTextView.setText(Integer.toString(points));
        levelTextView.setText(Integer.toString(level));
        timerProgress.setProgress(0);
    }

    private void startGame() {
        gameStart = true;

        Toast.makeText(this, R.string.game_help, Toast.LENGTH_LONG).show();
        setColorsOnButtons();

        // start timer
        timer = START_TIMER;
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void endGame() {
        gameStart = false;
        String popMsg = "You scored: " + points + " points";

        final Intent intent = new Intent(this, HomeScreenActivity.class);
        final CPScoreManager scoreManager = new CPScoreManager(this);
        if (points > 0 && scoreManager.newTopScore(points, level)) {
            scoreManager.addScore(points, level);
            popMsg = "New Top Score! " + popMsg;
        }

        GameOverPopup.Builder popup = new GameOverPopup.Builder(this);

        popup.setTitle("Game Over!");
        popup.setMessage(popMsg);

        popup.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                startGame();
            }
        });
        popup.setNegativeButton("Main Menu", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                startActivity(intent);
                finish();
            }
        });
        popup.show();
        resetGame();
    }

    @Override
    public void onClick(View view) {
        if (!gameStart) return;
        calculatePoints(view);
        setColorsOnButtons();
    }

    private void calculatePoints(View clickedView) {

        View unclickedView = clickedView == topBtn ? bottomBtn : topBtn;
        ColorDrawable clickedColor = (ColorDrawable) clickedView.getBackground();
        ColorDrawable unClickedColor = (ColorDrawable) unclickedView.getBackground();

        int alpha1 =  Color.alpha(clickedColor.getColor());
        int alpha2 = Color.alpha(unClickedColor.getColor());

        // correct guess
        if (alpha1 < alpha2) {
            points = points + POINT_INCREMENT;
            TIMER_DELTA = -TIMER_DELTA;
            pointsTextView.setText(Integer.toString(points));

            // increment level
            if (points > level * 50) {
                level += 1;
                TIMER_DELTA = level;
                levelTextView.setText(Integer.toString(level));
            }
        } else {
            // incorrect guess
            endGame();
        }
    }

    // generates a pair of colors separated by alpha controlled by a level
    private int[] getRandomColor(int level) {

        int color = Color.HSVToColor(BetterColor.getColor());
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);

        // generate 2 alpha values separated by a random delta
        // TODO: Reduce probability of very bright colors
        Random random = new Random();
        int alpha1 = 185 + random.nextInt(70);
        int delta = (6 - level) * 7;
        int alpha2 = alpha1 + delta * (alpha1 > 220 ? -1 : 1);

        int color1 = Color.argb(alpha1, red, green, blue);
        int color2 = Color.argb(alpha2, red, green, blue);

        return new int[] {color1, color2};
    }
}