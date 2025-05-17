package game.Town;


import game.Castle.Buildings.Building;
import game.Player.Player;
import game.Utils.GameTime;

import java.util.*;

public class NpcManager {
    private static final List<Npc> npcs = new ArrayList<>();
    private static final List<Npc> freeNpcs = new ArrayList<>();
    private static Thread lifeThread;
    private static volatile boolean isRunning = true;
    private static final Random random = new Random();
    private static List<TownBuilding> townBuildings;

    public static void init(List<TownBuilding> buildings) {
        townBuildings = buildings;

        for (int i = 0; i < 10; i++) {
            npcs.add(new Npc("Npc " + i));
            freeNpcs.add(new Npc("Npc " + i));
        }

        startLife();
    }

    public static void startLife() {
        lifeThread = new Thread(() -> {
            while (isRunning) {
                try {
                    Thread.sleep(2000);

                    //Только в рабочие часы
//                    int hour = GameTime.getOnlyHours();
//                    if (hour < 8 || hour > 22) return;

                    for (Npc npc : new ArrayList<>(freeNpcs)) {
                        if (random.nextDouble() < 0.5) {
                            visitRandomBuilding(npc);
                        }
                    }
                } catch (InterruptedException e) {
                    System.out.println("Поток жизни NPC прерван");
                    break;
                }
            }
        });

        lifeThread.start();
    }

    private static void visitRandomBuilding(Npc npc) {
        TownBuilding building = townBuildings.get(random.nextInt(townBuildings.size()));

        if (building.currentVisitors.size() >= building.maxVisitors) return;

        List<Service> services = building.getAvailableItems();

        Service service = services.get(random.nextInt(services.size()));

        freeNpcs.remove(npc);
        building.interact(npc, service, () -> freeNpcs.add(npc));
    }

    public static void stop() {
        isRunning = false;
        if (lifeThread != null) {
            lifeThread.interrupt();
        }
    }

    public static List<Npc> getFreeNpcs() {
        return freeNpcs;
    }
}
