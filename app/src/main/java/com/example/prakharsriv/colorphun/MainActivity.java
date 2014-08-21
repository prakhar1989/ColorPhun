package com.example.prakharsriv.colorphun;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import scoreHandlers.CPScoreManager;
import scoreHandlers.ScoreModels.CPScore;


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

    private ArrayList<CPScore> topScores;

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

    private void setColorsOnButtons() {
        Pair<Integer, Integer> colorPair = getRandomColor(level);
        topBtn.setBackgroundColor(colorPair.first);
        bottomBtn.setBackgroundColor(colorPair.second);
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
        topScores = new CPScoreManager(this).getTopScores();
        gameStart = true;
        setColorsOnButtons();

        // start timer
        timer = START_TIMER;
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void endGame() {
        gameStart = false;

        final Intent intent = new Intent(this, ScoreboardActivity.class);

        GameOverPopup.Builder popup = new GameOverPopup.Builder(this);
        popup.setTitle("Game Over!");
        popup.setMessage("Congrats! You scored " + points + " points");
        popup.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                startGame();
            }
        });
        popup.setNegativeButton("View Scores", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                updateScore(points);
                startActivity(intent);
                finish();
            }
        });
        popup.show();
        resetGame();
    }

    private void updateScore(final int points) {
        CPScore score = new CPScore();
        score.setPlayer("NA");
        score.setScore(points);
        boolean scoreUpdate = false;

        if(topScores!=null) {
            int scoreIndex = 0;
            for (CPScore cpScore : topScores) {
                if(cpScore.getScore() < score.getScore()) {
                    cpScore.setPlayer(score.getPlayer());
                    cpScore.setScore(score.getScore());
                    topScores.set(scoreIndex, cpScore);
                    scoreUpdate = true;
                    break;
                }
                scoreIndex = scoreIndex + 1;
            }
            if(!scoreUpdate) {
                topScores.add(score);
            }
        } else {
            topScores = new ArrayList<CPScore>(1);
            topScores.add(score);
        }

        CPScoreManager scoreManager = new CPScoreManager(MainActivity.this);
        scoreManager.updateScores(topScores);
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
        if (alpha1 > alpha2) {
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