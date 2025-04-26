package game.Town.Buildings;

import game.Player.Entities.Hero;
import game.Player.Entities.Unit;
import game.Player.Player;
import game.Town.Service;
import game.Town.TownBuilding;
import game.Utils.Menu.BuildingMenu;

import java.util.ArrayList;
import java.util.List;

public class Hotel extends TownBuilding {
    private transient Runnable chillAction;

    public Hotel() {
        super("Отель «У погибшего альпиниста»", 5, createAvailableItems());
    }

    private static List<Service> createAvailableItems() {
        List<Service> availableServices = new ArrayList<>();

        availableServices.add(new Service("Короткий отдых (+20 к здоровью всех юнитов, длит. 1 день)",
                20, 200));
        availableServices.add(new Service("Длинный отдых (+30 к здоровью всех юнитов, длит. 3 дня)",
                15, 600));

        return availableServices;
    }


    public Runnable chill(Player player, int hp) {
        chillAction = () -> {
            for (Hero hero : player.getHeroes()) {
                for (Unit unit : hero.getUnits()) {
                    unit.addHp(hp);
                }
            }
            BuildingMenu.println("HP всех ваших Юнитов было увеличено на " + hp + "!");
        };
        return chillAction;
    }

    public void testInteract(Player p, Service s, Runnable r) {
        this.interact(p, s, r);
    }
}
