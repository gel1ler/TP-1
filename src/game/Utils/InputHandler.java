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
    private static final int MAX_DEPTH = 100;
    private static int currentDepth = 0;

    public static void setScanner(Scanner scanner) {
        InputHandler.scanner = scanner;
    }

    //Рекурсию не убирать (тест на отсечку stack-а)
    public static int getIntInputOld() {
        try {
            Menu.print("> ");
            return scanner.nextInt();
        } catch (Exception e) {
            if (checkForSaveCommand()) {
                Menu.println("Game Saved");
                Menu.print("> ");
            } else {
                //Игнорировать вывод в консоль
//                invalidInput();
            }
            return getIntInputOld();
        }
    }

    public static int getIntInput() {
        if (currentDepth > MAX_DEPTH) {
            currentDepth = 0;
            throw new IllegalStateException("Слишком много попыток ввода. Повторите попытку позже.");
        }
        try {
            currentDepth++;
            Menu.print("> ");
            return scanner.nextInt();
        } catch (Exception e) {
            if (!checkForSaveCommand())
                invalidInput();
            return getIntInput();
        } finally {
            currentDepth--;
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
                boolean saved = saveGame(false);

                if (!saved) {
                    Menu.errorMessage("Вы пытаетесь сохранить игру, которая еще не началась!");
                    return false;
                }

                Menu.println("Игра сохранена в файл!");
                return true;
            }
        }
        return false;
    }
}