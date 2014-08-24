package com.example.prakharsriv.colorphun;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomeScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        // setting the typeface
        TextView taglineTextView1 = (TextView) findViewById(R.id.tagline_text);
        TextView taglineTextView2 = (TextView) findViewById(R.id.tagline_text2);
        TextView taglineTextView3 = (TextView) findViewById(R.id.tagline_text3);

        Typeface avenir_book = Typeface.createFromAsset(getAssets(), "fonts/avenir_book.ttf");
        taglineTextView1.setTypeface(avenir_book);
        taglineTextView2.setTypeface(avenir_book);
        taglineTextView3.setTypeface(avenir_book);
    }

    public void playGame(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

}
