package game.Player;

import game.Castle.Buildings.Building;
import game.Castle.Buy;
import game.Castle.Castle;
import game.Player.Entities.Hero;
import game.Utils.Menu.GameMenu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static game.Main.incrementStats;

public class Player implements Serializable {
    private final String name;
    private final Castle castle;
    private int gold;
    private final List<Hero> heroes = new ArrayList<>();
    private final OwnerType ownerType;

    public Player(int initialGold, OwnerType ownerType) {
        this.castle = new Castle(this);
        this.gold = initialGold;
        this.ownerType = ownerType;
        this.name = ownerType.getOwner();
    }

    public String getName() {
        return name;
    }

    public Castle getCastle() {
        return castle;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public void plusGold(int gold) {
        this.gold += gold;
    }

    public void minusGold(int gold) {
        this.gold -= gold;
    }

    public boolean canAfford(Buy item) {
        return getGold() >= item.getCost();
    }

    public boolean canAfford(int cost) {
        return getGold() >= cost;
    }

    public void addHero(Hero hero) {
        this.heroes.add(hero);
    }

    public List<Hero> getHeroes() {
        return heroes;
    }

    public boolean hasTavern() {
        return this.castle.hasBuilding("Таверна");
    }

    public boolean hasHub() {
        return this.castle.hasBuilding("Хаб");
    }

    public boolean hasHeroes() {
        return !getHeroes().isEmpty();
    }

    public Hero getHeroByCords(int[] enemyCords) {
        return heroes.stream()
                .filter(hero -> hero.getY() == enemyCords[0] && hero.getX() == enemyCords[1])
                .findFirst()
                .orElse(null);
    }

    public void kill(Hero victim) {
        victim.display();
        GameMenu.println(victim.getOwner().toString());
        if(ownerType == OwnerType.COMPUTER) incrementStats("kills");
        this.heroes.removeIf(unit -> unit.getX() == victim.getX() && unit.getY() == victim.getY());
    }

    public OwnerType getOwnerType() {
        return ownerType;
    }

    public void buyRandom() {

    }

    public boolean hasBuilding(Building item) {
        return this.castle.hasBuilding(item.getName());
    }
}