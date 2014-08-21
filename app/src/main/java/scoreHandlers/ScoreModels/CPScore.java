package scoreHandlers.ScoreModels;

public class CPScore {

    private String player;
    private int score;

    public CPScore(String player, int score) {
        this.player = player;
        this.score = score;
    }

    public String getPlayer() {
        return player;
    }
    public int getScore() {
        return score;
    }
}