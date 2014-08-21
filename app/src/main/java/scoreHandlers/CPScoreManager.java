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

    // adds a score to the db, takes points as an input
    public void addScore(final int points) {
        // ArrayList<CPScore> scores = this.getTopScores();
        CPScore score = new CPScore();
        score.setPlayer("NA");
        score.setScore(points);
        dbHandler.insertScore(score);

//        if(topScores!=null) {
//            int scoreIndex = 0;
//            for (CPScore cpScore : topScores) {
//                if(cpScore.getScore() < score.getScore()) {
//                    cpScore.setPlayer(score.getPlayer());
//                    cpScore.setScore(score.getScore());
//                    topScores.set(scoreIndex, cpScore);
//                    scoreUpdate = true;
//                    break;
//                }
//                scoreIndex = scoreIndex + 1;
//            }
//            if(!scoreUpdate) {
//                topScores.add(score);
//            }
//        } else {
//            topScores = new ArrayList<CPScore>(1);
//            topScores.add(score);
//        }
//        this.updateScores(topScores);
    }
}