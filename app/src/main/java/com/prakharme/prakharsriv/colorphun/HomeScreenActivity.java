package com.prakharme.prakharsriv.colorphun;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.example.games.basegameutils.BaseGameActivity;

public class HomeScreenActivity extends BaseGameActivity implements View.OnClickListener {

    private Button playGameButton;
    private View signInButton, signOutButton;
    private ImageView logoView;
    private TextView taglineTextView1, taglineTextView2, taglineTextView3;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        signInButton = findViewById(R.id.sign_in_button);
        signOutButton = findViewById(R.id.sign_out_button);
        signInButton.setOnClickListener(this);
        signOutButton.setOnClickListener(this);

        taglineTextView1 = (TextView) findViewById(R.id.tagline_text);
        taglineTextView2 = (TextView) findViewById(R.id.tagline_text2);
        taglineTextView3 = (TextView) findViewById(R.id.tagline_text3);
        playGameButton = (Button) findViewById(R.id.play_game_btn);
        logoView = (ImageView) findViewById(R.id.logo);

        // setting the typeface
        Typeface avenir_book = Typeface.createFromAsset(getAssets(), "fonts/avenir_book.ttf");
        taglineTextView1.setTypeface(avenir_book);
        taglineTextView2.setTypeface(avenir_book);
        taglineTextView3.setTypeface(avenir_book);

        // get user's preference for sign-in
        sharedPreferences = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        int shouldSignIn = sharedPreferences.getInt("SIGNIN", 1);
        getGameHelper().setMaxAutoSignInAttempts(shouldSignIn);
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            taglineTextView1.setVisibility(View.INVISIBLE);
            taglineTextView2.setVisibility(View.INVISIBLE);
            taglineTextView3.setVisibility(View.INVISIBLE);

            ValueAnimator bounceAnim = getBounceAnimator();
            ValueAnimator fadeAnim = getFadeAnimator();
            bounceAnim.setDuration(800);
            fadeAnim.setDuration(1000);

            bounceAnim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    taglineTextView1.setVisibility(View.VISIBLE);
                    taglineTextView2.setVisibility(View.VISIBLE);
                    taglineTextView3.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            AnimatorSet homeAnimation = new AnimatorSet();
            homeAnimation.playSequentially(bounceAnim, fadeAnim);
            homeAnimation.start();
        }
    }

    ValueAnimator getFadeAnimator() {
        ValueAnimator anim = ValueAnimator.ofFloat(0f, 1f);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                taglineTextView1.setAlpha(valueAnimator.getAnimatedFraction());
                taglineTextView2.setAlpha(valueAnimator.getAnimatedFraction());
                taglineTextView3.setAlpha(valueAnimator.getAnimatedFraction());
            }
        });
        return anim;
    }

    ValueAnimator getBounceAnimator() {
        ValueAnimator anim = ValueAnimator.ofFloat(0f, 1f);
        anim.setInterpolator(new BounceInterpolator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                logoView.setScaleX(valueAnimator.getAnimatedFraction());
                logoView.setScaleY(valueAnimator.getAnimatedFraction());
                logoView.setAlpha(valueAnimator.getAnimatedFraction());

                playGameButton.setScaleX(valueAnimator.getAnimatedFraction());
                playGameButton.setScaleY(valueAnimator.getAnimatedFraction());
                playGameButton.setAlpha(valueAnimator.getAnimatedFraction());
            }
        });
        return anim;
    }

    public void playGame(View view) {
        startActivity(new Intent(this, EasyGameActivity.class));
        //startActivity(new Intent(this, HardGameActivity.class));
    }

    // TODO: Find out the best practices for below events and handle them
    @Override
    public void onSignInFailed() {
        signInButton.setVisibility(View.VISIBLE);
        signOutButton.setVisibility(View.GONE);
        Log.e("SIGN IN", "ERROR Signin in home screen");
    }

    @Override
    public void onSignInSucceeded() {
        signInButton.setVisibility(View.GONE);
        signOutButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.sign_in_button) {
            beginUserInitiatedSignIn();
        } else if (view.getId() == R.id.sign_out_button) {
            signOut();

            // save user's preference for sign-in
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("SIGNIN", 0);
            editor.apply();

            signInButton.setVisibility(View.VISIBLE);
            signOutButton.setVisibility(View.GONE);
        }
    }
}
