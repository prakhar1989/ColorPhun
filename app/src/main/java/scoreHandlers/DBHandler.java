package scoreHandlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import scoreHandlers.ScoreModels.CPScore;

public class DBHandler extends SQLiteOpenHelper {

    private final String SCOREBOARD_TABLE = "tblScoreBoard";

    public DBHandler(Context context) {
        super(context, "cpScoreBoard", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE " + SCOREBOARD_TABLE + " (" +
                "scoreIndex INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "player TEXT, " +
                "score INTEGER);";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SCOREBOARD_TABLE);
        onCreate(sqLiteDatabase);
    }

    // inserts a score in the database
    public boolean insertScore (CPScore score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("player", score.getPlayer());
        contentValues.put("score", score.getScore());

        db.insert(SCOREBOARD_TABLE, null, contentValues);
        db.close();
        return true;
    }

    // returns an array list of score records
    public ArrayList<CPScore> getScores() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<CPScore> scoresList = null;
        Cursor resultSet =  db.rawQuery("SELECT * FROM " + SCOREBOARD_TABLE +
                                        " ORDER BY score DESC", null);
        resultSet.moveToFirst();

        if(resultSet.getCount() > 0) {
            scoresList = new ArrayList<CPScore>(resultSet.getCount());
            resultSet.moveToNext();
            while (resultSet.moveToNext()) {
                CPScore score = new CPScore();
                score.setPlayer(resultSet.getString(resultSet.getColumnIndex("player")));
                score.setScore(resultSet.getInt(resultSet.getColumnIndex("score")));
                scoresList.add(score);
            }
        }
        db.close();
        return scoresList;
    }
}
