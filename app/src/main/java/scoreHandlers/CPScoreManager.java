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
}
