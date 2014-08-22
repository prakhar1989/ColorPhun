package com.example.prakharsriv.colorphun;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;
import scoreHandlers.CPScoreManager;
import scoreHandlers.ScoreModels.CPScore;

public class ScoreboardActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        final CPScoreManager scoreManager = new CPScoreManager(this);
        ArrayList<CPScore> scores = scoreManager.getTopScores();
        List<String> stringScores = new ArrayList<String>();

        if (scores != null) {
            stringScores.add("NAME        SCORE          LEVEL");
            for (CPScore score: scores) {
                // TODO: Make this better by using fixed-width strings
                stringScores.add(score.getPlayer() + "                 " +
                        score.getScore() +  "               " + score.getLevel());
            }
        }

        ArrayAdapter<String> scoreAdapter = new ArrayAdapter<String>(this,
                                                    R.layout.score_list_item,
                                                    R.id.score_list_item_view,
                                                    stringScores);

        ListView scoreListView = (ListView) findViewById(R.id.scores_list_view);
        scoreListView.setAdapter(scoreAdapter);
    }

}
