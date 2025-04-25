package game.Town;


import game.Castle.Buildings.Building;
import game.Player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NpcManager {
    private static final List<Npc> npcs = new ArrayList<>();
    private static final List<Npc> freeNpcs = new ArrayList<>();

    public static void init() {
        for (int i = 0; i < 10; i++) {
            npcs.add(new Npc("Npc " + i));
            freeNpcs.add(new Npc("Npc " + i));
        }
    }

    public void startLife(){
        for (Npc npc: freeNpcs) {
            npc.startVisiting();
        }
    }

    public static void choose(List<TownBuilding> buildings) {
        // Каждую секунду с рандомным (малым) шансом свободный NPC может захотеть куда то сходить.
        // Когда захотел - выбирает рандомную услугу
    }
}
