package game.Utils;

public class GameTime {
    private long startTime;

    private static final long GAME_MINUTE_IN_MS = 100;

    public GameTime() {
        this.startTime = System.currentTimeMillis();
    }

    public long getCurrentGameTime() {
        return (System.currentTimeMillis() - startTime) / GAME_MINUTE_IN_MS;
    }

    public static long realMinutesToGame(long realMinutes) {
        return realMinutes * 60; // 60 игровых минут = 1 реальная минута
    }
}