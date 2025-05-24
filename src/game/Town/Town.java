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
}
