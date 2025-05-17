package game.Town;

import game.Player.Player;

public class Npc extends Player {
    private final boolean busy;
    public Npc(String name) {
        super(name, Integer.MAX_VALUE);
        this.busy = false;
    }

    public boolean isBusy() {
        return busy;
    }
}
