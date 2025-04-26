package game.Utils.Menu;

import game.Player.OwnerType;
import game.Player.Entities.Entity;
import game.Player.Entities.Hero;
import game.Player.Entities.Unit;

import java.util.List;

public class GameMenu extends Menu {
    public static void printInvasion(Hero hero) {
        println(hero.getOwnerType().name() + " захватывает замок!");
    }

    public static void showAvailableHeroActions(int[] enemyCords, int[] castleCords) {
        String helperText = "";

        if (enemyCords != null) {
            helperText += "3 - Начать Битву\t\t";
        }
        if (castleCords != null) {
            helperText += "4 - Войти в замок\t\t";
        }

        println("Выберите действие:");
        println("1 - Переместить Героя\t\t2 - Пропустить ход\t\t" + helperText + "0 - Выбрать другого Героя\t\t10 - войти в замок");
    }

    public static void showAvailableUnitActions(int[] enemyCords, Unit selectedUnit) {
        String helperText = "";
        if (selectedUnit.getMP() != 0) helperText += "1 - Переместить Юнита\t\t";
        helperText += "2 - Пропустить ход \t\t";
        if (enemyCords != null && selectedUnit.getDamage() != 0) helperText += "3 - Атаковать\t\t";
        if(selectedUnit.haveSuperAbility()) helperText += "4 - Использовать суперспособность\t\t";

        println("Выберите действие:");
        println(helperText + "0 - Выбрать другого Юнита");
    }

    public static void chooseEntity(List<? extends Entity> entities, String entityType) {
        println("Выберите " + entityType + ":");
        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            println((i + 1) + " - " + entity.getName() + " на (" + entity.getY() + ", " + entity.getX() + ")");
        }
    }

    public static void chooseMapSave(){
        println("Выберите карту:");
        println("0 - База, основа так сказать");
        println("1 - Карты сообщества");
    }

    public static void showAvailiableMoves(int mp) {
        println(mp + " - очков передвижения");
        println("Выберите направление:");
        println("7  8  9");
        println("4     6");
        println("1  2  3");
        println("Или введите 0 для завершения хода");
    }

    public static void printFightEnd(OwnerType owner) {
        String whose = owner == OwnerType.COMPUTER ? "компьютера" : "человека";
        println("Все юниты " + whose + " уничтожены!");
        printFormattedMessage("Битва закончена");
    }

    public static void displayHeroes(List<Hero> heroes) {
        Menu.println("Выберите Героя:");
        for (int i = 0; i < heroes.size(); i++) {
            print((i + 1) + " - ");
            heroes.get(i).display();
        }
        Menu.println("0 - Выйти");
    }

    public static void mapEditorChoose(){
        println("1 - Редактировать карту\t\t0 - Продолжить с базовой картой");
    }
}
