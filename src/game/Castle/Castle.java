package game.Castle;

import game.Castle.Buildings.*;
import game.Castle.Buildings.Tavern.Tavern;
import game.Player.Player;
import game.Town.Town;
import game.Utils.InputHandler;
import game.Utils.Menu.BuildingMenu;
import game.Utils.Menu.GameMenu;
import game.Utils.Menu.Menu;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Castle extends Shop<Building> {
    private final List<Building> buildings = new ArrayList<>();
    private final Player player;

    public Castle(Player player) {
        super(player, createAvailableBuildings(player));

        this.player = player;
        //TEST
//        buildings.add(new Tavern(player));
//        buildings.add(new Hub(player));
    }

    private static List<Building> createAvailableBuildings(Player player) {
        List<Building> availableBuildings = new ArrayList<>();
        availableBuildings.add(new Tavern(player));
        availableBuildings.add(new Hub(player));
        availableBuildings.add(new Stable(player));
        return availableBuildings;
    }

    public boolean hasBuilding(String name) {
        return buildings.stream().anyMatch(i -> Objects.equals(i.getName(), name));
    }

    public void buyBuilding() {
        showAvailableItems();

        int selected = InputHandler.getIntInput();
        while (selected != 0) {
            Building item = getAvailableItems().get(selected - 1);
            addBuildingToCastle(item);
            showAvailableItems();
            selected = InputHandler.getIntInput();
        }
    }

    public void addBuildingToCastle(Building item) {
        if (player.hasBuilding(item)) {
            BuildingMenu.errorMessage("У вас уже есть это здание");
            return;
        }
        if (player.canAfford(item)) {
            buyItem(item);
            buildings.add(item);
        } else {
            BuildingMenu.println("Недостаточно золота для покупки: " + item.getName());
        }
    }

    private void enterBuilding() {
        if (!buildings.isEmpty()) {
            BuildingMenu.showEnterBuilding();
            Menu.displayMenu(buildings, false);
            int selected = InputHandler.getIntInput();

            while (selected != 0) {
                try {
                    buildings.get(selected - 1).interact();
                } catch (Exception e) {
                    GameMenu.errorMessage("Ошибка при сохранении");
                    throw e;
                }

                BuildingMenu.showEnterBuilding();
                Menu.displayMenu(buildings, false);
                selected = InputHandler.getIntInput();
            }
        } else {
            BuildingMenu.println("У вас нет зданий.");
        }
    }

    private void enterTown() {
        String[] townBuildingsRus = {"Кафе «Сырники от тети Глаши»", "Парикмахерская «Отрезанное ухо»"};
        String[] townBuildings = {"cafe", "barbershop"};
        BuildingMenu.displayAvailiableBuildings(townBuildingsRus);

        int selected = InputHandler.getIntInput();

        Town.enterBuilding(townBuildings[selected - 1], player);
    }

    public void enter() {
        BuildingMenu.println("\nВы в замке.");
        String[] menuItems = {"Купить здание", "Войти в здание", "Войти в город"};
        BuildingMenu.displayAvailiableBuildings(menuItems);

        int selected = InputHandler.getIntInput();
        while (selected != 0) {
            switch (selected) {
                case 1:
                    buyBuilding();
                    break;
                case 2:
                    enterBuilding();
                    break;
                case 3:
                    enterTown();
                    break;
                default:
                    BuildingMenu.wrongChoice();
            }
            BuildingMenu.displayAvailiableBuildings(menuItems);
            selected = InputHandler.getIntInput();
        }
    }
}