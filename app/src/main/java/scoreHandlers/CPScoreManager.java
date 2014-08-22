package scoreHandlers;

import android.content.Context;
import java.util.ArrayList;
import scoreHandlers.ScoreModels.CPScore;

public class CPScoreManager {

    Context mContext;
    DBHandler dbHandler;

    // max scores to keep in database
    private final int MAX_SCORES_COUNT = 5;

    public CPScoreManager(Context context) {
        this.mContext = context;
        this.dbHandler = new DBHandler(this.mContext);
    }

    // returns all the scores stored
    public ArrayList<CPScore> getTopScores() {
        return  dbHandler.getScores();
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

    // Returns a boolean to indicate where a score will be inserted
    public boolean newTopScore(int points, int level) {
        ArrayList<CPScore> scores = getTopScores();
        CPScore score = new CPScore(getPlayerName(), points, level);
        return scores == null
                || (scores.size() < MAX_SCORES_COUNT && !dbHandler.hasScore(score))
                || (scores.size() == MAX_SCORES_COUNT && !dbHandler.hasScore(score) &&
                        scores.get(MAX_SCORES_COUNT - 1).getScore() < points);
    }

    /* Adds a unique non-zero score to the db.
     * Updates lowest score if number of records = MAX_SCORES_COUNT
     */
    public void addScore(int points, int level) {
        CPScore score = new CPScore(getPlayerName(), points, level);
        if (!dbHandler.hasScore(score) && score.getScore() > 0) {
            ArrayList<CPScore> scores = getTopScores();
            if (scores == null || scores.size() < MAX_SCORES_COUNT) {
                dbHandler.insertScore(score);
            } else {
                dbHandler.updateScore(scores.get(MAX_SCORES_COUNT - 1), score);
            }
        }
    }
}