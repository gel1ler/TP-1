package game.Utils;

import game.Utils.Menu.MainMenu;
import game.Utils.Menu.Menu;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

import static game.Main.saveGame;

public class InputHandler {
    private static Scanner scanner = new Scanner(System.in);

    public static void setScanner(Scanner scanner) {
        InputHandler.scanner = scanner;
    }

    //Рекурсию не убирать (тест на отсечку stack-а)
    public static int getIntInput() {
        try {
            Menu.print("> ");
            return scanner.nextInt();
        } catch (Exception e) {
            if (checkForSaveCommand()) {
                Menu.println("Game Saved");
                Menu.print("> ");
            } else {
                invalidInput();
            }
            return getIntInput();
        }
    }

    public static void invalidInput() {
        Menu.errorMessage("Неверный ввод. Повторите попытку:", true);
    }

    public static String getStringInput() {
        do {
            String input;
            input = scanner.nextLine();
            if (input.getBytes().length != 0)
                return input;
        } while (true);
    }

    public static boolean checkForSaveCommand() {
        if (scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();
            if ("SAVE".equalsIgnoreCase(input)) {
                Menu.println("Сохранение игры...");
                saveGame(false);
                Menu.println("Игра сохранена в файл!");
                return true;
            }
        }
        return false;
    }
}