package game.Town;

import game.Castle.Buildings.Building;
import game.Player.Player;
import game.Town.Buildings.Barbershop;
import game.Town.Buildings.Cafe;
import game.Town.Buildings.Hotel;
import game.Utils.Menu.BuildingMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Town {

    private static final Map<String, TownBuilding> buildings = new HashMap<>();

    public static void startLife() {
//        buildings.put("hotel", new Hotel());
        buildings.put("cafe", new Cafe());
        buildings.put("barbershop", new Barbershop());
        NpcManager.init(new ArrayList<>(buildings.values()));
    }


    public static void enterBuilding(String name, Player player) {
        TownBuilding building = buildings.get(name);
        if (building != null) {
            building.enter(player, false);
        }
    }

//    public static void enterBuilding(String name, Player player) {
//        if (player.isBusy()) {
//            BuildingMenu.println("Вы уже пользуетесь какой-то услугой. Приходите когда завершите ее");
//            return;
//        }
//        switch (name) {
//            case "hotel" -> {
//                Hotel hotel = (Hotel) buildings.getFirst();
//                if (isFull(hotel, player)) break;
//
//                Service service = hotel.selectService(player);
//                if (service == null) break;
//                int hp = service.getName().contains("Короткий") ? 20 : 30;
//                hotel.interact(player, service, hotel.chill(player, hp));
//            }
//            case "cafe" -> {
//                Cafe cafe = (Cafe) buildings.get(1);
//                if (isFull(cafe, player)) break;
//
//                Service service = cafe.selectService(player);
//                if (service == null) break;
//                int mp = service.getName().contains("перекус") ? 20 : 30;
//                cafe.interact(player, service, cafe.eating(player, mp));
//            }
//            case "barbershop" -> {
//                Barbershop barbershop = (Barbershop) buildings.get(2);
//                if (isFull(barbershop, player)) break;
//
//                Service service = barbershop.selectService(player);
//                if (service == null) break;
//                boolean simple = service.getName().contains("Просто");
//                barbershop.interact(player, service, simple ? () -> {
//                } : barbershop.haircut(player));
//            }
//        }
//    }
}
