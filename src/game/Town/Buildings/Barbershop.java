package game.Town.Buildings;

import game.Player.Player;
import game.Town.Service;
import game.Town.TownBuilding;

import java.util.ArrayList;
import java.util.List;

public class Barbershop extends TownBuilding {
    private transient Runnable haircutAction;

    public Barbershop() {
        super("Парикмахерская «Отрезанное ухо»", 2, createAvailableItems());
    }

    @Override
    protected void performAction(Player player, Service service) {
        boolean simple = service.getName().contains("Просто");
        if(simple) haircut(player);
    }

    private static List<Service> createAvailableItems() {
        List<Service> availableServices = new ArrayList<>();

        availableServices.add(new Service("Просто стрижка (бонусов нет, длит. 10 минут)",
                10, 10));
        availableServices.add(new Service("(Модная стрижка – сокращение времени захвата замка, длит. 30 минут)",
                40, 30));

        return availableServices;
    }


    public void haircut(Player player) {
    }
}