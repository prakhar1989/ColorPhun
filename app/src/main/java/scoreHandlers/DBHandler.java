package scoreHandlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import scoreHandlers.ScoreModels.CPScore;

public class DBHandler extends SQLiteOpenHelper {

    private final String TBLSCOREBOARD = "tblScoreBoard";
    private final String SELECT_SCRORE = "SELECT * FROM " + TBLSCOREBOARD + " ORDER BY score DESC LIMIT 5";
    private final String SELECT_TOPSCRORE = "SELECT * FROM " + TBLSCOREBOARD + " ORDER BY score DESC LIMIT 1";

    public DBHandler(Context context) {
        super(context, "cpScoreBoard", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE " + TBLSCOREBOARD + " (scroreIndex INTEGER PRIMARY KEY AUTOINCREMENT, player TEXT, score INTEGER);";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TBLSCOREBOARD);
        onCreate(sqLiteDatabase);
    }

    public void tuncateScores() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM  " + TBLSCOREBOARD);
        db.close();
    }

    public boolean addScrore (CPScore score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("player", score.getPlayer());
        contentValues.put("score", score.getScore());

        db.insert(TBLSCOREBOARD, null, contentValues);
        db.close();
        return true;
    }

    public ArrayList<CPScore> getScoreCards(boolean getHighScorer) {

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<CPScore> topScores = null;
        Cursor resultSet =  db.rawQuery(getHighScorer? SELECT_TOPSCRORE : SELECT_SCRORE, null );

        resultSet.moveToFirst();

        if(resultSet.getCount()>0) {
            topScores = new ArrayList<CPScore>(resultSet.getCount());
            if(!getHighScorer  && resultSet.getCount() > 1) {
                resultSet.moveToNext();
                while ( resultSet.moveToNext()) {
                    CPScore score = new CPScore();
                    score.setPlayer(resultSet.getString(resultSet.getColumnIndex("player")));
                    score.setScore(resultSet.getInt(resultSet.getColumnIndex("score")));
                    topScores.add(score);
                }
            } else {
                CPScore score = new CPScore();
                score.setPlayer(resultSet.getString(resultSet.getColumnIndex("player")));
                score.setScore(resultSet.getInt(resultSet.getColumnIndex("score")));
                topScores.add(score);
            }
        }
        db.close();
        return topScores;
    }
}
