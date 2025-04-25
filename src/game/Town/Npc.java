package game.Town;

import game.Player.Player;

public class Npc extends Player {
    private boolean busy;
    public Npc(String name) {
        super(name);
        this.busy = false;
    }

    public boolean isBusy() {
        return busy;
    }

    public void startVisiting() {
        // Каждую секунду с рандомным (малым) шансом свободный NPC может захотеть куда то сходить.
    }
}
