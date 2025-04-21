package game.Castle;

import game.Player.Player;
import game.Utils.Menu.BuildingMenu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static game.Main.saveGame;

public class Shop<T extends Buy> implements Serializable {
    private final Player player;
    private final List<T> availableItems;

    public Shop(Player player, List<T> availableItems) {
        this.player = player;
        this.availableItems = new ArrayList<>(availableItems);
    }

    public void buyItem(T item) {
        if (player.canAfford(item)) {
            player.minusGold(item.getCost());
            BuildingMenu.println("Куплено: " + item.getName());
            saveGame(true);
        } else {
            BuildingMenu.println("Недостаточно золота для покупки: " + item.getName());
        }
    }

    public void showAvailableItems() {
        BuildingMenu.showGold(player);
        BuildingMenu.println("\nДоступные позиции:");
        for (int i = 0; i < availableItems.size(); i++) {
            T item = availableItems.get(i);
            BuildingMenu.print((i + 1) + " - " + item.getName() + " (" + item.getCost() + " золота) \t\t");
        }
        BuildingMenu.print("0 - Выйти\n");
    }

    public List<T> getAvailableItems() {
        return availableItems;
    }
}
