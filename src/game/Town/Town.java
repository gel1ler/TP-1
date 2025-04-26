package game.Town;

import game.Castle.Buildings.Building;
import game.Player.Player;
import game.Town.Buildings.Barbershop;
import game.Town.Buildings.Cafe;
import game.Town.Buildings.Hotel;
import game.Utils.Menu.BuildingMenu;

import java.util.ArrayList;
import java.util.List;

public class Town {
    private static final List<TownBuilding> buildings = new ArrayList<>();

    public static void startLife() {
        buildings.add(new Hotel());
        buildings.add(new Cafe());
        buildings.add(new Barbershop());

        NpcManager.init(buildings);
    }

    public static void enterBuilding(String name, Player player) {
        if (player.isBusy()) {
            BuildingMenu.println("Вы уже пользуетесь какой-то услугой. Приходите когда завершите ее");
            return;
        }
        switch (name) {
            case "hotel" -> {
                Hotel hotel = (Hotel) buildings.getFirst();
                if (isFull(hotel)) break;
                Service service = hotel.selectService(player);
                if (service == null) break;
                int hp = service.getName().contains("Короткий") ? 20 : 30;
                hotel.interact(player, service, hotel.chill(player, hp));
            }
            case "cafe" -> {
                Cafe cafe = (Cafe) buildings.get(1);
                if (isFull(cafe)) break;
                Service service = cafe.selectService(player);
                if (service == null) break;
                int mp = service.getName().contains("перекус") ? 20 : 30;
                cafe.interact(player, service, cafe.eating(player, mp));
            }
            case "barbershop" -> {
                Barbershop barbershop = (Barbershop) buildings.get(2);
                if (isFull(barbershop)) break;
                Service service = barbershop.selectService(player);
                if (service == null) break;
                boolean empty = service.getName().contains("Просто");
                barbershop.interact(player, service, empty ? () -> {
                } : barbershop.haircut(player));
            }
        }
    }

    private static boolean isFull(TownBuilding building) {
        if (building.isAvailable())
            return false;

        BuildingMenu.println(building.getName() + " в данный момент недоступен. Заходите позже!");
        return true;
    }
}
