package game.Utils.Menu;

import game.Castle.Buildings.Building;
import game.Utils.Logs.GameLogger;

import java.util.List;
import java.util.stream.Collectors;

public class Menu {
    public static void wrongChoice() {
        errorMessage("Неверный выбор.");
        GameLogger.warn("Неверный выбор");
    }

    public static void print(String message) {
        System.out.print(message);
    }

    public static void println(String message) {
        System.out.println(message);
    }

    public static void println(int message) {
        System.out.println(message);
    }

    public static void errorMessage(String message) {
        try {
            System.err.flush();
            System.err.println(message);
            GameLogger.warn(message);
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            GameLogger.error("Задержка вывода сообщения была прервана: " + e.getMessage());
        }
    }

    public static void errorMessage(String message, boolean isErrorLog) {

        System.err.flush();
        System.err.println(message);

        if (isErrorLog) {
            GameLogger.error(message);
        } else {
            GameLogger.warn(message);
        }

        sleep(200);
    }

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }

    public static void displayMenu(List<Building> items, boolean priceDisplay) {
        for (int i = 0; i < items.size(); i++) {
            String name = items.get(i).getName();
            int price = items.get(i).getCost();

            if (priceDisplay)
                System.out.printf("%d - %s (%d золота) \t\t", i + 1, name, price);
            else
                System.out.printf("%d - %s \t\t", i + 1, name);
        }
        print("0 - Выйти \n");
    }

    public static <T> void displayArrays(List<T> list) {
        for (int i = 0; i < list.size(); i++) {
            println((i + 1) + " - " + list.get(i));
        }
    }

    public static void printFormattedMessage(String message) {
        String border = "=".repeat(message.length() * 2);
        String formattedMessage = message.toUpperCase()
                .chars()
                .mapToObj(c -> (char) c + " ")
                .collect(Collectors.joining())
                .trim();

        println(border);
        println(formattedMessage);
        println(border);
    }
}