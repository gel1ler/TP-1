package game.Castle;

import game.Player.OwnerType;

import java.io.Serializable;

public class Buy implements Serializable {
    private final String name;
    private final int cost;
    private OwnerType owner;

    public Buy(String name, int cost, OwnerType owner) {
        this.name = name;
        this.cost = cost;
        this.owner = owner;
    }

    public OwnerType getOwner() {
        return owner;
    }

    public void reverseOwner() {
        if (owner == OwnerType.PERSON) owner = OwnerType.COMPUTER;
        else owner = OwnerType.PERSON;
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }
}
