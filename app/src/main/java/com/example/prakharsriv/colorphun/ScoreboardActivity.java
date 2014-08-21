package com.example.prakharsriv.colorphun;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScoreboardActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        ListView scoreListView = (ListView) findViewById(R.id.scores_list_view);

        String[] scores = {
               "Prakhar               50",
               "MZafar                40",
               "Swapnil               30",
               "Prakhar               10"
        };

        List<String> testList = new ArrayList<String>(Arrays.asList(scores));
        ArrayAdapter<String> scoreAdapter = new ArrayAdapter<String>(this,
                                                    R.layout.score_list_item,
                                                    R.id.score_list_item_view,
                                                    testList);
        scoreListView.setAdapter(scoreAdapter);
    }

}
