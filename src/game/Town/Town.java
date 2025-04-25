package game.Town;

import game.Player.Player;

import java.util.ArrayList;
import java.util.List;

public class Town {
    private static final List<TownBuilding> buildings = new ArrayList<>();

    public static void startLife() {
        buildings.add(new Hotel());

        NpcManager.init();
        NpcManager.choose(buildings);
    }

    public static void enterBuilding(String name, Player player) {
        switch (name){
            case "hotel" -> buildings.getFirst().chooseService(player);
        }
    }
}
