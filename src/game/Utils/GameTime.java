package game.Utils;

import java.io.Serializable;

public class GameTime implements Serializable {
    private static long startTime;
    private static long pauseStartTime; // Время начала паузы
    private static long totalPausedTime = 0; // Общее время в паузе
    public static final long GAME_MINUTE_IN_MS = 300;

    private static boolean paused = false;

    public static void init() {
        startTime = System.currentTimeMillis();
        totalPausedTime = 0;
    }

    public static void pause() {
        if (!paused) {
            paused = true;
            pauseStartTime = System.currentTimeMillis();
        }
    }

    public static void resume() {
        if (paused) {
            paused = false;
            totalPausedTime += System.currentTimeMillis() - pauseStartTime;
        }
    }

    public static boolean isPaused() {
        return paused;
    }

    // Получаем реальное игровое время (исключая время паузы)
    private static long getCurrentGameTime() {
        long currentTime = System.currentTimeMillis();
        if (paused) {
            return pauseStartTime - startTime - totalPausedTime;
        }
        return currentTime - startTime - totalPausedTime;
    }

    public static long getMinutes() {
        return getCurrentGameTime() / GAME_MINUTE_IN_MS;
    }

    public static long convertMinutes(long minutes) {
        return minutes * GAME_MINUTE_IN_MS;
    }

    public static int getOnlyMinutes() {
        return (int) (getMinutes() % 60);
    }

    public static int getOnlyMinutes(long minutes) {
        return (int) (minutes % 60);
    }

    public static int getHours() {
        return (int) (getMinutes() / 60);
    }

    public static int getHours(long minutes) {
        return (int) (minutes / 60);
    }

    public static int getOnlyHours() {
        return (int) (getHours() % 24);
    }

    public static int getOnlyHours(long minutes) {
        return (int) (getHours(minutes) % 24);
    }

    public static int getDays() {
        return getHours() / 24;
    }

    public static int getDays(long minutes) {
        return getHours(minutes) / 24;
    }

    public static String getFullTime() {
        return "День " + getDays() + " - " + String.format("%02d:%02d", getOnlyHours(), getOnlyMinutes());
    }

    public static String formatMinutes(long remains) {
        return "День " + getDays(remains) + " - " +
                String.format("%02d:%02d", getOnlyHours(remains), getOnlyMinutes(remains));
    }
}