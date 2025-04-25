package game.Town;

import game.Player.OwnerType;
import game.Player.Player;
import game.Utils.InputHandler;
import game.Utils.Menu.BuildingMenu;

import java.util.ArrayList;
import java.util.List;

public class Hotel extends TownBuilding {
    public Hotel() {
        super("Отель «У погибшего альпиниста»", 5, createAvailableItems());
    }

    private static List<Service> createAvailableItems() {
        List<Service> availableServices = new ArrayList<>();

        availableServices.add(new Service("Короткий отдых (+2 к здоровью всех юнитов, длит. 1 день)",
                20, 1140, null));
        availableServices.add(new Service("Длинный отдых (+3 к здоровью всех юнитов, длит. 3 дня)",
                15, 4320, null));

        return availableServices;
    }


    public void interact(Player player) {
        int selected;
        List<Service> services = getAvailableItems();
        if (player.getOwnerType() == OwnerType.PERSON) {
            BuildingMenu.println("Вы вошли в Отель «У погибшего альпиниста».");
            BuildingMenu.println(getStatus());
            showAvailableItems();
            selected = InputHandler.getIntInput();
        } else {
            selected = (int) (Math.random() * services.size());
        }

        Service service = getAvailableItems().get(selected - 1);
        buyItem(service);
        visit(service.getName(), service.getDuration(), player);
        service.use();
    }

    public void visitRandom(Player player) {
        List<Service> services = getAvailableItems();
        int rand = (int) (Math.random() * services.size());
        Service service = getAvailableItems().get(rand);
        buyItem(service, player);
        visit(service.getName(), service.getDuration(), player);
        service.use();
    }
}
