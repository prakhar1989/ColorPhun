package scoreHandlers.ScoreModels;

public class CPScore {

    private String player;
    private int score;
    private int level;

    public CPScore(String player, int score, int level) {
        this.player = player;
        this.score = score;
        this.level = level;
    }

    public String getPlayer() {
        return player;
    }

    public int getScore() {
        return score;
    }

    public int getLevel() {
        return level;
    }
}
