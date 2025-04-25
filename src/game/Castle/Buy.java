package game.Castle;

import game.Player.OwnerType;
import game.Player.Player;

import java.io.Serializable;

public class Buy implements Serializable {
    protected final String name;
    private final int cost;
    private OwnerType ownerType;

    public Buy(String name, int cost, Player owner) {
        this.name = name;
        this.cost = cost;
        this.ownerType= owner != null ? owner.getOwnerType() : null;
    }

    public OwnerType getOwnerType() {
        return ownerType;
    }

    public void reverseOwner() {
        if (ownerType == OwnerType.PERSON) ownerType = OwnerType.COMPUTER;
        else ownerType = OwnerType.PERSON;
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }
}
