package game.Castle.Buildings;

import game.Castle.Buy;
import game.Player.OwnerType;

public abstract class Building extends Buy {
    public Building(String name, int cost, OwnerType owner) {
        super(name, cost, owner);
    }

    public abstract void interact();
}
