package game.Town;

import game.Player.Entities.Hero;
import game.Player.Entities.HeroType;
import game.Player.Entities.Unit;
import game.Player.Entities.UnitType;
import game.Player.Player;

public class Npc extends Player {
    private final boolean busy;
    public Npc(String name) {
        super(name, Integer.MAX_VALUE);
        this.busy = false;

        Hero stockHero = new Hero(HeroType.WIZARD, this);
        stockHero.addUnit(new Unit(UnitType.CAVALRYMAN, this));

        this.addHero(stockHero);
    }

    public boolean isBusy() {
        return busy;
    }
}
