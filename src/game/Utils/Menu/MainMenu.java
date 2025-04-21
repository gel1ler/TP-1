package game.Utils.Menu;

import game.Player.OwnerType;

public class MainMenu extends Menu {
    public static void showStartMenu() {
        println("1 - Новая игра");
        println("2 - Загрузить сохранение");
        println("3 - Редактор карт");
    }

    public static void registrationForm() {
        printFormattedMessage("Регистрация / Вход");
        print("Введите имя: ");
    }

    public static void printGameEnd(OwnerType looser) {
        println(looser.name() + " проиграл...");
        printFormattedMessage("игра закончилась");
    }
}