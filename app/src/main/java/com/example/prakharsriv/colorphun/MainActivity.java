package com.example.prakharsriv.colorphun;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.app.DialogFragment;
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
import scoreHandlers.CPScoreManager;

public class MainActivity extends Activity implements GameOverPopup.GameOverPopupListener {

    private Button topBtn, bottomBtn;
    private TextView pointsTextView, levelTextView;
    private ProgressBar timerProgress;

    private int level, points;
    private boolean gameStart = false;
    private Runnable runnable;
    private int timer;

    private AnimatorSet pointAnim, levelAnim;

    private static final int POINT_INCREMENT = 2;
    private static int TIMER_DELTA = -1;
    private static final int TIMER_BUMP = 2;
    private static final int START_TIMER = 300;
    private static final int FPS = 100;
    private static final int LEVEL = 50;

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


        // setting up listeners on both buttons
        topBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!gameStart) return;
                calculatePoints(view);
                setColorsOnButtons();
            }
        });

        // TODO: See if this duplication can be removed
        bottomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!gameStart) return;
                calculatePoints(view);
                setColorsOnButtons();
            }
        });

        // setting up animations
        pointAnim = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.points_animations);
        pointAnim.setTarget(pointsTextView);

        levelAnim = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.level_animations);
        levelAnim.setTarget(levelTextView);


        // setting up the game loop
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
                            TIMER_DELTA = -TIMER_DELTA / TIMER_BUMP;
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
        int[] colorPair = getRandomColor();
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

        final CPScoreManager scoreManager = new CPScoreManager(this);
        if (points > 0 && scoreManager.newTopScore(points, level)) {
            scoreManager.addScore(points, level);
            popMsg = "New Top Score! " + popMsg;
        }

        Bundle args = new Bundle();
        args.putString("message", popMsg);
        DialogFragment newFragment = new GameOverPopup();
        newFragment.setArguments(args);
        newFragment.show(this.getFragmentManager(), "dialog");
        resetGame();
    }


    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        startGame();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        final Intent intent = new Intent(this, HomeScreenActivity.class);
        startActivity(intent);
        finish();
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
            TIMER_DELTA = -TIMER_BUMP * TIMER_DELTA; // give a timer bump
            pointsTextView.setText(Integer.toString(points));
            pointAnim.start();

            // increment level
            if (points > level * LEVEL) {
                level += 1;
                TIMER_DELTA = level;
                levelTextView.setText(Integer.toString(level));
                levelAnim.start();
            }
        } else {
            // incorrect guess
            endGame();
        }
    }

    // generates a pair of colors separated by alpha
    private int[] getRandomColor() {

        float[] betterColor = BetterColor.getColor();
        Log.i("COLORS", "Color generated - H: " + betterColor[0] +
                            " S: " + betterColor[1] + " V: " +  betterColor[2]);

        int color = Color.HSVToColor(betterColor);
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

        int color1 = Color.argb(alpha1, red, green, blue);
        int color2 = Color.argb(alpha2, red, green, blue);

        return new int[] {color1, color2};
    }

}