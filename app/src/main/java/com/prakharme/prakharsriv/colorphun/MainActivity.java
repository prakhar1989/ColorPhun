package com.prakharme.prakharsriv.colorphun;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;

public class MainActivity extends Activity {

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
    private static final int START_TIMER = 200;
    private static final int FPS = 100;
    private static final int LEVEL = 25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        topBtn = (Button) findViewById(R.id.top_button);
        bottomBtn = (Button) findViewById(R.id.bottom_button);

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
    protected void onRestart() {
        super.onRestart();
        endGame();
    }

    @Override
    protected void onStop() {
        super.onStop();
        gameStart = false;
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

        Toast.makeText(this, R.string.game_help, Toast.LENGTH_SHORT).show();
        setColorsOnButtons();

        // start timer
        timer = START_TIMER;
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void endGame() {
        gameStart = false;

        // PERSIST points in shared preferences
        SharedPreferences preferences = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        int highScore = preferences.getInt("HIGHSCORE", 0);
        // update the highscore
        if (points > highScore) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("HIGHSCORE", points);
            editor.commit();
            highScore = points;
        }

        // Send data to another activity
        Intent intent = new Intent(this, GameOverActivity.class);
        intent.putExtra("points", points);
        intent.putExtra("level", level);
        intent.putExtra("best", highScore);
        startActivity(intent);

        // finish main game activity
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

//        int color = Color.HSVToColor(BetterColor.getColor());
//        int red = Color.red(color);
//        int green = Color.green(color);
//        int blue = Color.blue(color);

        String[] colorList = {
                "#1391FF",
                "#d9881b",
                "#00AAAA",
                "#00e598",
                "#006e49",
                "#804500",
                "#ffa012",
                "#0000FF",
                "#8A2BE2",
                "#4d1284",
                "#330c57",
                "#A52A2A",
                "#cc3636",
                "#c88937",
                "#5F9EA0",
                "#467475",
                "#6edd00",
                "#D2691E",
                "#fa4300",
                "#c73500",
                "#a52c00",
                "#ff5a1d",
                "#6495ED",
                "#fecb00",
                "#DC143C",
                "#00008B",
                "#008B8B",
                "#B8860B",
                "#f1b216",
                "#A9A9A9",
                "#006400",
                "#00b900",
                "#BDB76B",
                "#8B008B",
                "#cf00cf",
                "#360036",
                "#556B2F",
                "#FF8C00",
                "#9932CC",
                "#8B0000",
                "#ff0303",
                "#d95224",
                "#8FBC8F",
                "#5c9a5c",
                "#483D8B",
                "#6457b5",
                "#2F4F4F",
                "#00CED1",
                "#9400D3",
                "#f10082",
                "#00BFFF",
                "#696969",
                "#1E90FF",
                "#B22222",
                "#ffab02",
                "#2fc22f",
                "#228B22",
                "#bb00bb",
                "#FF00FF",
                "#00004e",
                "#FFD700",
                "#DAA520",
                "#8eea00",
                "#00ce00",
                "#00ce00",
                "#CD5C5C",
                "#bc3a3a",
                "#4B0082",
                "#4a4adb",
                "#20209f",
                "#74eb00",
                "#ADD8E6",
                "#6cb9d2",
                "#F08080",
                "#00f1f1",
                "#e6e61b",
                "#64e764",
                "#ff3f5c",
                "#ff5714",
                "#be3600",
                "#471400",
                "#20B2AA",
                "#87CEFA",
                "#778899",
                "#7497c5",
                "#4874ad",
                "#00FF00",
                "#00CC00",
                "#32CD32",
                "#ca7928",
                "#FF00FF",
                "#440044",
                "#800000",
                "#66CDAA",
                "#0000CD",
                "#78248d",
                "#BA55D3",
                "#6334bf",
                "#9370D8",
                "#3CB371",
                "#2c15b9",
                "#7B68EE",
                "#00e990",
                "#48D1CC",
                "#C71585",
                "#191970",
                "#f21800",
                "#ffb93e",
                "#4f3200",
                "#000080",
                "#808000",
                "#6B8E23",
                "#94c430",
                "#DA70D6",
                "#b52fb0",
                "#09e009",
                "#AFEEEE",
                "#5bdcdc",
                "#c93b6b",
                "#D87093",
                "#ffb53c",
                "#ffae2b",
                "#FFDAB9",
                "#CD853F",
                "#ba7430",
                "#c00022",
                "#DDA0DD",
                "#b541b5",
                "#B0E0E6",
                "#3db2c0",
                "#800080",
                "#663399",
                "#FF0000",
                "#9a5c5c",
                "#4169E1",
                "#8B4513",
                "#f73620",
                "#ef7711",
                "#2E8B57",
                "#3fbe77",
                "#ff7311",
                "#A0522D",
                "#24a3d7",
                "#6A5ACD",
                "#708090",
                "#830000",
                "#00cc66",
                "#D2B48C",
                "#008080",
                "#9e609e",
                "#f12500",
                "#21ccbb",
                "#db1edb",
                "#e19d1e",
                "#9ACD32"
        };
        Random random = new Random();
        int index = random.nextInt(colorList.length);
        int color = Color.parseColor(colorList[index]);
        Log.i("COLORPHUN", colorList[index]);
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