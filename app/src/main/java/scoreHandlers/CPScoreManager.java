package scoreHandlers;

import android.content.Context;
import java.util.ArrayList;
import scoreHandlers.ScoreModels.CPScore;

public class CPScoreManager {

    Context mContext;
    DBHandler dbHandler;

    public CPScoreManager(Context context) {
        this.mContext = context;
        this.dbHandler = new DBHandler(this.mContext);
    }

    // returns all the scores stored
    public ArrayList<CPScore> getTopScores() {
        return  dbHandler.getScores();
    }

    // returns the highest score
    public CPScore getHighScore() {
        ArrayList<CPScore> scores = getTopScores();
        return scores == null ? null : scores.get(0);
    }

    // clears all scores
    public boolean clearScores() {
        dbHandler.purgeScores();
        return true;
    }

    // TODO: Use phone defaults here
    public String getPlayerName() {
        return "User";
    }

    // adds a score to the db, takes points as an input
    public void addScore(int points) {
        CPScore score = new CPScore(getPlayerName(), points);
        dbHandler.insertScore(score);
    }
}