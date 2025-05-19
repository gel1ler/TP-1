package game.Utils.Menu;

import game.Player.Player;

import java.util.LinkedList;
import java.util.Queue;

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

    public static void checkQueuePosition(Queue<Player> queue){
        if (queue.isEmpty()) {
            BuildingMenu.println("Очередь пуста.");
            return;
        }

        int position = 0;
        boolean found = false;

        // Находим позицию игрока в очереди
        for (Player p : queue) {
            position++;
            if (p.isPerson()) {
                found = true;
                break;
            }
        }

        if (!found) {
            BuildingMenu.println("Вы не в очереди.");
            return;
        }

        if (position > 1) {
            BuildingMenu.println("Перед вами:");
            int count = 0;
            for (Player p : queue) {
                if (++count >= position) break;
                BuildingMenu.println(count + ". " + p.getName());
            }
        }
    }
}
