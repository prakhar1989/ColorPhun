package scoreHandlers;

import android.content.Context;
import java.util.ArrayList;
import scoreHandlers.ScoreModels.CPScore;

public class CPScoreManager {
    boolean TOPSCRORES = false;
    boolean HIGHSCRORE = true;

    Context mContext;
    DBHandler dbHandler;

    public CPScoreManager(Context context) {
        this.mContext = context;
        this.dbHandler = new DBHandler(this.mContext);
    }

    public ArrayList<CPScore> getTopScores() {
        return  dbHandler.getScoreCards(TOPSCRORES);
    }

    public CPScore getHighScore() {
        ArrayList<CPScore> highScorer = dbHandler.getScoreCards(HIGHSCRORE);
        return highScorer == null ? null : highScorer.get(0);
    }

    public void updateScores(ArrayList<CPScore> scores) {
        this.dbHandler.tuncateScores();
        if(scores!=null && scores.size()>0) {
            for (CPScore score : scores) {
                this.dbHandler.addScrore(score);
            }
        }
    }

    public void addScore(final int points) {
        ArrayList<CPScore> topScores = this.getTopScores();
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
        this.updateScores(topScores);
    }
}