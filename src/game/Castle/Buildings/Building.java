package game.Castle.Buildings;

import game.Castle.Buy;
import game.Town.Customer;
import game.Player.Player;

import java.util.ArrayList;
import java.util.List;


public abstract class Building extends Buy {
    protected Player owner;

    public Building(String name, int cost, Player owner) {
        super(name, cost, owner);
        this.owner = owner;
    }

    public abstract void interact();

}
