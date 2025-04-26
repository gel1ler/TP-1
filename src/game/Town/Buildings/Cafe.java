package game.Town.Buildings;

import game.Player.Entities.Hero;
import game.Player.Entities.Unit;
import game.Player.Player;
import game.Town.Service;
import game.Town.TownBuilding;
import game.Utils.Menu.BuildingMenu;

import java.util.ArrayList;
import java.util.List;

public class Cafe extends TownBuilding {
    private transient Runnable eatAction;

    public Cafe() {
        super("Кафе «Сырники от тети Глаши»", 12, createAvailableItems());
    }

    private static List<Service> createAvailableItems() {
        List<Service> availableServices = new ArrayList<>();

        availableServices.add(new Service("Просто перекус (+20 к перемещению всех юнитов, длит. 15 минут)",
                20, 150));
        availableServices.add(new Service("Плотный обед (+30 к перемещению всех юнитов, длит. 30 минут)",
                25, 300));

        return availableServices;
    }

    public Runnable eating(Player player, int mp) {
        eatAction = () -> {
            for (Hero hero : player.getHeroes()) {
                for (Unit unit : hero.getUnits()) {
                    unit.plusMP(mp);
                }
            }
            BuildingMenu.println("Очки передвижения всех ваших Юнитов были увеличены на " + mp + "!");
        };

        return eatAction;
    }
}