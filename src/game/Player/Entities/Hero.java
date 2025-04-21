package game.Player.Entities;

import game.Map.CellType;
import game.Player.OwnerType;
import game.Utils.Menu.GameMenu;

import java.util.ArrayList;
import java.util.List;

public class Hero extends Entity {
    private final List<Unit> units = new ArrayList<>();
    private final HeroType heroType;

    public Hero(HeroType heroType, OwnerType owner) {
        super(heroType.getName(), heroType.getCost(), heroType.getMovementPoints(), heroType.getCellType(), owner);
        this.heroType = heroType;
    }

    public void addUnit(Unit unit) {
        this.units.add(unit);
    }

    public int getUnitsCount() {
        return units.size();
    }

    public void display() {
        GameMenu.println(getName() + "(" + getX() + ", " + getY() + ")");
        if (units.isEmpty()) {
            GameMenu.println("\t- Без юнитов.");
        } else {
            units.forEach(i -> GameMenu.println("\t- " + i.getName()));
        }
    }

    public List<Unit> getUnits() {
        return this.units;
    }

    public void kill(Unit victim) {
        this.units.removeIf(unit -> unit.getX() == victim.getX() && unit.getY() == victim.getY());
    }

    public Unit getUnit(int[] enemyCords) {
        for (Unit i : this.units) {
            if (i.getY() == enemyCords[0] && i.getX() == enemyCords[1])
                return i;
        }
        return null;
    }

    public HeroType getHeroType() {
        return heroType;
    }

    public CellType getCellType() {
        return heroType.getCellType();
    }

    @Override
    public String toString(){
        return this.getName();
    }
}