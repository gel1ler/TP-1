package game.Utils;

import java.io.Serializable;

public class GameTime extends Thread implements Serializable {
    private static long startTime;
    private static final long GAME_MINUTE_IN_MS = 100;

    public static void init() {
        startTime = System.currentTimeMillis();
    }

    @Override
    public void run() {
        startTime = System.currentTimeMillis();
    }

    public static Long getMinutes() {
        return (System.currentTimeMillis() - startTime) / GAME_MINUTE_IN_MS;
    }

    public static int getOnlyMinutes() {
        return (int) (getMinutes() % 60);
    }

    public static int getHours() {
        return (int) (getMinutes() / 60);
    }

    public static int getOnlyHours() {
        return (int) (getHours() % 24);
    }

    public static int getDays() {
        return getHours() / 24;
    }

    public static String getFullTime() {
        return "День " + getDays() + " - " + getOnlyHours() + ":" + getOnlyMinutes();
    }
}