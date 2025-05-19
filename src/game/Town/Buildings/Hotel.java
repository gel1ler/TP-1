package game.Town.Buildings;

import game.Player.Entities.Hero;
import game.Player.Entities.Unit;
import game.Player.Player;
import game.Town.SerializableRunnable;
import game.Town.Service;
import game.Town.TownBuilding;
import game.Utils.Menu.BuildingMenu;

import java.util.ArrayList;
import java.util.List;

public class Hotel extends TownBuilding {

    public Hotel() {
        super("Отель «У погибшего альпиниста»", 5, createAvailableItems());
    }

    private static List<Service> createAvailableItems() {
        List<Service> availableServices = new ArrayList<>();

        availableServices.add(new Service("Короткий отдых (+20 к здоровью всех юнитов, длит. 1 день)",
                20, 60*24));
        availableServices.add(new Service("Длинный отдых (+30 к здоровью всех юнитов, длит. 3 дня)",
                15, 60*24*3));

        return availableServices;
    }

    @Override
    protected void performAction(Player player, Service service) {
        int hp = service.getName().contains("Короткий") ? 20 : 30;
        chill(player, hp);
    }


    public SerializableRunnable chill(Player player, int hp) {
        return () -> {
            for (Hero hero : player.getHeroes()) {
                for (Unit unit : hero.getUnits()) {
                    unit.addHp(hp);
                }
            }
            BuildingMenu.println("HP всех ваших Юнитов было увеличено на " + hp + "!");
        };
    }

    public void testInteract(Player p, Service s, SerializableRunnable r) {
        this.interact(p, s, r);
    }
}
