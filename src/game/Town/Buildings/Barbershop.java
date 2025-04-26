package game.Town.Buildings;

import game.Player.Entities.Hero;
import game.Player.Entities.Unit;
import game.Player.Player;
import game.Town.Service;
import game.Town.TownBuilding;
import game.Utils.Menu.BuildingMenu;

import java.util.ArrayList;
import java.util.List;

public class Barbershop extends TownBuilding {
    private transient Runnable haircutAction;

    public Barbershop() {
        super("Парикмахерская «Отрезанное ухо»", 2, createAvailableItems());
    }

    private static List<Service> createAvailableItems() {
        List<Service> availableServices = new ArrayList<>();

        availableServices.add(new Service("Просто стрижка (бонусов нет, длит. 10 минут)",
                10, 100));
        availableServices.add(new Service("(Модная стрижка – сокращение времени захвата замка, длит. 30 минут)",
                40, 300));

        return availableServices;
    }


    public Runnable haircut(Player player) {
        haircutAction = () -> {
            player.setInvasionTime(1);
            BuildingMenu.println("Время захвата замка сократилась с 2х до 1го хода");
        };
        return haircutAction;
    }
}