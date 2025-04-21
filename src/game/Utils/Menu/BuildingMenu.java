package game.Utils.Menu;

import game.Player.Player;

public class BuildingMenu extends Menu {
    public static void showGold(Player player) {
        BuildingMenu.println("----------\nКоличество золота: " + player.getGold());
    }

    public static void showEnterBuilding() {
        println("==Войти в здание==");
    }

    public static void displayAvailiableBuildings(String[] items){
        for (int i = 0; i < items.length; i++) {
            print((i + 1) + " - " + items[i] + "\t\t");
        }
        print("0 - Выйти \n");
    }
}
